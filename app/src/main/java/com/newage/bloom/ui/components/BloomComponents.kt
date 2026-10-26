package com.newage.bloom.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newage.bloom.ui.theme.BloomThemeTokens

/**
 * Signature Bloom 4-petal flower icon accurately matching Frame 26080030.
 * Default color is Bloom Rose (#FF528C) with Honey Gold center core.
 */
@Composable
fun BloomFlowerLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    petalColor: Color = BloomThemeTokens.colors.primary,
    centerColor: Color = BloomThemeTokens.colors.tertiary,
    progress: Float = 1.0f
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(size.toPx() / 2, size.toPx() / 2)
            val petalRadius = size.toPx() * 0.22f
            val petalDistance = size.toPx() * 0.23f

            val angles = listOf(0f, 90f, 180f, 270f)
            angles.forEachIndexed { index, angle ->
                val isFilled = progress >= (index / 4.0f + 0.1f)
                val currentColor = if (isFilled) petalColor else petalColor.copy(alpha = 0.22f)

                rotate(angle, pivot = center) {
                    val petalCenter = Offset(center.x, center.y - petalDistance)
                    drawOval(
                        color = currentColor,
                        topLeft = Offset(petalCenter.x - petalRadius * 0.85f, petalCenter.y - petalRadius * 1.15f),
                        size = Size(petalRadius * 1.7f, petalRadius * 2.3f)
                    )
                }
            }

            // Center golden core
            val centerRadius = size.toPx() * 0.13f
            drawCircle(
                color = centerColor,
                radius = centerRadius,
                center = center
            )
        }
    }
}

/**
 * Circular Arc Progress Gauge matching Frame 26080030 ("Today's Wellness Score").
 */
@Composable
fun WellnessScoreGauge(
    scorePercent: Int,
    completedCount: Int = 2,
    totalCount: Int = 3,
    subtitle: String = "Nice progress today",
    badgeText: String = "$completedCount of $totalCount complete",
    size: Dp = 160.dp,
    modifier: Modifier = Modifier
) {
    val colors = BloomThemeTokens.colors
    val animatedScore by animateFloatAsState(
        targetValue = scorePercent.toFloat(),
        animationSpec = tween(durationMillis = 800),
        label = "score_anim"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.95f)) {
            val strokeWidth = 10.dp.toPx()
            val radius = (this.size.minDimension - strokeWidth) / 2
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(radius * 2, radius * 2)

            // Track Arc (240 degrees)
            drawArc(
                color = colors.progressTrack,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress Arc
            val sweepAngle = 240f * (animatedScore / 100f).coerceIn(0f, 1f)
            drawArc(
                color = colors.primary,
                startAngle = 150f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BloomFlowerLogo(
                size = 28.dp,
                petalColor = colors.primary,
                centerColor = colors.tertiary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$scorePercent%",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = badgeText,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.primary
            )
        }
    }
}

/**
 * 4-segment progress bar at the top of onboarding.
 */
@Composable
fun StepProgressBar(
    currentStep: Int,
    totalSteps: Int = 4,
    modifier: Modifier = Modifier
) {
    val colors = BloomThemeTokens.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isFilled = i <= currentStep
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isFilled) colors.primary else colors.progressTrack)
            )
        }
    }
}

/**
 * Top bar for Onboarding screens with back button and progress indicator.
 */
@Composable
fun OnboardingHeader(
    currentStep: Int,
    totalSteps: Int = 4,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = BloomThemeTokens.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }

        StepProgressBar(
            currentStep = currentStep,
            totalSteps = totalSteps,
            modifier = Modifier.weight(1f)
        )

        Box(modifier = Modifier.size(48.dp))
    }
}

/**
 * Primary Bloom Rose CTA Button matching Frame 26080030.
 */
@Composable
fun BloomPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = BloomThemeTokens.colors
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = Color.White,
            disabledContainerColor = colors.primaryDisabled,
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
