package com.financeapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.financeapp.data.models.Transaction
import com.financeapp.data.models.Category
import com.financeapp.data.models.Goal
import com.financeapp.data.models.TransactionType

// ─── Transaction DAO ───────────────────────────────────────────────────────────
@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getByPeriod(start: Long, end: Long): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'RECEITA'")
    fun totalReceitas(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'DESPESA'")
    fun totalDespesas(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'DESPESA' AND categoryId = :categoryId AND date BETWEEN :start AND :end")
    suspend fun totalDespesasByCategory(categoryId: Int, start: Long, end: Long): Double?

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getByCategory(categoryId: Int): LiveData<List<Transaction>>
}

// ─── Category DAO ─────────────────────────────────────────────────────────────
@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAll(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Int): Category?
}

// ─── Goal DAO ─────────────────────────────────────────────────────────────────
@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: Goal)

    @Update
    suspend fun update(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("SELECT * FROM goals ORDER BY deadline ASC")
    fun getAll(): LiveData<List<Goal>>

    @Query("UPDATE goals SET currentAmount = currentAmount + :value WHERE id = :goalId")
    suspend fun addAmount(goalId: Int, value: Double)
}
