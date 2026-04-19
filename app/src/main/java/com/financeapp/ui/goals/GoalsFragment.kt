package com.financeapp.ui.goals

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.financeapp.databinding.FragmentGoalsBinding
import com.financeapp.viewmodel.FinanceViewModel
import com.financeapp.viewmodel.FinanceViewModelFactory

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    private val vm: FinanceViewModel by activityViewModels { FinanceViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentGoalsBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GoalAdapter(
            onContribuir = { goal ->
                ContribuirDialog(goal) { valor ->
                    vm.contribuirMeta(goal.id, valor)
                }.show(parentFragmentManager, "contribuir")
            },
            onDelete = { vm.deleteGoal(it) }
        )

        binding.rvGoals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGoals.adapter = adapter

        vm.allGoals.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.fabAddGoal.setOnClickListener {
            AddGoalDialog { goal -> vm.addGoal(goal) }
                .show(parentFragmentManager, "addGoal")
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
