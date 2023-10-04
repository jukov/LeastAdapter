package info.jukov.leastadapter_sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.jukov.leastadapter.LeastAdapter
import info.jukov.leastadapter.NotifyChange
import info.jukov.leastadapter_sample.R
import info.jukov.leastadapter_sample.data.Model
import info.jukov.leastadapter_sample.databinding.LayoutHeaderBinding
import info.jukov.leastadapter_sample.databinding.LayoutItemBinding
import java.lang.Integer.min
import java.util.Random

@Suppress("unused")
class ListFragment : Fragment(), MenuProvider {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: LeastAdapter

    private var items = mutableListOf<Model>()

    var itemId = 0

    init {
        items.add(Model.Header(itemId++, "Header $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Header(itemId++, "Header $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
        items.add(Model.Item(itemId++, "Item $itemId"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = LeastAdapter(
            items,
            stableIds = true,
            notifyChange = NotifyChange.DIFF_UTIL
        )
            .map<Model.Header, LayoutHeaderBinding>(
                viewHolder = {
                    onBindView { model, binding ->
                        binding.headerText.text = model.text
                        binding.root.setOnClickListener {
                            toast("Click on ${model.text}")
                        }
                    }
                    getItemId { model ->
                        model.id.toLong()
                    }
                }
            )
            .map<Model.Item, LayoutItemBinding>(
                viewHolder = {
                    onBindView { model, binding ->
                        binding.text.text = model.text
                        binding.root.setOnClickListener {
                            toast("Click on ${model.text}")
                        }
                    }
                    getItemId { model ->
                        model.id.toLong()
                    }
                    itemComparison { old, new ->
                        old.id == new.id
                    }
                    contentComparison { old, new ->
                        old == new
                    }
                }
            )
            .into(recyclerView)

        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.change_data -> changeData()
        }
        return true
    }

    private fun changeData() {
        val random = Random()
        for (i in items.indices) {
            when (random.nextInt(3)) {
                0 -> items.removeAt(min(i, items.lastIndex))
                1 -> items.add(Model.Item(itemId++, "Item $itemId"))
                else -> Unit
            }
        }
        adapter.setItems(items)
    }

    private fun toast(text: String) = requireContext().let {
        Toast.makeText(it, text, Toast.LENGTH_SHORT).show()
    }
}
