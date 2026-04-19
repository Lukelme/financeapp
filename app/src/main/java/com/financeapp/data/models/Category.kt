package com.financeapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String,       // nome do emoji ou ícone, ex: "🍔"
    val color: String,      // hex, ex: "#FF5722"
    val budgetLimit: Double = 0.0  // 0 = sem limite
)
