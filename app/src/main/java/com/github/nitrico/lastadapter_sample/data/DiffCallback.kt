package com.github.nitrico.lastadapter_sample.data

import androidx.recyclerview.widget.DiffUtil

class DiffCallback(
    private val oldList: List<Model>,
    private val newList: List<Model>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        if (old is Model.Item && new is Model.Item) {
            return old.id == new.id
        }

        if (old is Model.Header && new is Model.Header) {
            return old.id == new.id
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}