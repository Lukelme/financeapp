package com.financeapp.ui.reports

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.financeapp.databinding.FragmentReportsBinding
import com.financeapp.data.models.TransactionType
import com.financeapp.viewmodel.FinanceViewModel
import com.financeapp.viewmodel.FinanceViewModelFactory
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private val vm: FinanceViewModel by activityViewModels { FinanceViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportsBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.allTransactions.observe(viewLifecycleOwner) { transactions ->

            // ── Gráfico Pizza: Receitas vs Despesas ──────────────────────────
            val receitas = transactions.filter { it.type == TransactionType.RECEITA }.sumOf { it.amount }.toFloat()
            val despesas = transactions.filter { it.type == TransactionType.DESPESA }.sumOf { it.amount }.toFloat()

            val pieEntries = listOf(
                PieEntry(receitas, "Receitas"),
                PieEntry(despesas, "Despesas")
            )
            val pieDataSet = PieDataSet(pieEntries, "").apply {
                colors = listOf(Color.parseColor("#4CAF50"), Color.parseColor("#F44336"))
                valueTextSize = 14f
                valueTextColor = Color.WHITE
            }
            binding.pieChart.apply {
                data = PieData(pieDataSet)
                description.isEnabled = false
                isDrawHoleEnabled = true
                holeRadius = 40f
                setHoleColor(Color.TRANSPARENT)
                legend.textSize = 13f
                animateY(800)
                invalidate()
            }

            // ── Gráfico Barras: gastos dos últimos 6 meses ───────────────────
            val cal = Calendar.getInstance()
            val monthFmt = SimpleDateFormat("MMM", Locale("pt", "BR"))
            val barEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()

            for (i in 5 downTo 0) {
                cal.time = Date()
                cal.add(Calendar.MONTH, -i)
                val monthStart = cal.apply { set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0) }.timeInMillis
                val monthEnd = cal.apply { set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); set(Calendar.HOUR_OF_DAY, 23) }.timeInMillis

                val total = transactions
                    .filter { it.type == TransactionType.DESPESA && it.date in monthStart..monthEnd }
                    .sumOf { it.amount }.toFloat()

                barEntries.add(BarEntry((5 - i).toFloat(), total))
                labels.add(monthFmt.format(Date(monthStart)))
            }

            val barDataSet = BarDataSet(barEntries, "Despesas por mês").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 10f
            }

            binding.barChart.apply {
                data = BarData(barDataSet)
                xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels)
                xAxis.granularity = 1f
                description.isEnabled = false
                animateY(1000)
                invalidate()
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
