package com.isidroid.b21.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.ui.home.adapter.Item
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.AppBarListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView, AppBarListener, Adapter.Listener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private val adapter = Adapter(this) {
        viewModel.loadNext()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun createToolbar(toolbar: MaterialToolbar, navController: NavController) {
        super.createToolbar(toolbar, navController)
        toolbar.visible(true)
        toolbar.title = "Hello Sample World"
    }

    override fun createAdapter() {
        binding.recyclerView.adapter = adapter
//        val list = (0..1).map { Item(id = it, name = UUID.randomUUID().toString().take(5), createdAt = Date()) }
//        adapter.insert(list)
    }

    override fun createForm() {
        with(binding) {
            buttonUpdateAll.setOnClickListener { updateAll() }
            buttonUpdatePartiallyAll.setOnClickListener { updatePartially() }
            buttonAdd.setOnClickListener { add() }
            buttonAdd2.setOnClickListener { addMulti() }
            buttonAddFew.setOnClickListener { addFew() }
            buttonLoading.setOnClickListener { loadData() }
        }
    }

    override suspend fun onCreateViewModel() {
        viewModel.viewState.collect { state ->
            when (state) {
                is UiState.Data -> onData(state.items, state.hasMore)
                else -> {}
            }
        }
    }

    private fun onData(items: List<Item>, hasMore: Boolean) {
        adapter.insert(list = items, hasMore = hasMore)
    }

    private fun loadData() {
        adapter.reset(hasEmpty = false)
        adapter.insert(emptyList(), hasMore = true)

        Timber.i("sdfsdfsdf items=${adapter.list.size}, itemCount=${adapter.itemCount}, hasMore=${adapter.hasMore}")
    }

    private fun addFew() {
        val list = (0..1).map { Item(id = kotlin.random.Random.nextInt(200, 300), name = "multi $it", createdAt = Date()) }
        adapter.add(list)
    }

    private fun addMulti() {
        val list = (0..1).map { Item(id = kotlin.random.Random.nextInt(200, 300), name = "multi $it", createdAt = Date()) }
        adapter.insert(list)
    }

    private fun add() {
        val item = Item(id = kotlin.random.Random.nextInt(10, 100), name = "hi", createdAt = Date())
        adapter.add(item)
    }

    private fun updatePartially() {
        val items = adapter.list.mapIndexed { index, item ->
            item.copy(name = "updatePartially ${kotlin.random.Random.nextInt(100)}", createdAt = Date())
        }.toMutableList()

        if (!items.any { it.id == 0 })
            items.add(Item(id = 0, name = "sda", createdAt = Date()))

        adapter.update(items,
            listContentsComparison = { _, _, _, _ -> false },
            listComparisonPayload = { _, _, _, _ -> "hello" }
        )
    }

    private fun updateAll() {
        val items = adapter.list.map { it.copy(name = "updateAll", createdAt = Date()) }
        adapter.clear().insert(items)
    }

    override fun updateTime(item: Item) {
        item.createdAt = Date()
        adapter.update(item, listOf(item.createdAt, "update time"))
    }

    override fun updateComplete(item: Item) {
        val newItem = item.copy(name = "Sasha ${item.id}")
        adapter.update(newItem)
    }

    override fun delete(item: Item) {
        adapter.remove(item)
    }
}
