package com.dragonball.app.data.repository

import com.dragonball.app.data.model.Character
import com.dragonball.app.data.model.CharacterFilters
import com.dragonball.app.data.model.CharactersResponse
import com.dragonball.app.data.model.Planet
import com.dragonball.app.data.model.PlanetFilters
import com.dragonball.app.data.model.PlanetsResponse
import com.dragonball.app.data.model.UiState
import com.dragonball.app.data.remote.DragonBallApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DragonBallRepository @Inject constructor(
    private val apiService: DragonBallApiService
) {

    // ─── Characters — Paginated ───────────────────────────────────────────────

    fun getCharacters(page: Int = 1, limit: Int = 10): Flow<UiState<CharactersResponse>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getCharacters(page, limit)
            if (response.isSuccessful) {
                response.body()?.let { emit(UiState.Success(it)) }
                    ?: emit(UiState.Error("Empty response"))
            } else {
                emit(UiState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error"))
        }
    }

    fun getCharacterById(id: Int): Flow<UiState<Character>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getCharacterById(id)
            if (response.isSuccessful) {
                response.body()?.let { emit(UiState.Success(it)) }
                    ?: emit(UiState.Error("Character not found"))
            } else {
                emit(UiState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error"))
        }
    }

    /**
     * Filters characters using name/race/affiliation/gender.
     * API returns a flat List<Character> — no pagination.
     */
    fun filterCharacters(filters: CharacterFilters): Flow<UiState<List<Character>>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.filterCharacters(filters.toQueryMap())
            if (response.isSuccessful) {
                response.body()?.let { emit(UiState.Success(it)) }
                    ?: emit(UiState.Success(emptyList()))
            } else {
                emit(UiState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error"))
        }
    }

    // ─── Planets — Paginated ──────────────────────────────────────────────────

    fun getPlanets(page: Int = 1, limit: Int = 10): Flow<UiState<PlanetsResponse>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getPlanets(page, limit)
            if (response.isSuccessful) {
                response.body()?.let { emit(UiState.Success(it)) }
                    ?: emit(UiState.Error("Empty response"))
            } else {
                emit(UiState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error"))
        }
    }

    fun getPlanetById(id: Int): Flow<UiState<Planet>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getPlanetById(id)
            if (response.isSuccessful) {
                response.body()?.let { emit(UiState.Success(it)) }
                    ?: emit(UiState.Error("Planet not found"))
            } else {
                emit(UiState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error"))
        }
    }

    /**
     * Filters planets using name and/or isDestroyed.
     * API returns a flat List<Planet> — no pagination.
     */
    fun filterPlanets(filters: PlanetFilters): Flow<UiState<List<Planet>>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.filterPlanets(filters.toQueryMap())
            if (response.isSuccessful) {
                response.body()?.let { emit(UiState.Success(it)) }
                    ?: emit(UiState.Success(emptyList()))
            } else {
                emit(UiState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error"))
        }
    }
}
