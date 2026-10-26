package com.newage.bloom.data

import androidx.compose.ui.graphics.Color

data class GoalItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val targetDescription: String,
    val currentProgress: Int,
    val maxTarget: Int,
    val unit: String,
    val tintColor: Color,
    val lightBgColor: Color,
    val isEnabled: Boolean = true
)

data class PcosFaqItem(
    val id: String,
    val category: String,
    val question: String,
    val answer: String
)

data class DailyTip(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val isDismissed: Boolean = false
)
