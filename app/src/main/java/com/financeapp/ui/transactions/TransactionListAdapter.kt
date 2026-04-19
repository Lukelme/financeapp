package com.financeapp.ui.transactions

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financeapp.R
import com.financeapp.data.models.Transaction
import com.financeapp.data.models.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionListAdapter(
    private val onDelete: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionListAdapter.VH>(DIFF) {

    private val fmt = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView  = view.findViewById(R.id.tvTitle)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDate: TextView   = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.tvTitle.text  = item.title
        holder.tvDate.text   = dateFmt.format(Date(item.date))
        val isReceita = item.type == TransactionType.RECEITA
        val color = if (isReceita)
            holder.itemView.context.getColor(R.color.green)
        else
            holder.itemView.context.getColor(R.color.red)
        holder.tvAmount.text = (if (isReceita) "+ " else "- ") + fmt.format(item.amount)
        holder.tvAmount.setTextColor(color)

        // Segura para deletar
        holder.itemView.setOnLongClickListener {
            onDelete(item)
            true
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(a: Transaction, b: Transaction) = a.id == b.id
            override fun areContentsTheSame(a: Transaction, b: Transaction) = a == b
        }
    }
}
