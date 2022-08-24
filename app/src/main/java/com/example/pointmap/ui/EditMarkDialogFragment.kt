package com.example.pointmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.pointmap.databinding.FragmentDialogEditMarkBinding
import com.example.pointmap.model.AppState
import com.example.pointmap.model.Mark
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditMarkDialogFragment : DialogFragment() {

    private val viewModel: MainViewModel by sharedViewModel()

    private var _binding: FragmentDialogEditMarkBinding? = null
    private val binding: FragmentDialogEditMarkBinding get() = _binding!!

    private val markId: Long by lazy {
        arguments?.getLong(ARG_MARK_ID, DEFAULT_MARK_ID) ?: DEFAULT_MARK_ID
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogEditMarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        btnApply.setOnClickListener {  }
        btnCancel.setOnClickListener { dismiss() }
    }

    private fun initViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner) { renderState(it) }
    }

    private fun renderState(state: AppState) {
        when (state) {
            is AppState.Data -> showData(mark = state.data.first { it.id == markId })
            else -> {}
        }
    }

    private fun showData(mark: Mark) {
        binding.nameField.text?.let { it.replace(0, it.length, mark.name) }
        if (mark.description.isNotEmpty()) {
            binding.descriptionField.text?.let { it.replace(0, it.length, mark.description) }
        }
    }

    companion object {
        private const val ARG_MARK_ID = "arg_mark_id"
        private const val DEFAULT_MARK_ID = -1L

        fun newInstance(markId: Long): EditMarkDialogFragment =
            EditMarkDialogFragment().apply {
                arguments = bundleOf(ARG_MARK_ID to markId)
            }
    }
}