package com.dragonball.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dragonball.app.data.model.CharacterAffiliation
import com.dragonball.app.data.model.CharacterFilters
import com.dragonball.app.data.model.CharacterGender
import com.dragonball.app.data.model.CharacterRace
import com.dragonball.app.data.model.UiState
import com.dragonball.app.ui.components.CharacterCard
import com.dragonball.app.ui.components.ErrorScreen
import com.dragonball.app.ui.components.PaginationControls
import com.dragonball.app.ui.components.SectionHeader
import com.dragonball.app.ui.components.ShimmerBox
import com.dragonball.app.ui.theme.DarkBackground
import com.dragonball.app.ui.theme.DarkCard
import com.dragonball.app.ui.theme.DarkCardElevated
import com.dragonball.app.ui.theme.DragonOrange
import com.dragonball.app.ui.theme.DragonOrangeLight
import com.dragonball.app.ui.theme.GradientEnd
import com.dragonball.app.ui.theme.GradientMid
import com.dragonball.app.ui.theme.GradientStart
import com.dragonball.app.ui.theme.KiYellow
import com.dragonball.app.ui.theme.TextMuted
import com.dragonball.app.ui.theme.TextSecondary
import com.dragonball.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCharacterClick: (Int) -> Unit,
    onPlanetsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val charactersState by viewModel.charactersState.collectAsState()
    val filterResults by viewModel.filterResults.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showFilterPanel by viewModel.showFilterPanel.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPlanetsClick,
                containerColor = DragonOrange,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Language, contentDescription = "Planets", tint = Color.Black)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(GradientStart, GradientMid, GradientEnd))
                )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding() + 80.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ── Header + Search + Filter panel ───────────────────────────
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(top = 16.dp, bottom = 4.dp)
                    ) {
                        // Title row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "🐉 Dragon Ball",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = DragonOrange
                                )
                                Text(
                                    "58 warriors · 43 transformations",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(listOf(KiYellow, DragonOrange))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("★", style = MaterialTheme.typography.titleLarge, color = Color.Black)
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Search + filter button row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = viewModel::onSearchQueryChange,
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Search by name...", color = TextMuted) },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, null, tint = DragonOrange)
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                            Icon(Icons.Default.Close, null, tint = TextSecondary)
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = DarkCard,
                                    unfocusedContainerColor = DarkCard,
                                    focusedBorderColor = DragonOrange,
                                    unfocusedBorderColor = DarkCardElevated,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = DragonOrange
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            // Filter badge button
                            val activeFilterCount = listOf(
                                filters.race != CharacterRace.ALL,
                                filters.affiliation != CharacterAffiliation.ALL,
                                filters.gender != CharacterGender.ALL
                            ).count { it }
                            BadgedBox(
                                badge = {
                                    if (activeFilterCount > 0) {
                                        Badge(
                                            containerColor = DragonOrange,
                                            contentColor = Color.Black
                                        ) { Text("$activeFilterCount") }
                                    }
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (showFilterPanel) DragonOrange else DarkCard)
                                        .clickable { viewModel.toggleFilterPanel() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.FilterList,
                                        contentDescription = "Filters",
                                        tint = if (showFilterPanel) Color.Black else DragonOrange
                                    )
                                }
                            }
                        }

                        // ── Collapsible filter panel ─────────────────────────
                        AnimatedVisibility(
                            visible = showFilterPanel,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            FilterPanel(
                                filters = filters,
                                onRaceSelected = viewModel::setRaceFilter,
                                onAffiliationSelected = viewModel::setAffiliationFilter,
                                onGenderSelected = viewModel::setGenderFilter,
                                onClearAll = viewModel::clearAllFilters
                            )
                        }

                        Spacer(Modifier.height(4.dp))
                    }
                }

                // ── Content area ─────────────────────────────────────────────
                if (filters.isActive) {
                    // FILTER MODE — flat list, no pagination
                    item(span = { GridItemSpan(2) }) {
                        FilterModeHeader(filters = filters, onClear = viewModel::clearAllFilters)
                    }
                    when (val result = filterResults) {
                        is UiState.Loading -> {
                            items(4, span = { GridItemSpan(1) }) {
                                ShimmerBox(modifier = Modifier.height(230.dp))
                            }
                        }
                        is UiState.Success -> {
                            if (result.data.isEmpty()) {
                                item(span = { GridItemSpan(2) }) { NoResultsView() }
                            } else {
                                item(span = { GridItemSpan(2) }) {
                                    Text(
                                        "${result.data.size} results",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = TextMuted
                                    )
                                }
                                items(result.data, key = { it.id }) { character ->
                                    AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
                                        CharacterCard(character = character, onClick = { onCharacterClick(character.id) })
                                    }
                                }
                            }
                        }
                        is UiState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                ErrorScreen(result.message, viewModel::retry)
                            }
                        }
                    }
                } else {
                    // PAGINATED MODE
                    item(span = { GridItemSpan(2) }) {
                        SectionHeader(title = "Warriors", subtitle = "All Dragon Ball characters")
                    }
                    when (val state = charactersState) {
                        is UiState.Loading -> {
                            items(6, span = { GridItemSpan(1) }) {
                                ShimmerBox(modifier = Modifier.height(240.dp))
                            }
                        }
                        is UiState.Success -> {
                            items(state.data.items, key = { it.id }) { character ->
                                AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
                                    CharacterCard(character = character, onClick = { onCharacterClick(character.id) })
                                }
                            }
                            item(span = { GridItemSpan(2) }) {
                                Column {
                                    Spacer(Modifier.height(8.dp))
                                    PaginationControls(
                                        currentPage = currentPage,
                                        totalPages = totalPages,
                                        onPrevious = viewModel::previousPage,
                                        onNext = viewModel::nextPage
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                        is UiState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                ErrorScreen(state.message, viewModel::retry)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Filter Panel ─────────────────────────────────────────────────────────────

@Composable
private fun FilterPanel(
    filters: CharacterFilters,
    onRaceSelected: (CharacterRace) -> Unit,
    onAffiliationSelected: (CharacterAffiliation) -> Unit,
    onGenderSelected: (CharacterGender) -> Unit,
    onClearAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCard)
            .padding(12.dp)
    ) {
        // Race
        FilterSection(label = "Race") {
            val races = listOf(
                CharacterRace.ALL, CharacterRace.SAIYAN, CharacterRace.HUMAN,
                CharacterRace.NAMEKIAN, CharacterRace.MAJIN, CharacterRace.FRIEZA_RACE,
                CharacterRace.ANDROID, CharacterRace.ANGEL, CharacterRace.GOD,
                CharacterRace.EVIL, CharacterRace.UNKNOWN
            )
            races.forEach { race ->
                FilterChip(
                    label = race.displayName,
                    selected = filters.race == race,
                    onClick = { onRaceSelected(race) }
                )
            }
        }

        HorizontalDivider(color = DarkCardElevated, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

        // Affiliation
        FilterSection(label = "Affiliation") {
            CharacterAffiliation.entries.forEach { aff ->
                FilterChip(
                    label = aff.displayName,
                    selected = filters.affiliation == aff,
                    onClick = { onAffiliationSelected(aff) }
                )
            }
        }

        HorizontalDivider(color = DarkCardElevated, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

        // Gender
        FilterSection(label = "Gender") {
            CharacterGender.entries.forEach { g ->
                FilterChip(
                    label = g.displayName,
                    selected = filters.gender == g,
                    onClick = { onGenderSelected(g) }
                )
            }
        }

        if (filters.isActive) {
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkCardElevated)
                    .clickable(onClick = onClearAll)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Clear all filters", color = DragonOrangeLight, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun FilterSection(
    label: String,
    chips: @Composable () -> Unit
) {
    Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        chips()
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) DragonOrange else DarkCardElevated)
            .border(
                width = if (selected) 0.dp else 0.5.dp,
                color = if (selected) Color.Transparent else DarkCardElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color.Black else TextSecondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ─── Filter Mode Header ───────────────────────────────────────────────────────

@Composable
private fun FilterModeHeader(filters: CharacterFilters, onClear: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Filtered results", style = MaterialTheme.typography.titleSmall,
                color = Color.White, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (filters.name.isNotEmpty())
                    ActiveFilterBadge("\"${filters.name}\"")
                if (filters.race != CharacterRace.ALL)
                    ActiveFilterBadge(filters.race.displayName)
                if (filters.affiliation != CharacterAffiliation.ALL)
                    ActiveFilterBadge(filters.affiliation.displayName)
                if (filters.gender != CharacterGender.ALL)
                    ActiveFilterBadge(filters.gender.displayName)
            }
        }
        IconButton(onClick = onClear) {
            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary)
        }
    }
}

@Composable
private fun ActiveFilterBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(DragonOrange.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, color = DragonOrangeLight)
    }
}

@Composable
private fun NoResultsView() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔍", style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(12.dp))
        Text("No warriors found", style = MaterialTheme.typography.titleMedium,
            color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Try adjusting your filters", style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary, textAlign = TextAlign.Center)
    }
}
