package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ambient background wrapper providing a lush botanical gradient backdrop with glowing soft orbs.
 * Translucent frosted glass cards placed on top will reveal the soft glowing colors underneath.
 */
@Composable
fun FrostedGlassBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable BoxScope.() -> Unit
) {
    val topGradientStart = if (isDark) Color(0xFF0D1711) else Color(0xFFE8F2EA)
    val bottomGradientEnd = if (isDark) Color(0xFF080F0B) else Color(0xFFF4F8F5)
    
    val orb1Color = if (isDark) Color(0xFF1E4D2A).copy(alpha = 0.45f) else Color(0xFFA8E6CF).copy(alpha = 0.55f)
    val orb2Color = if (isDark) Color(0xFF2E6F40).copy(alpha = 0.35f) else Color(0xFFDCEDC8).copy(alpha = 0.60f)
    val orb3Color = if (isDark) Color(0xFF81D498).copy(alpha = 0.20f) else Color(0xFFC8E6C9).copy(alpha = 0.50f)
    val accentOrb = if (isDark) Color(0xFFD97706).copy(alpha = 0.15f) else Color(0xFFFEF3C7).copy(alpha = 0.40f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(topGradientStart, bottomGradientEnd)
                )
            )
            .drawBehind {
                val width = size.width
                val height = size.height

                // Soft ambient radial glowing blobs behind the frosted glass cards
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(orb1Color, Color.Transparent),
                        center = Offset(width * 0.15f, height * 0.12f),
                        radius = width * 0.65f
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(orb2Color, Color.Transparent),
                        center = Offset(width * 0.85f, height * 0.35f),
                        radius = width * 0.75f
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(orb3Color, Color.Transparent),
                        center = Offset(width * 0.25f, height * 0.70f),
                        radius = width * 0.70f
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accentOrb, Color.Transparent),
                        center = Offset(width * 0.80f, height * 0.88f),
                        radius = width * 0.50f
                    )
                )
            }
    ) {
        content()
    }
}

/**
 * Reusable Frosted Glass Card component with semi-transparent surface, soft double-light edge border,
 * and elegant rounded corners.
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = RoundedCornerShape(22.dp),
    isDark: Boolean = isSystemInDarkTheme(),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val glassColor = if (isDark) {
        Color(0x9917261C) // Semi-transparent dark green surface
    } else {
        Color(0xC8FFFFFF) // 78% transparent white glass
    }

    val borderBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.25f),
                Color.White.copy(alpha = 0.05f),
                Color.White.copy(alpha = 0.15f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                Color.White.copy(alpha = 0.30f),
                Color.White.copy(alpha = 0.60f)
            )
        )
    }

    Card(
        modifier = modifier
            .border(
                border = BorderStroke(1.2.dp, borderBrush),
                shape = shape
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = glassColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(0.dp)
        ) {
            content()
        }
    }
}

/**
 * Frosted Glass Surface / Badge for chips, search containers, and status bars.
 */
@Composable
fun FrostedGlassSurface(
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = RoundedCornerShape(14.dp),
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val surfaceColor = if (isDark) Color(0xAA1F3325) else Color(0xDDFFFFFF)
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.8f),
            Color.White.copy(alpha = 0.2f)
        )
    )

    Surface(
        modifier = modifier.border(BorderStroke(1.dp, borderBrush), shape),
        shape = shape,
        color = surfaceColor
    ) {
        content()
    }
}
