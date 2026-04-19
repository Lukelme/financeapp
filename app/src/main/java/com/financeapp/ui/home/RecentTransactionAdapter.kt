package com.financeapp.ui.home

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.financeapp.R
import com.financeapp.data.models.Transaction
import com.financeapp.data.models.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class RecentTransactionAdapter(private val items: List<Transaction>) :
    RecyclerView.Adapter<RecentTransactionAdapter.VH>() {

    private val fmt = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvDate.text = dateFmt.format(Date(item.date))
        val color = if (item.type == TransactionType.RECEITA)
            holder.itemView.context.getColor(R.color.green)
        else
            holder.itemView.context.getColor(R.color.red)
        val prefix = if (item.type == TransactionType.RECEITA) "+ " else "- "
        holder.tvAmount.text = prefix + fmt.format(item.amount)
        holder.tvAmount.setTextColor(color)
    }
}
