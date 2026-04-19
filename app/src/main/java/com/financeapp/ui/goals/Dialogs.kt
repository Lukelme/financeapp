package com.financeapp.ui.goals

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.financeapp.R
import com.financeapp.data.models.Goal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class AddGoalDialog(private val onSave: (Goal) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_goal, null)
        val etName = view.findViewById<TextInputEditText>(R.id.etGoalName)
        val etTarget = view.findViewById<TextInputEditText>(R.id.etGoalTarget)
        val etIcon = view.findViewById<TextInputEditText>(R.id.etGoalIcon)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nova Meta")
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                val name = etName.text.toString().trim()
                val target = etTarget.text.toString().toDoubleOrNull() ?: 0.0
                val icon = etIcon.text.toString().ifEmpty { "🎯" }
                if (name.isNotEmpty() && target > 0) {
                    onSave(Goal(name = name, targetAmount = target, icon = icon, deadline = null))
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}

class ContribuirDialog(private val goal: Goal, private val onContribuir: (Double) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_contribuir, null)
        val etValor = view.findViewById<TextInputEditText>(R.id.etValorContribuicao)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Contribuir para: ${goal.name}")
            .setView(view)
            .setPositiveButton("Confirmar") { _, _ ->
                val valor = etValor.text.toString().toDoubleOrNull() ?: 0.0
                if (valor > 0) onContribuir(valor)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}
