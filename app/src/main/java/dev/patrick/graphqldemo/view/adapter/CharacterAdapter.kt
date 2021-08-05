package dev.patrick.graphqldemo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.patrick.graphqldemo.CharactersListQuery
import dev.patrick.graphqldemo.databinding.ItemCharacterBinding

typealias CharacterListener = (CharactersListQuery.Result) -> Unit

class CharacterAdapter :
    ListAdapter<CharactersListQuery.Result, CharacterAdapter.CharacterViewHolder>(
        CharacterDiffUtil
    ) {

    private var onItemClicked: CharacterListener? = null

    fun setOnItemClicked(listener: CharacterListener) {
        onItemClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        onItemClicked?.let { holder.bind(getItem(position), it) }
    }

    class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharactersListQuery.Result, listener: CharacterListener) {
            binding.root.setOnClickListener {
                listener.invoke(item)
            }
            binding.character = item
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): CharacterViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return CharacterViewHolder(ItemCharacterBinding.inflate(inflater, parent, false))
            }
        }
    }

    companion object {
        private val CharacterDiffUtil =
            object : DiffUtil.ItemCallback<CharactersListQuery.Result>() {
                override fun areItemsTheSame(
                    oldItem: CharactersListQuery.Result,
                    newItem: CharactersListQuery.Result
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CharactersListQuery.Result,
                    newItem: CharactersListQuery.Result
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    load(url) { crossfade(true) }
}