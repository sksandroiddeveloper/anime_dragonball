package com.dragonball.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dragonball.app.data.model.Character
import com.dragonball.app.data.model.CharacterAffiliation
import com.dragonball.app.data.model.CharacterFilters
import com.dragonball.app.data.model.CharacterGender
import com.dragonball.app.data.model.CharacterRace
import com.dragonball.app.data.model.CharactersResponse
import com.dragonball.app.data.model.Links
import com.dragonball.app.data.model.UiState
import com.dragonball.app.data.repository.DragonBallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    // ─── Paginated state (no filter active) ───────────────────────────────────

    private val _charactersState = MutableStateFlow<UiState<CharactersResponse>>(UiState.Loading)
    val charactersState: StateFlow<UiState<CharactersResponse>> = _charactersState.asStateFlow()

    /** Page links from the last paginated response — drive prev/next navigation */
    private val _links = MutableStateFlow(Links())
    val links: StateFlow<Links> = _links.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    // ─── Filter state ─────────────────────────────────────────────────────────

    private val _filters = MutableStateFlow(CharacterFilters())
    val filters: StateFlow<CharacterFilters> = _filters.asStateFlow()

    private val _filterResults = MutableStateFlow<UiState<List<Character>>>(UiState.Success(emptyList()))
    val filterResults: StateFlow<UiState<List<Character>>> = _filterResults.asStateFlow()

    // ─── Search query (debounced name filter) ─────────────────────────────────

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ─── Filter panel visibility ──────────────────────────────────────────────

    private val _showFilterPanel = MutableStateFlow(false)
    val showFilterPanel: StateFlow<Boolean> = _showFilterPanel.asStateFlow()

    private var filterJob: Job? = null

    init {
        loadCharacters(page = 1)
        observeSearchDebounce()
    }

    // ─── Paginated loading ────────────────────────────────────────────────────

    fun loadCharacters(page: Int = 1) {
        viewModelScope.launch {
            repository.getCharacters(page = page, limit = 10).collect { state ->
                _charactersState.value = state
                if (state is UiState.Success) {
                    _currentPage.value = state.data.meta?.currentPage ?: page
                    _totalPages.value = state.data.meta?.totalPages ?: 1
                    _links.value = state.data.links ?: Links()
                }
            }
        }
    }

    fun nextPage() {
        val next = _currentPage.value + 1
        if (next <= _totalPages.value) loadCharacters(next)
    }

    fun previousPage() {
        val prev = _currentPage.value - 1
        if (prev >= 1) loadCharacters(prev)
    }

    fun retry() = loadCharacters(_currentPage.value)

    // ─── Search / Filter ──────────────────────────────────────────────────────

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _filters.value = _filters.value.copy(name = query)
        // immediate trigger for empty query (clear results)
        if (query.isEmpty()) runFilters()
    }

    fun setRaceFilter(race: CharacterRace) {
        _filters.value = _filters.value.copy(race = race)
        runFilters()
    }

    fun setAffiliationFilter(affiliation: CharacterAffiliation) {
        _filters.value = _filters.value.copy(affiliation = affiliation)
        runFilters()
    }

    fun setGenderFilter(gender: CharacterGender) {
        _filters.value = _filters.value.copy(gender = gender)
        runFilters()
    }

    fun clearAllFilters() {
        _searchQuery.value = ""
        _filters.value = CharacterFilters()
        _filterResults.value = UiState.Success(emptyList())
    }

    fun toggleFilterPanel() {
        _showFilterPanel.value = !_showFilterPanel.value
    }

    private fun observeSearchDebounce() {
        viewModelScope.launch {
            _searchQuery
                .debounce(450)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        // name-only filter is still a filter call
                        runFilters()
                    }
                }
        }
    }

    private fun runFilters() {
        val currentFilters = _filters.value
        if (!currentFilters.isActive) {
            _filterResults.value = UiState.Success(emptyList())
            return
        }
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            repository.filterCharacters(currentFilters).collect {
                _filterResults.value = it
            }
        }
    }
}
