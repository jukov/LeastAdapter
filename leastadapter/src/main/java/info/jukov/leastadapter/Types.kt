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

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

@Suppress("unused", "PropertyName")
open class Type<Item : Any, Binding : ViewBinding>() {
    internal var _onCreateView: ((parent: ViewGroup) -> Binding)? = null; private set
    internal var _onBindView: ((position: Int, item: Item, binding: Binding) -> Unit)? = null; private set
    internal var _onRecycleView: ((binding: Binding) -> Unit)? = null; private set
    internal var _getItemId: ((item: Item) -> Long)? = null; private set
    internal var _itemComparison: ((old: Item, new: Item) -> Boolean)? = null; private set
    internal var _contentComparison: ((old: Item, new: Item) -> Boolean)? = null; private set

    /**
     * @param action should provide ViewBinding for concrete item
     * */
    fun onCreateView(action: (parent: ViewGroup) -> Binding) {
        _onCreateView = action
    }

    /**
     * @param action should bind concrete item to ViewBinding
     * */
    fun onBindView(action: (position: Int, item: Item, binding: Binding) -> Unit) {
        _onBindView = action
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