package com.dragonball.app.data.remote

import com.dragonball.app.data.model.Character
import com.dragonball.app.data.model.CharactersResponse
import com.dragonball.app.data.model.Planet
import com.dragonball.app.data.model.PlanetsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface DragonBallApiService {

    // ─── Characters (paginated, no filters) ──────────────────────────────────

    /**
     * Returns paginated CharactersResponse.
     * Do NOT mix page/limit with filter params — filtered calls use filterCharacters().
     */
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<CharactersResponse>

    /**
     * Single character — includes originPlanet and transformations objects.
     */
    @GET("characters/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<Character>

    /**
     * Filter characters by any combination of: name, gender, race, affiliation.
     * Returns a FLAT List<Character> — NO pagination wrapper.
     * Supported values (exact strings):
     *   race: Human | Saiyan | Namekian | Majin | Frieza Race | Android |
     *          Jiren Race | God | Angel | Evil | Nucleico | Nucleico benigno | Unknown
     *   affiliation: Z Fighter | Red Ribbon Army | Namekian Warrior | Freelancer |
     *                Army of Frieza | Pride Troopers | Assistant of Vermoud | God |
     *                Assistant of Beerus | Villain | Other
     *   gender: Male | Female | Unknown
     */
    @GET("characters")
    suspend fun filterCharacters(
        @QueryMap filters: Map<String, String>
    ): Response<List<Character>>

    // ─── Planets (paginated, no filters) ─────────────────────────────────────

    @GET("planets")
    suspend fun getPlanets(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<PlanetsResponse>

    /**
     * Single planet — includes characters array.
     */
    @GET("planets/{id}")
    suspend fun getPlanetById(
        @Path("id") id: Int
    ): Response<Planet>

    /**
     * Filter planets by name or isDestroyed status.
     * Returns a FLAT List<Planet> — NO pagination wrapper.
     *   isDestroyed: true | false
     */
    @GET("planets")
    suspend fun filterPlanets(
        @QueryMap filters: Map<String, String>
    ): Response<List<Planet>>
}
