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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

@Suppress("unused", "PropertyName")
open class Type<Item : Any, Binding : ViewBinding>(
    private val vbClass: Class<Binding>
) {
    internal var _onCreateView: ((parent: ViewGroup) -> Binding)? = { parent ->
        createBindingInstance(LayoutInflater.from(parent.context), parent)
    }
    internal var _onBindView: ((holder: LeastAdapter.Holder<Item, Binding>, position: Int) -> Unit)? = null; private set
    internal var _onRecycleView: ((binding: Binding) -> Unit)? = null; private set
    internal var _getItemId: ((item: Item) -> Long)? = null; private set
    internal var _itemComparison: ((old: Item, new: Item) -> Boolean)? = null; private set
    internal var _contentComparison: ((old: Item, new: Item) -> Boolean)? = null; private set

    @Suppress("UNCHECKED_CAST")
    private fun createBindingInstance(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Binding {
        val method = vbClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)

        // Call Binding.inflate(inflater, container, false) Java static method
        return method.invoke(null, inflater, container, false) as Binding
    }

    /**
     * @param action should bind concrete item to Holder
     * */
    fun onBindViewHolder(action: (item: Item, holder: LeastAdapter.Holder<Item, Binding>, position: Int) -> Unit) {
        if (_onBindView != null) error("Bind can be performed only once per mapper")
        _onBindView = { holder, position ->
            action(requireNotNull(holder.item), holder, position)
        }
    }

    /**
     * @param action should bind concrete item to Holder
     * */
    fun onBindViewHolder(action: (item: Item, holder: LeastAdapter.Holder<Item, Binding>) -> Unit) {
        if (_onBindView != null) error("Bind can be performed only once per mapper")
        _onBindView = { holder, _ ->
            action(requireNotNull(holder.item), holder)
        }
    }

    /**
     * @param action should bind concrete item to ViewBinding
     * */
    fun onBindView(action: (item: Item, binding: Binding, position: Int) -> Unit) {
        if (_onBindView != null) error("Bind can be performed only once per mapper")
        _onBindView = { holder, position ->
            action(requireNotNull(holder.item), holder.binding, position)
        }
    }

    /**
     * @param action should bind concrete item to ViewBinding
     * */
    fun onBindView(action: (item: Item, binding: Binding) -> Unit) {
        if (_onBindView != null) error("Bind can be performed only once per mapper")
        _onBindView = { holder, _ ->
            action(requireNotNull(holder.item), holder.binding)
        }
    }

    /**
     * @param action optional action on view recycling
     * */
    fun onRecycleView(action: (binding: Binding) -> Unit) {
        _onRecycleView = action
    }

    /**
     * @param action should provide stable item id if stableIds requested in [LeastAdapter] constructor
     * */
    fun getItemId(action: (item: Item) -> Long) {
        _getItemId = action
    }

    /**
     * @param action similar to [androidx.recyclerview.widget.DiffUtil.Callback.areItemsTheSame]
     * should return true if items the same.
     * */
    fun itemComparison(action: (old: Item, new: Item) -> Boolean) {
        _itemComparison = action
    }

    /**
     * @param action similar to [androidx.recyclerview.widget.DiffUtil.Callback.areContentsTheSame]
     * should return true if content of items the same.
     * */
    fun contentComparison(action: (old: Item, new: Item) -> Boolean) {
        _contentComparison = action
    }
}