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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.jukov.leastadapter.LeastAdapter
import info.jukov.leastadapter_sample.R
import info.jukov.leastadapter_sample.data.DiffCallback
import info.jukov.leastadapter_sample.data.Model
import info.jukov.leastadapter_sample.databinding.LayoutHeaderBinding
import info.jukov.leastadapter_sample.databinding.LayoutItemBinding
import java.util.Random

@Suppress("unused")
class ListFragment : Fragment(), MenuProvider {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: RecyclerView.Adapter<*>

    private var items = mutableListOf<Model>()

    var itemId = 0

    init {
        items.add(Model.Header(itemId++, "Header 0"))
        items.add(Model.Item(itemId++, "Item 1"))
        items.add(Model.Item(itemId++, "Item 2"))
        items.add(Model.Item(itemId++, "Item 3"))
        items.add(Model.Item(itemId++, "Item 4"))
        items.add(Model.Header(itemId++, "Header 5"))
        items.add(Model.Item(itemId++, "Item 6"))
        items.add(Model.Item(itemId++, "Item 7"))
        items.add(Model.Item(itemId++, "Item 8"))
        items.add(Model.Item(itemId++, "Item 9"))
        items.add(Model.Item(itemId++, "Item 10"))
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

        adapter = LeastAdapter(items)
            .map<Model.Header, LayoutHeaderBinding>(
                viewHolder = {
                    onCreateView { parent ->
                        LayoutHeaderBinding.inflate(layoutInflater, parent, false)
                    }
                    onBindView { _, model, binding ->
                        binding.headerText.text = model.text
                        binding.root.setOnClickListener {
                            toast("Click on ${model.text}")
                        }
                    }
                }
            )
            .map<Model.Item, LayoutItemBinding>(
                viewHolder = {
                    onCreateView { parent ->
                        LayoutItemBinding.inflate(layoutInflater, parent, false)
                    }
                    onBindView { _, model, binding ->
                        binding.text.text = model.text
                        binding.root.setOnClickListener {
                            toast("Click on ${model.text}")
                        }
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
            R.id.addLast -> addItem(items.size)
            R.id.addFirst -> addItem(0)
            R.id.addRandom -> addItem(Random().nextInt(items.size))
            R.id.removeFirst -> removeItem(0)
            R.id.removeLast -> removeItem(items.lastIndex)
            R.id.removeRandom -> removeItem(Random().nextInt(items.lastIndex))
        }
        return true
    }

    private fun addItem(position: Int) {
        val oldItems = ArrayList(items)
        val id = itemId++
        items.add(position, Model.Item(id, "User item $id"))
        DiffUtil.calculateDiff(DiffCallback(oldItems, items))
            .dispatchUpdatesTo(adapter)

        recyclerView.scrollToPosition(position)
    }

    private fun removeItem(position: Int) {
        val oldItems = ArrayList(items)
        items.removeAt(position)
        DiffUtil.calculateDiff(DiffCallback(oldItems, items))
            .dispatchUpdatesTo(adapter)
    }

    private fun toast(text: String) = requireContext().let {
        Toast.makeText(it, text, Toast.LENGTH_SHORT).show()
    }
}
