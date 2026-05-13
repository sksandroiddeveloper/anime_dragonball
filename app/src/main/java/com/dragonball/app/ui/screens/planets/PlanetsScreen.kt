package com.dragonball.app.ui.screens.planets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dragonball.app.data.model.Planet
import com.dragonball.app.data.model.PlanetFilters
import com.dragonball.app.data.model.UiState
import com.dragonball.app.ui.components.DragonBallLoadingScreen
import com.dragonball.app.ui.components.ErrorScreen
import com.dragonball.app.ui.components.InfoChip
import com.dragonball.app.ui.components.NetworkImage
import com.dragonball.app.ui.components.PaginationControls
import com.dragonball.app.ui.components.ShimmerBox
import com.dragonball.app.ui.theme.AliveGreen
import com.dragonball.app.ui.theme.DarkBackground
import com.dragonball.app.ui.theme.DarkCard
import com.dragonball.app.ui.theme.DarkCardElevated
import com.dragonball.app.ui.theme.DestroyedRed
import com.dragonball.app.ui.theme.DragonOrange
import com.dragonball.app.ui.theme.DragonOrangeLight
import com.dragonball.app.ui.theme.GradientEnd
import com.dragonball.app.ui.theme.GradientMid
import com.dragonball.app.ui.theme.GradientStart
import com.dragonball.app.ui.theme.TextMuted
import com.dragonball.app.ui.theme.TextSecondary
import com.dragonball.app.viewmodel.PlanetsViewModel

@Composable
fun PlanetsScreen(
    onBack: () -> Unit,
    onPlanetClick: (Int) -> Unit,
    viewModel: PlanetsViewModel = hiltViewModel()
) {
    val planetsState by viewModel.planetsState.collectAsState()
    val filterResults by viewModel.filterResults.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.clip(CircleShape).background(DarkCard)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("🌍 Planets", style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black, color = DragonOrange)
                        Text("20 planets in the universe", style = MaterialTheme.typography.labelSmall,
                            color = TextMuted)
                    }
                    // isDestroyed filter toggle
                    val filterLabel = when (filters.isDestroyed) {
                        true -> "💥 Destroyed"
                        false -> "🌱 Alive"
                        null -> "All"
                    }
                    val filterBg = when (filters.isDestroyed) {
                        true -> DestroyedRed.copy(alpha = 0.15f)
                        false -> AliveGreen.copy(alpha = 0.15f)
                        null -> DarkCard
                    }
                    val filterColor = when (filters.isDestroyed) {
                        true -> DestroyedRed
                        false -> AliveGreen
                        null -> TextSecondary
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(filterBg)
                            .clickable { viewModel.toggleDestroyedFilter() }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(filterLabel, style = MaterialTheme.typography.labelSmall,
                            color = filterColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(GradientStart, GradientMid, GradientEnd)))
                .padding(padding)
        ) {
            if (filters.isActive) {
                // ── FILTER RESULTS — flat list ────────────────────────────────
                when (val res = filterResults) {
                    is UiState.Loading -> {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(5) { ShimmerBox(modifier = Modifier.fillMaxWidth().height(130.dp)) }
                        }
                    }
                    is UiState.Success -> {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            item {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()) {
                                    Text("${res.data.size} planets found", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                                    Text("Clear filter", style = MaterialTheme.typography.labelSmall,
                                        color = DragonOrangeLight,
                                        modifier = Modifier.clickable { viewModel.clearFilters() })
                                }
                            }
                            items(res.data, key = { it.id }) { planet ->
                                PlanetCard(planet = planet, onClick = { onPlanetClick(planet.id) })
                            }
                        }
                    }
                    is UiState.Error -> ErrorScreen(res.message, viewModel::retry)
                }
            } else {
                // ── PAGINATED ─────────────────────────────────────────────────
                when (val state = planetsState) {
                    is UiState.Loading -> {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(5) { ShimmerBox(modifier = Modifier.fillMaxWidth().height(130.dp)) }
                        }
                    }
                    is UiState.Success -> {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.data.items, key = { it.id }) { planet ->
                                PlanetCard(planet = planet, onClick = { onPlanetClick(planet.id) })
                            }
                            item {
                                Spacer(Modifier.height(8.dp))
                                PaginationControls(
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                    onPrevious = viewModel::previousPage,
                                    onNext = viewModel::nextPage
                                )
                            }
                        }
                    }
                    is UiState.Error -> ErrorScreen(state.message, viewModel::retry)
                }
            }
        }
    }
}

@Composable
private fun PlanetCard(planet: Planet, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(130.dp)) {
            NetworkImage(
                url = planet.image,
                contentDescription = planet.name,
                modifier = Modifier.width(130.dp).fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(14.dp).weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(planet.name, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    InfoChip(
                        label = if (planet.isDestroyed) "💥 Destroyed" else "🌱 Alive",
                        color = if (planet.isDestroyed) DestroyedRed else AliveGreen
                    )
                }
                Text(
                    text = planet.description.take(80) + if (planet.description.length > 80) "…" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary, maxLines = 3
                )
                // Note: characters[] only populated on /planets/{id}, not in the list.
                // Show status text instead of a count that is always 0.
                Text(
                    if (planet.isDestroyed) "No longer habitable" else "Currently habitable",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (planet.isDestroyed) DestroyedRed.copy(alpha = 0.7f) else AliveGreen.copy(alpha = 0.7f)
                )
            }
        }
    }
}
