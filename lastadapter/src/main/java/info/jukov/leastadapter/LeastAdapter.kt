/*
 * Copyright (C) 2016 Miguel Ángel Moreno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.jukov.leastadapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

@Suppress("unused")
class LeastAdapter(
    items: List<Any> = emptyList(),
    stableIds: Boolean = false,
    private val diffUtil: Boolean = false
) : RecyclerView.Adapter<LeastAdapter.Holder>() {

    private var viewType = 0

    private val classToViewType = mutableMapOf<Class<*>, Int>()
    private val classToType = mutableMapOf<Class<*>, Type<*, *>>()
    private val viewTypeToType = mutableMapOf<Int, Type<*, *>>()

    private val items = mutableListOf<Any>().apply { addAll(items) }

    init {
        setHasStableIds(stableIds)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<Any>) {
        if (diffUtil) {
            val old = ArrayList(items)
            items.clear()
            items.addAll(newItems)
            DiffUtil.calculateDiff(DiffCallback(old)).dispatchUpdatesTo(this)
        } else {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    fun <M : Any, B: ViewBinding> map(clazz: Class<M>, type: Type<M, B>): LeastAdapter =
        apply {
            require(type._onCreateView != null) { "onCreate not implemented" }
            if (diffUtil) {
                require(type._itemComparison != null && type._contentComparison != null) {
                    error("comparisons should be implemented for using DiffUtil")
                }
            }
            if (hasStableIds()) {
                require(type._getItemId != null) {
                    "StableIds requested, but getItemId for $clazz not implemented"
                }
            }

            classToType[clazz] = type
            classToViewType[clazz] = viewType
            viewTypeToType[viewType] = type
            viewType++
        }

    inline fun <reified M : Any, reified B: ViewBinding> map(
        noinline viewHolder: Type<M, B>.() -> Unit
    ): LeastAdapter = map(
        M::class.java,
        Type<M, B>().apply { viewHolder(this) }
    )

    fun into(recyclerView: RecyclerView) = apply { recyclerView.adapter = this }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): Holder {
        val type = viewTypeToType[viewType] ?: error("No Type for viewType $viewType")
        val onCreate = type._onCreateView ?: error("onCreate for viewType $viewType not implemented")
        val binding = onCreate(view)

        return Holder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = items[position]

        val type = getType(position) as Type<Any, ViewBinding>

        type._onBindView?.invoke(position, model, holder.binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewRecycled(holder: Holder) {
        val position = holder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION && position < items.size) {
            val type = getType(position) as Type<Any, ViewBinding>
            type._onRecycleView?.invoke(holder.binding)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getItemId(position: Int): Long {
        val item = items[position]
        val type = classToType[item.javaClass] as? Type<Any, ViewBinding>
            ?: error("No Type for ${item.javaClass}")
        val getItemId = type._getItemId
            ?: error("getItemId not implemented for ${item.javaClass}")

        return getItemId(item)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val itemClass = items[position].javaClass
        return classToViewType[itemClass] ?: error("No viewType for $itemClass")
    }

    private fun getType(position: Int): Type<*, *> {
        val itemClass = items[position].javaClass
        return classToType[itemClass] ?: error("No Type for $itemClass")
    }

    class Holder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    private inner class DiffCallback(private val old: ArrayList<Any>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = items.size

        @Suppress("UNCHECKED_CAST")
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = items[newItemPosition]

            if (oldItem.javaClass == newItem.javaClass) {
                val type = classToType[oldItem.javaClass] as? Type<Any, ViewBinding> ?: error("No type for ${oldItem.javaClass}")
                val comparison = type._itemComparison ?: error("Item comparison not implemented for ${oldItem.javaClass}")
                return comparison(oldItem, newItem)
            }

            return false
        }

        @Suppress("UNCHECKED_CAST")
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = items[newItemPosition]

            if (oldItem.javaClass == newItem.javaClass) {
                val type = classToType[oldItem.javaClass] as? Type<Any, ViewBinding> ?: error("No type for ${oldItem.javaClass}")
                val comparison = type._contentComparison ?: error("Content comparison not implemented for ${oldItem.javaClass}")
                return comparison(oldItem, newItem)
            }

            return false
        }
    }
}
