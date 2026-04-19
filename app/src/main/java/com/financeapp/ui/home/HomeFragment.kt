package com.financeapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.financeapp.R
import com.financeapp.databinding.FragmentHomeBinding
import com.financeapp.viewmodel.FinanceViewModel
import com.financeapp.viewmodel.FinanceViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val vm: FinanceViewModel by activityViewModels {
        FinanceViewModelFactory(requireActivity().application)
    }

    private val fmt = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa saldo, receitas e despesas
        vm.saldo.observe(viewLifecycleOwner) {
            binding.tvSaldo.text = fmt.format(it ?: 0.0)
            binding.tvSaldo.setTextColor(
                resources.getColor(if ((it ?: 0.0) >= 0) R.color.green else R.color.red, null)
            )
        }
        vm.totalReceitas.observe(viewLifecycleOwner) {
            binding.tvReceitas.text = fmt.format(it ?: 0.0)
        }
        vm.totalDespesas.observe(viewLifecycleOwner) {
            binding.tvDespesas.text = fmt.format(it ?: 0.0)
        }

        // Últimas transações (3)
        vm.allTransactions.observe(viewLifecycleOwner) { list ->
            val adapter = RecentTransactionAdapter(list.take(3))
            binding.rvRecentes.adapter = adapter
        }

        // Botão adicionar transação
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addTransaction)
        }

        // Ver todas as transações
        binding.btnVerTodas.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_transactions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
