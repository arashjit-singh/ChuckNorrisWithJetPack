package com.example.chucknorriswithjetpack.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chucknorriswithjetpack.databinding.ItemJokeListBinding
import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel

class JokeListAdapter : RecyclerView.Adapter<JokeListAdapter.ViewHolder>() {
    private lateinit var binding: ItemJokeListBinding

    val diffCallback = object : DiffUtil.ItemCallback<RandomJokeModel>() {

        override fun areItemsTheSame(oldItem: RandomJokeModel, newItem: RandomJokeModel): Boolean {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            return oldItem.jokeId == newItem.jokeId
        }

        override fun areContentsTheSame(
            oldItem: RandomJokeModel,
            newItem: RandomJokeModel
        ): Boolean {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(mCatList: List<RandomJokeModel>) {
        Log.i("TAG", "submitList: ${mCatList}")
        differ.submitList(mCatList)
    }

    class ViewHolder(binding: ItemJokeListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemJokeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            binding.categoryNameTxtVw.text = differ.currentList.get(position).joke
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}