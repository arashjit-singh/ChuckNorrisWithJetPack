package com.example.chucknorriswithjetpack.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chucknorriswithjetpack.databinding.ItemCategoryListBinding
import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import timber.log.Timber

class CategoryListAdapter : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {
    private lateinit var binding: ItemCategoryListBinding
    var callback: ItemClick? = null

    val diffCallback = object : DiffUtil.ItemCallback<JokeCategories>() {

        override fun areItemsTheSame(oldItem: JokeCategories, newItem: JokeCategories): Boolean {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JokeCategories, newItem: JokeCategories): Boolean {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(mCatList: List<JokeCategories>) {
        Log.i("TAG", "submitList: ${mCatList}")
        differ.submitList(mCatList)
    }

    class ViewHolder(binding: ItemCategoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            binding.categoryNameTxtVw.text = differ.currentList.get(position).name
            setOnClickListener {
                callback?.onItemClick(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface ItemClick {
        fun onItemClick(jokeCategories: JokeCategories)
    }
}