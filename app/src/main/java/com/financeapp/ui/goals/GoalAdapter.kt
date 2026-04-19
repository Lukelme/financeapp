package com.financeapp.ui.goals

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financeapp.R
import com.financeapp.data.models.Goal
import java.text.NumberFormat
import java.util.Locale

class GoalAdapter(
    private val onContribuir: (Goal) -> Unit,
    private val onDelete: (Goal) -> Unit
) : ListAdapter<Goal, GoalAdapter.VH>(DIFF) {

    private val fmt = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvGoalIcon)
        val tvName: TextView = view.findViewById(R.id.tvGoalName)
        val tvProgress: TextView = view.findViewById(R.id.tvGoalProgress)
        val progressBar: ProgressBar = view.findViewById(R.id.progressGoal)
        val btnContribuir: Button = view.findViewById(R.id.btnContribuir)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteGoal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val goal = getItem(position)
        holder.tvIcon.text = goal.icon
        holder.tvName.text = goal.name
        holder.tvProgress.text = "${fmt.format(goal.currentAmount)} / ${fmt.format(goal.targetAmount)}"
        holder.progressBar.progress = (goal.progress * 100).toInt()
        holder.btnContribuir.setOnClickListener { onContribuir(goal) }
        holder.btnDelete.setOnClickListener { onDelete(goal) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Goal>() {
            override fun areItemsTheSame(a: Goal, b: Goal) = a.id == b.id
            override fun areContentsTheSame(a: Goal, b: Goal) = a == b
        }
    }
}
