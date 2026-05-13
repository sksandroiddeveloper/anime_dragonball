package com.dragonball.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.dragonball.app.data.model.Character
import com.dragonball.app.ui.theme.AliveGreen
import com.dragonball.app.ui.theme.DarkCard
import com.dragonball.app.ui.theme.DarkCardElevated
import com.dragonball.app.ui.theme.DestroyedRed
import com.dragonball.app.ui.theme.DragonOrange
import com.dragonball.app.ui.theme.DragonOrangeLight
import com.dragonball.app.ui.theme.FriezaForceColor
import com.dragonball.app.ui.theme.KiYellow
import com.dragonball.app.ui.theme.NeutralColor
import com.dragonball.app.ui.theme.TextMuted
import com.dragonball.app.ui.theme.TextSecondary
import com.dragonball.app.ui.theme.VillainColor
import com.dragonball.app.ui.theme.ZFighterColor

// ─── Dragon Ball Shimmer ──────────────────────────────────────────────────────

@Composable
fun ShimmerBox(modifier: Modifier = Modifier, cornerRadius: Dp = 12.dp) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerX"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1C2537),
                        Color(0xFF2C3A50),
                        Color(0xFF1C2537)
                    ),
                    start = Offset(shimmerX - 200, 0f),
                    end = Offset(shimmerX, 0f)
                )
            )
    )
}

// ─── Character Card ───────────────────────────────────────────────────────────

@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Image with gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                NetworkImage(
                    url = character.image,
                    contentDescription = character.name,
                    modifier = Modifier.fillMaxSize()
                )
                // Bottom gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DarkCard)
                            )
                        )
                )
                // Affiliation badge
                AffiliationBadge(
                    affiliation = character.affiliation,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            // Info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoChip(label = character.race, color = DragonOrange)
                    InfoChip(label = character.gender, color = MaterialTheme.colorScheme.secondary)
                }
                Spacer(Modifier.height(6.dp))
                // Ki power
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.SignalCellularAlt,
                        contentDescription = "Ki",
                        tint = KiYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Ki: ${character.ki}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

// ─── Info Chip ────────────────────────────────────────────────────────────────

@Composable
fun InfoChip(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .border(0.5.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─── Affiliation Badge ────────────────────────────────────────────────────────

@Composable
fun AffiliationBadge(affiliation: String, modifier: Modifier = Modifier) {
    val color = when {
        affiliation.contains("Z Fighter", true) -> ZFighterColor
        affiliation.contains("Villain", true) -> VillainColor
        affiliation.contains("Frieza", true) -> FriezaForceColor
        else -> NeutralColor
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .border(1.dp, color.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = affiliation.take(12),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─── Ki Power Bar ─────────────────────────────────────────────────────────────

@Composable
fun KiPowerBar(
    label: String,
    value: String,
    maxValue: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Text(value, style = MaterialTheme.typography.labelMedium, color = DragonOrangeLight, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(DarkCardElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(DragonOrange, KiYellow)
                        )
                    )
            )
        }
        Spacer(Modifier.height(2.dp))
        Text("Max: $maxValue", style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

// ─── Network Image ────────────────────────────────────────────────────────────

@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            ShimmerBox(modifier = Modifier.fillMaxSize(), cornerRadius = 0.dp)
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.BrokenImage,
                    contentDescription = "Error",
                    tint = TextMuted,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    )
}

// ─── Loading Screen ───────────────────────────────────────────────────────────

@Composable
fun DragonBallLoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "rotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Dragon Ball spinning loader
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .rotate(rotation)
                    .border(3.dp, DragonOrange, CircleShape)
                    .padding(8.dp)
                    .background(
                        Brush.radialGradient(listOf(KiYellow.copy(0.3f), DragonOrange.copy(0.1f))),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("★", style = MaterialTheme.typography.headlineLarge, color = DragonOrange)
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Gathering Dragon Balls...",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
        }
    }
}

// ─── Error Screen ─────────────────────────────────────────────────────────────

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("⚡", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(16.dp))
            Text(
                "Power Level Too Low!",
                style = MaterialTheme.typography.titleLarge,
                color = DestroyedRed,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = DragonOrange)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Power Up Again!", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ─── Section Header ───────────────────────────────────────────────────────────

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(listOf(DragonOrange, KiYellow))
                    )
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.padding(start = 14.dp)
            )
        }
    }
}

// ─── Stat Row ─────────────────────────────────────────────────────────────────

@Composable
fun StatRow(label: String, value: String, valueColor: Color = Color.White) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─── Pagination Controls ──────────────────────────────────────────────────────

@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPrevious,
            enabled = currentPage > 1,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkCardElevated,
                disabledContainerColor = DarkCard
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("← Prev", color = if (currentPage > 1) DragonOrange else TextMuted)
        }

        Spacer(Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(DarkCard)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "$currentPage / $totalPages",
                style = MaterialTheme.typography.titleSmall,
                color = DragonOrangeLight,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(16.dp))

        Button(
            onClick = onNext,
            enabled = currentPage < totalPages,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkCardElevated,
                disabledContainerColor = DarkCard
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Next →", color = if (currentPage < totalPages) DragonOrange else TextMuted)
        }
    }
}
