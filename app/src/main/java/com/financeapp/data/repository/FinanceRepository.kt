package com.financeapp.data.repository

import android.content.Context
import com.financeapp.data.database.AppDatabase
import com.financeapp.data.models.Category
import com.financeapp.data.models.Goal
import com.financeapp.data.models.Transaction

class FinanceRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val txDao = db.transactionDao()
    private val catDao = db.categoryDao()
    private val goalDao = db.goalDao()

    // Transactions
    val allTransactions = txDao.getAll()
    val totalReceitas = txDao.totalReceitas()
    val totalDespesas = txDao.totalDespesas()

    suspend fun insertTransaction(t: Transaction) = txDao.insert(t)
    suspend fun updateTransaction(t: Transaction) = txDao.update(t)
    suspend fun deleteTransaction(t: Transaction) = txDao.delete(t)
    fun getTransactionsByPeriod(start: Long, end: Long) = txDao.getByPeriod(start, end)
    suspend fun totalDespesasByCategory(catId: Int, start: Long, end: Long) =
        txDao.totalDespesasByCategory(catId, start, end)

    // Categories
    val allCategories = catDao.getAll()
    suspend fun insertCategory(c: Category) = catDao.insert(c)
    suspend fun updateCategory(c: Category) = catDao.update(c)
    suspend fun deleteCategory(c: Category) = catDao.delete(c)
    suspend fun getCategoryById(id: Int) = catDao.getById(id)

    // Goals
    val allGoals = goalDao.getAll()
    suspend fun insertGoal(g: Goal) = goalDao.insert(g)
    suspend fun updateGoal(g: Goal) = goalDao.update(g)
    suspend fun deleteGoal(g: Goal) = goalDao.delete(g)
    suspend fun addAmountToGoal(goalId: Int, value: Double) = goalDao.addAmount(goalId, value)
}
