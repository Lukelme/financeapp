package com.financeapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.financeapp.data.models.*
import com.financeapp.data.repository.FinanceRepository
import kotlinx.coroutines.launch

class FinanceViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = FinanceRepository(app)

    // ── LiveData expostos para a UI ────────────────────────────────────────────
    val allTransactions = repo.allTransactions
    val totalReceitas   = repo.totalReceitas
    val totalDespesas   = repo.totalDespesas
    val allCategories   = repo.allCategories
    val allGoals        = repo.allGoals

    // ── Saldo calculado ───────────────────────────────────────────────────────
    val saldo: LiveData<Double> = MediatorLiveData<Double>().apply {
        fun recalc() {
            val r = totalReceitas.value ?: 0.0
            val d = totalDespesas.value ?: 0.0
            value = r - d
        }
        addSource(totalReceitas) { recalc() }
        addSource(totalDespesas) { recalc() }
    }

    // ── Transações ────────────────────────────────────────────────────────────
    fun addTransaction(t: Transaction) = viewModelScope.launch { repo.insertTransaction(t) }
    fun updateTransaction(t: Transaction) = viewModelScope.launch { repo.updateTransaction(t) }
    fun deleteTransaction(t: Transaction) = viewModelScope.launch { repo.deleteTransaction(t) }
    fun getByPeriod(start: Long, end: Long) = repo.getTransactionsByPeriod(start, end)

    // ── Categorias ────────────────────────────────────────────────────────────
    fun addCategory(c: Category) = viewModelScope.launch { repo.insertCategory(c) }
    fun updateCategory(c: Category) = viewModelScope.launch { repo.updateCategory(c) }
    fun deleteCategory(c: Category) = viewModelScope.launch { repo.deleteCategory(c) }

    // ── Metas ─────────────────────────────────────────────────────────────────
    fun addGoal(g: Goal) = viewModelScope.launch { repo.insertGoal(g) }
    fun updateGoal(g: Goal) = viewModelScope.launch { repo.updateGoal(g) }
    fun deleteGoal(g: Goal) = viewModelScope.launch { repo.deleteGoal(g) }
    fun contribuirMeta(goalId: Int, valor: Double) = viewModelScope.launch {
        repo.addAmountToGoal(goalId, valor)
    }

    // ── Verificar limite de categoria (para notificações) ─────────────────────
    fun verificarLimites(start: Long, end: Long) = viewModelScope.launch {
        allCategories.value?.forEach { cat ->
            if (cat.budgetLimit > 0) {
                val gasto = repo.totalDespesasByCategory(cat.id, start, end) ?: 0.0
                if (gasto >= cat.budgetLimit * 0.8) {
                    _limiteAlerta.postValue(Pair(cat, gasto))
                }
            }
        }
    }

    private val _limiteAlerta = MutableLiveData<Pair<Category, Double>>()
    val limiteAlerta: LiveData<Pair<Category, Double>> = _limiteAlerta
}

class FinanceViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java))
            @Suppress("UNCHECKED_CAST") return FinanceViewModel(app) as T
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
