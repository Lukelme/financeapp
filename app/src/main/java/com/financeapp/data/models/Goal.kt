package com.financeapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: Long?,        // timestamp ou null = sem prazo
    val icon: String = "🎯",
    val color: String = "#4CAF50"
) {
    val progress: Float get() = if (targetAmount > 0) (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f
    val isCompleted: Boolean get() = currentAmount >= targetAmount
}
