package com.example.pointmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.pointmap.databinding.FragmentListBinding
import com.example.pointmap.model.AppState
import com.example.pointmap.model.Mark
import com.yandex.mapkit.geometry.Point
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListFragment : Fragment(), ItemClickListener {

    private val viewModel: MainViewModel by sharedViewModel()

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private val adapter: MarkListAdapter = MarkListAdapter(itemClickListener = this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        markList.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner) { renderState(it) }
    }

    private fun renderState(state: AppState) {
        when (state) {
            is AppState.Loading -> showLoading()
            is AppState.Data -> showData(data = state.data)
            is AppState.Error -> showError(message = state.message)
        }
    }

    private fun showLoading() {
        binding.loader.isVisible = true
    }

    private fun showData(data: List<Mark>) {
        binding.loader.isVisible = false
        binding.emptyListText.isVisible = data.isEmpty()
        adapter.submitList(marks = data)
    }

    private fun showError(message: String) {
        binding.loader.isVisible = false
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSearchDialog(markId: Long) {
        EditMarkDialogFragment.newInstance(markId = markId).show(parentFragmentManager, "")
    }

    override fun onEditClick(itemId: Long) {
        showSearchDialog(markId = itemId)
    }

    override fun onRemoveClick(itemId: Long) {
        viewModel.removeMark(id = itemId)
    }

    override fun onItemClick(point: Point) {
        viewModel.moveToSelectedMark(point = point)
        requireActivity().onBackPressed()
    }

    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }
}