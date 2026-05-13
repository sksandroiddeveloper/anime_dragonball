    package com.dragonball.app.ui.screens.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dragonball.app.data.model.Character
import com.dragonball.app.data.model.UiState
import com.dragonball.app.ui.components.DragonBallLoadingScreen
import com.dragonball.app.ui.components.ErrorScreen
import com.dragonball.app.ui.components.InfoChip
import com.dragonball.app.ui.components.NetworkImage
import com.dragonball.app.ui.components.SectionHeader
import com.dragonball.app.ui.theme.AliveGreen
import com.dragonball.app.ui.theme.DarkBackground
import com.dragonball.app.ui.theme.DarkCard
import com.dragonball.app.ui.theme.DarkCardElevated
import com.dragonball.app.ui.theme.DestroyedRed
import com.dragonball.app.ui.theme.DragonOrange
import com.dragonball.app.ui.theme.GradientEnd
import com.dragonball.app.ui.theme.GradientMid
import com.dragonball.app.ui.theme.GradientStart
import com.dragonball.app.ui.theme.TextSecondary
import com.dragonball.app.viewmodel.PlanetDetailViewModel

@Composable
fun PlanetDetailScreen(
    planetId: Int,
    onBack: () -> Unit,
    viewModel: PlanetDetailViewModel = hiltViewModel()
) {
    val planetState by viewModel.planetState.collectAsState()

    LaunchedEffect(planetId) { viewModel.loadPlanet(planetId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientStart, GradientMid, GradientEnd)))
    ) {
        when (val state = planetState) {
            is UiState.Loading -> DragonBallLoadingScreen()
            is UiState.Error -> ErrorScreen(state.message) { viewModel.retry(planetId) }
            is UiState.Success -> {
                val planet = state.data
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 32.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Hero
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                            NetworkImage(
                                url = planet.image,
                                contentDescription = planet.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, DarkBackground)
                                        )
                                    )
                            )
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(0.5f))
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    "Back", tint = Color.White
                                )
                            }
                        }
                    }

                    // Info
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                text = planet.name,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(Modifier.height(8.dp))
                            InfoChip(
                                label = if (planet.isDestroyed) "💥 Destroyed" else "🌱 Alive",
                                color = if (planet.isDestroyed) DestroyedRed else AliveGreen
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = planet.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    // Residents
                    if (planet.characters.isNotEmpty()) {
                        item {
                            Column(modifier = Modifier.padding(20.dp)) {
                                SectionHeader(
                                    title = "👥 Residents",
                                    subtitle = "${planet.characters.size} known warriors"
                                )
                                Spacer(Modifier.height(12.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(end = 4.dp)
                                ) {
                                    items(planet.characters) { character ->
                                        ResidentCard(character)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResidentCard(character: Character) {
    Card(
        modifier = Modifier.width(100.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            NetworkImage(
                url = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = character.name,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = character.race,
                style = MaterialTheme.typography.labelSmall,
                color = DragonOrange,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
