package com.dragonball.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dragonball.app.data.model.Character
import com.dragonball.app.data.model.Planet
import com.dragonball.app.data.model.PlanetFilters
import com.dragonball.app.data.model.PlanetsResponse
import com.dragonball.app.data.model.UiState
import com.dragonball.app.data.repository.DragonBallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── Character Detail ViewModel ───────────────────────────────────────────────

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    private val _characterState = MutableStateFlow<UiState<Character>>(UiState.Loading)
    val characterState: StateFlow<UiState<Character>> = _characterState.asStateFlow()

    private val _selectedTransformationIndex = MutableStateFlow(0)
    val selectedTransformationIndex: StateFlow<Int> = _selectedTransformationIndex.asStateFlow()

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            repository.getCharacterById(id).collect { _characterState.value = it }
        }
    }

    fun selectTransformation(index: Int) { _selectedTransformationIndex.value = index }
    fun retry(id: Int) = loadCharacter(id)
}

// ─── Planets ViewModel ────────────────────────────────────────────────────────

@HiltViewModel
class PlanetsViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    private val _planetsState = MutableStateFlow<UiState<PlanetsResponse>>(UiState.Loading)
    val planetsState: StateFlow<UiState<PlanetsResponse>> = _planetsState.asStateFlow()

    /** Flat filter results — no pagination when filter is active */
    private val _filterResults = MutableStateFlow<UiState<List<Planet>>>(UiState.Success(emptyList()))
    val filterResults: StateFlow<UiState<List<Planet>>> = _filterResults.asStateFlow()

    private val _filters = MutableStateFlow(PlanetFilters())
    val filters: StateFlow<PlanetFilters> = _filters.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    private var filterJob: Job? = null

    init { loadPlanets() }

    // ─── Paginated ────────────────────────────────────────────────────────────

    fun loadPlanets(page: Int = 1) {
        viewModelScope.launch {
            repository.getPlanets(page = page, limit = 10).collect { state ->
                _planetsState.value = state
                if (state is UiState.Success) {
                    _currentPage.value = state.data.meta?.currentPage ?: page
                    _totalPages.value = state.data.meta?.totalPages ?: 1
                }
            }
        }
    }

    fun nextPage() {
        val next = _currentPage.value + 1
        if (next <= _totalPages.value) loadPlanets(next)
    }

    fun previousPage() {
        val prev = _currentPage.value - 1
        if (prev >= 1) loadPlanets(prev)
    }

    fun retry() = loadPlanets(_currentPage.value)

    // ─── Filters ──────────────────────────────────────────────────────────────

    /**
     * Toggle isDestroyed filter: null (all) → true (destroyed) → false (alive) → null
     */
    fun toggleDestroyedFilter() {
        val next = when (_filters.value.isDestroyed) {
            null -> true
            true -> false
            false -> null
        }
        _filters.value = _filters.value.copy(isDestroyed = next)
        runPlanetFilter()
    }

    fun clearFilters() {
        _filters.value = PlanetFilters()
        _filterResults.value = UiState.Success(emptyList())
    }

    private fun runPlanetFilter() {
        val f = _filters.value
        if (!f.isActive) {
            _filterResults.value = UiState.Success(emptyList())
            return
        }
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            repository.filterPlanets(f).collect { _filterResults.value = it }
        }
    }
}

// ─── Planet Detail ViewModel ──────────────────────────────────────────────────

@HiltViewModel
class PlanetDetailViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    private val _planetState = MutableStateFlow<UiState<Planet>>(UiState.Loading)
    val planetState: StateFlow<UiState<Planet>> = _planetState.asStateFlow()

    fun loadPlanet(id: Int) {
        viewModelScope.launch {
            repository.getPlanetById(id).collect { _planetState.value = it }
        }
    }

    fun retry(id: Int) = loadPlanet(id)
}
