package com.dragonball.app.data.model

import com.google.gson.annotations.SerializedName

// ─── Character Models ────────────────────────────────────────────────────────

data class CharactersResponse(
    @SerializedName("items") val items: List<Character> = emptyList(),
    @SerializedName("meta") val meta: Meta? = null,
    @SerializedName("links") val links: Links? = null
)

data class Character(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    // Ki is formatted with dots e.g. "60.000.000"
    @SerializedName("ki") val ki: String = "",
    @SerializedName("maxKi") val maxKi: String = "",
    @SerializedName("race") val race: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("affiliation") val affiliation: String = "",
    @SerializedName("deletedAt") val deletedAt: String? = null,
    // Only present on /characters/{id}
    @SerializedName("originPlanet") val originPlanet: Planet? = null,
    @SerializedName("transformations") val transformations: List<Transformation> = emptyList()
)

data class Transformation(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("ki") val ki: String = "",
    @SerializedName("deletedAt") val deletedAt: String? = null
)

// ─── Planet Models ───────────────────────────────────────────────────────────

data class PlanetsResponse(
    @SerializedName("items") val items: List<Planet> = emptyList(),
    @SerializedName("meta") val meta: Meta? = null,
    @SerializedName("links") val links: Links? = null
)

data class Planet(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("isDestroyed") val isDestroyed: Boolean = false,
    @SerializedName("description") val description: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("deletedAt") val deletedAt: String? = null,
    // Only present on /planets/{id}
    @SerializedName("characters") val characters: List<Character> = emptyList()
)

// ─── Pagination ──────────────────────────────────────────────────────────────

data class Meta(
    @SerializedName("totalItems") val totalItems: Int = 0,
    @SerializedName("itemCount") val itemCount: Int = 0,
    @SerializedName("itemsPerPage") val itemsPerPage: Int = 0,
    @SerializedName("totalPages") val totalPages: Int = 0,
    @SerializedName("currentPage") val currentPage: Int = 0
)

/**
 * Pagination links. [previous] is empty string on page 1 (not null).
 * Use hasPrevious / hasNext instead of null-checks.
 */
data class Links(
    @SerializedName("first") val first: String = "",
    @SerializedName("previous") val previous: String = "",
    @SerializedName("next") val next: String = "",
    @SerializedName("last") val last: String = ""
) {
    val hasPrevious: Boolean get() = previous.isNotEmpty()
    val hasNext: Boolean get() = next.isNotEmpty()
}

// ─── UI State ─────────────────────────────────────────────────────────────────

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// ─── Character Filter Enums (exact API values from docs) ─────────────────────

/** Exact race strings accepted by ?race= */
enum class CharacterRace(val apiValue: String, val displayName: String) {
    ALL("", "All Races"),
    HUMAN("Human", "Human"),
    SAIYAN("Saiyan", "Saiyan"),
    NAMEKIAN("Namekian", "Namekian"),
    MAJIN("Majin", "Majin"),
    FRIEZA_RACE("Frieza Race", "Frieza Race"),
    ANDROID("Android", "Android"),
    JIREN_RACE("Jiren Race", "Jiren Race"),
    GOD("God", "God"),
    ANGEL("Angel", "Angel"),
    EVIL("Evil", "Evil"),
    NUCLEICO("Nucleico", "Nucleico"),
    NUCLEICO_BENIGNO("Nucleico benigno", "Nucleico Benigno"),
    UNKNOWN("Unknown", "Unknown");
}

/** Exact affiliation strings accepted by ?affiliation= */
enum class CharacterAffiliation(val apiValue: String, val displayName: String) {
    ALL("", "All"),
    Z_FIGHTER("Z Fighter", "Z Fighter"),
    RED_RIBBON_ARMY("Red Ribbon Army", "Red Ribbon Army"),
    NAMEKIAN_WARRIOR("Namekian Warrior", "Namekian Warrior"),
    FREELANCER("Freelancer", "Freelancer"),
    ARMY_OF_FRIEZA("Army of Frieza", "Army of Frieza"),
    PRIDE_TROOPERS("Pride Troopers", "Pride Troopers"),
    ASSISTANT_OF_VERMOUD("Assistant of Vermoud", "Asst. of Vermoud"),
    GOD("God", "God"),
    ASSISTANT_OF_BEERUS("Assistant of Beerus", "Asst. of Beerus"),
    VILLAIN("Villain", "Villain"),
    OTHER("Other", "Other");
}

/** Exact gender strings accepted by ?gender= */
enum class CharacterGender(val apiValue: String, val displayName: String) {
    ALL("", "All"),
    MALE("Male", "Male"),
    FEMALE("Female", "Female"),
    UNKNOWN("Unknown", "Unknown");
}

// ─── Active Filter State ──────────────────────────────────────────────────────

/**
 * Holds the current filter state for characters.
 * Filters are mutually exclusive from pagination — the API returns a flat
 * List<Character> (no pagination) when any filter is active.
 */
data class CharacterFilters(
    val name: String = "",
    val race: CharacterRace = CharacterRace.ALL,
    val affiliation: CharacterAffiliation = CharacterAffiliation.ALL,
    val gender: CharacterGender = CharacterGender.ALL
) {
    val isActive: Boolean
        get() = name.isNotEmpty() ||
                race != CharacterRace.ALL ||
                affiliation != CharacterAffiliation.ALL ||
                gender != CharacterGender.ALL

    fun toQueryMap(): Map<String, String> = buildMap {
        if (name.isNotEmpty()) put("name", name)
        if (race != CharacterRace.ALL) put("race", race.apiValue)
        if (affiliation != CharacterAffiliation.ALL) put("affiliation", affiliation.apiValue)
        if (gender != CharacterGender.ALL) put("gender", gender.apiValue)
    }
}

/**
 * Filter state for planets.
 * ?name=... and ?isDestroyed=true|false return flat List<Planet> (no pagination).
 */
data class PlanetFilters(
    val name: String = "",
    val isDestroyed: Boolean? = null
) {
    val isActive: Boolean get() = name.isNotEmpty() || isDestroyed != null

    fun toQueryMap(): Map<String, String> = buildMap {
        if (name.isNotEmpty()) put("name", name)
        isDestroyed?.let { put("isDestroyed", it.toString()) }
    }
}
