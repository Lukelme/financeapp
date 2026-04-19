package com.financeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.financeapp.data.models.Category
import com.financeapp.data.models.Goal
import com.financeapp.data.models.Transaction
import com.financeapp.data.models.TransactionType

class Converters {
    @TypeConverter fun fromType(type: TransactionType): String = type.name
    @TypeConverter fun toType(value: String): TransactionType = TransactionType.valueOf(value)
}

@Database(
    entities = [Transaction::class, Category::class, Goal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
