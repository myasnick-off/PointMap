package com.example.pointmap.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pointmap.databinding.FragmentListItemBinding
import com.example.pointmap.model.Mark

class MarkListAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<MarkListAdapter.MarkViewHolder>() {

    private val markListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(marks: List<Mark>) {
        markListDiffer.submitList(marks)
    }

    override fun getItemCount(): Int = markListDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        val binding = FragmentListItemBinding.inflate(inflater, parent, false)
        return MarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkViewHolder, position: Int) {
        holder.bind(markListDiffer.currentList[position])
    }

    inner class MarkViewHolder(private val binding: FragmentListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Mark) = with(binding) {
            markName.text = item.name
            markDescription.text = item.description
            markLatVal.text = item.point.latitude.toString()
            markLonVal.text = item.point.longitude.toString()
            editButton.setOnClickListener { itemClickListener.onEditClick(name = item.name, description = item.description) }
            removeButton.setOnClickListener { itemClickListener.onRemoveClick(itemId = item.id) }
            root.setOnClickListener { itemClickListener.onItemClick(item = item) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mark>() {
            override fun areItemsTheSame(oldItem: Mark, newItem: Mark): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Mark, newItem: Mark): Boolean {
                return oldItem == newItem
            }
        }
    }
}