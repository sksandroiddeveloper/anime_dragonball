package com.dragonball.app.ui.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dragonball.app.data.model.Transformation
import com.dragonball.app.data.model.UiState
import com.dragonball.app.ui.components.AffiliationBadge
import com.dragonball.app.ui.components.DragonBallLoadingScreen
import com.dragonball.app.ui.components.ErrorScreen
import com.dragonball.app.ui.components.InfoChip
import com.dragonball.app.ui.components.KiPowerBar
import com.dragonball.app.ui.components.NetworkImage
import com.dragonball.app.ui.components.SectionHeader
import com.dragonball.app.ui.components.StatRow
import com.dragonball.app.ui.theme.AliveGreen
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
import com.dragonball.app.viewmodel.CharacterDetailViewModel

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onBack: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val characterState by viewModel.characterState.collectAsState()
    val selectedTransformation by viewModel.selectedTransformationIndex.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.loadCharacter(characterId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientStart, GradientMid, GradientEnd)))
    ) {
        when (val state = characterState) {
            is UiState.Loading -> DragonBallLoadingScreen()
            is UiState.Error -> ErrorScreen(state.message) { viewModel.retry(characterId) }
            is UiState.Success -> {
                val character = state.data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // ── Hero image ───────────────────────────────────────────
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                            NetworkImage(
                                url = character.image,
                                contentDescription = character.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
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
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // ── Name & badges ─────────────────────────────────────────
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                InfoChip(character.race, DragonOrange)
                                InfoChip(character.gender, MaterialTheme.colorScheme.secondary)
                                AffiliationBadge(character.affiliation)
                            }
                        }
                    }

                    // ── Ki power card ─────────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCard),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SectionHeader(title = "⚡ Ki Power Level")
                                Spacer(Modifier.height(16.dp))
                                // ki and maxKi are stored as formatted strings (e.g. "60.000.000")
                                KiPowerBar(
                                    label = "Base Ki",
                                    value = character.ki,
                                    maxValue = character.maxKi
                                )
                            }
                        }
                    }

                    // ── Stats card ────────────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCard),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SectionHeader(title = "📋 Stats")
                                Spacer(Modifier.height(12.dp))
                                StatRow("Race", character.race, DragonOrangeLight)
                                HorizontalDivider(
                                    color = DarkCardElevated,
                                    thickness = 0.5.dp
                                )
                                StatRow("Gender", character.gender)
                                HorizontalDivider(
                                    color = DarkCardElevated,
                                    thickness = 0.5.dp
                                )
                                StatRow("Affiliation", character.affiliation, AliveGreen)
                                // originPlanet is only present on single-character calls
                                character.originPlanet?.let { planet ->
                                    HorizontalDivider(
                                        color = DarkCardElevated,
                                        thickness = 0.5.dp
                                    )
                                    StatRow(
                                        label = "Origin Planet",
                                        value = "${planet.name} ${if (planet.isDestroyed) "💥" else "🌱"}",
                                        valueColor = KiYellow
                                    )
                                }
                            }
                        }
                    }

                    // ── Description ───────────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCard),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SectionHeader(title = "📖 About")
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = character.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                                )
                            }
                        }
                    }

                    // ── Transformations ───────────────────────────────────────
                    // Transformations array: [{ id, name, image, ki, deletedAt }]
                    // Empty array if character has no transformations
                    if (character.transformations.isNotEmpty()) {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                                SectionHeader(
                                    title = "✨ Transformations",
                                    subtitle = "${character.transformations.size} forms unlocked"
                                )
                                Spacer(Modifier.height(12.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(end = 4.dp)
                                ) {
                                    itemsIndexed(character.transformations) { index, transformation ->
                                        TransformationCard(
                                            transformation = transformation,
                                            isSelected = index == selectedTransformation,
                                            onClick = { viewModel.selectTransformation(index) }
                                        )
                                    }
                                }
                                Spacer(Modifier.height(12.dp))
                                character.transformations.getOrNull(selectedTransformation)?.let { t ->
                                    TransformationDetail(t)
                                }
                            }
                        }
                    }

                    // ── Bottom safe area ──────────────────────────────────────
                    item {
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }
            }
        }
    }
}

@Composable
private fun TransformationCard(
    transformation: Transformation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )
    Card(
        modifier = Modifier
            .width(100.dp)
            .scale(scale)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) DragonOrange else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DarkCardElevated else DarkCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            NetworkImage(
                url = transformation.image,
                contentDescription = transformation.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = transformation.name,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) DragonOrange else TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun TransformationDetail(transformation: Transformation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkCardElevated),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transformation.name,
                style = MaterialTheme.typography.titleSmall,
                color = DragonOrangeLight,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (transformation.ki.isNotEmpty()) {
                Text(
                    text = "Ki: ${transformation.ki}",
                    style = MaterialTheme.typography.labelMedium,
                    color = KiYellow
                )
            }
        }
    }
}
