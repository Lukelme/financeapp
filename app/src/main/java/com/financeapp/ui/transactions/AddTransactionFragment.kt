package com.financeapp.ui.transactions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.financeapp.data.models.Transaction
import com.financeapp.data.models.TransactionType
import com.financeapp.databinding.FragmentAddTransactionBinding
import com.financeapp.viewmodel.FinanceViewModel
import com.financeapp.viewmodel.FinanceViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val vm: FinanceViewModel by activityViewModels { FinanceViewModelFactory(requireActivity().application) }

    private var selectedDate = System.currentTimeMillis()
    private var categoryList = listOf<com.financeapp.data.models.Category>()
    private val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAddTransactionBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDate.text = dateFmt.format(Date(selectedDate))

        // Spinner de categorias
        vm.allCategories.observe(viewLifecycleOwner) { cats ->
            categoryList = cats
            val names = cats.map { "${it.icon} ${it.name}" }
            binding.spinnerCategory.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
                .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        }

        // Seletor de data
        binding.btnDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                cal.set(y, m, d)
                selectedDate = cal.timeInMillis
                binding.tvDate.text = dateFmt.format(Date(selectedDate))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Salvar
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val amountStr = binding.etAmount.text.toString().trim()

            if (title.isEmpty() || amountStr.isEmpty()) {
                Snackbar.make(binding.root, "Preencha todos os campos!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Snackbar.make(binding.root, "Valor inválido!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = if (binding.rgType.checkedRadioButtonId == binding.rbReceita.id)
                TransactionType.RECEITA else TransactionType.DESPESA

            val catId = if (categoryList.isNotEmpty()) categoryList[binding.spinnerCategory.selectedItemPosition].id else null

            vm.addTransaction(Transaction(
                title = title,
                amount = amount,
                type = type,
                categoryId = catId,
                date = selectedDate,
                note = binding.etNote.text.toString()
            ))

            findNavController().popBackStack()
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
