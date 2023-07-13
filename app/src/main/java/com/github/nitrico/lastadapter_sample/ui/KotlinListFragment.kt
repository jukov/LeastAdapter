package com.github.nitrico.lastadapter_sample.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import com.github.nitrico.lastadapter_sample.data.Car
import com.github.nitrico.lastadapter_sample.data.Data
import com.github.nitrico.lastadapter_sample.data.Header
import com.github.nitrico.lastadapter_sample.data.Person
import com.github.nitrico.lastadapter_sample.data.Point
import com.github.nitrico.lastadapter_sample.data.StableData
import info.jukov.leastadapter.BR
import info.jukov.leastadapter_sample.R
import info.jukov.leastadapter_sample.databinding.ItemCarBinding
import info.jukov.leastadapter_sample.databinding.ItemHeaderBinding
import info.jukov.leastadapter_sample.databinding.ItemHeaderFirstBinding
import info.jukov.leastadapter_sample.databinding.ItemPersonBinding
import info.jukov.leastadapter_sample.databinding.ItemPointBinding

@Suppress("unused")
class KotlinListFragment : Fragment() {

    private lateinit var list: RecyclerView

    private val typeHeader = Type<ItemHeaderBinding>(R.layout.item_header)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }

    private val typeHeaderFirst = Type<ItemHeaderFirstBinding>(R.layout.item_header_first)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }

    private val typePoint = Type<ItemPointBinding>(R.layout.item_point)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }

    private val typeCar = Type<ItemCarBinding>(R.layout.item_car)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }

    private val typePerson = Type<ItemPersonBinding>(R.layout.item_person)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = view.findViewById(R.id.list)
        list.layoutManager = LinearLayoutManager(activity)

        val items = Data.items
        val stableIds = items == StableData.items

        setTypeHandlerAdapter(items, stableIds)
    }

    private fun setMapAdapter(items: List<Any>, stableIds: Boolean) {
        LastAdapter(items, BR.item, stableIds)
                .map<Person>(R.layout.item_person)
                .map<Car>(R.layout.item_car)
                .map<Header>(R.layout.item_header)
                .map<Point>(R.layout.item_point)
                .into(list)
    }

    private fun setMapAdapterWithListeners(items: List<Any>, stableIds: Boolean) {
        list.adapter = LastAdapter(items, BR.item, stableIds)
                .map<Header, ItemHeaderBinding>(R.layout.item_header)
                .map<Point>(typePoint)
                .map<Car>(Type<ItemCarBinding>(R.layout.item_car)
                        .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
                        .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
                        .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
                        .onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
                        .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }
                )
                .map<Person, ItemPersonBinding>(R.layout.item_person) {
                    onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
                    onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
                    onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
                    onClick { activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}") }
                    onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }
                }
                .into(list)
    }

    private fun setLayoutHandlerAdapter(items: List<Any>, stableIds: Boolean) {
        LastAdapter(items, BR.item, stableIds).layout { item, position ->
            when (item) {
                is Header -> if (position == 0) R.layout.item_header_first else R.layout.item_header
                is Person -> R.layout.item_person
                is Point -> R.layout.item_point
                is Car -> R.layout.item_car
                else -> -1
            }
        }.into(list)
    }

    private fun setTypeHandlerAdapter(items: List<Any>, stableIds: Boolean) {
        LastAdapter(items, BR.item, stableIds).type { item, position ->
            when (item) {
                is Header -> if (position == 0) typeHeaderFirst else typeHeader
                is Point -> typePoint
                is Person -> typePerson
                is Car -> typeCar
                else -> null
            }
        }.into(list)
    }

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }

}
