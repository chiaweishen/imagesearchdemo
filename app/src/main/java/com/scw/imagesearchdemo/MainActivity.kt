package com.scw.imagesearchdemo

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.imagesearchdemo.databinding.ActivityMainBinding
import com.scw.imagesearchdemo.ext.dp
import com.scw.imagesearchdemo.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var adapter: MainPagingAdapter
    private var gridLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    private fun initView() {
        setSupportActionBar(viewBinding.toolbar)
        adapter = MainPagingAdapter()
        viewBinding.viewRecycler.adapter = adapter
        viewBinding.viewRecycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(16.dp(), 0, 16.dp(), 0)
            }
        })
        swapLayout()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                search(searchView.query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    adapter.submitData(lifecycle, PagingData.empty())
                }
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_layout -> {
                swapLayout()
            }
        }
        return true
    }

    private fun swapLayout() {
        if (gridLayout) {
            gridLayout = false
            viewBinding.viewRecycler.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        } else {
            gridLayout = true
            viewBinding.viewRecycler.layoutManager =
                GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun search(query: String) {
        lifecycleScope.launchWhenStarted {
            viewModel.loadImages(query)
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}