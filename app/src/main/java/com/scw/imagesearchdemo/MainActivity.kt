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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.scw.imagesearchdemo.databinding.ActivityMainBinding
import com.scw.imagesearchdemo.ext.dp
import com.scw.imagesearchdemo.ui.adapter.MainPagingAdapter
import com.scw.imagesearchdemo.ui.adapter.RecentAdapter
import com.scw.imagesearchdemo.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var pagingAdapter: MainPagingAdapter
    private lateinit var recentAdapter: RecentAdapter
    private var searchView: SearchView? = null
    private var gridLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
        initLayoutStyle()
        observeData()
        viewModel.loadRecentData()
    }

    private fun initLayoutStyle() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val layoutStyle = remoteConfig.getDouble("layoutStyle")
                    gridLayout = layoutStyle.toInt() == 1
                    updateLayoutStyle()
                }
            }
    }

    private fun initView() {
        setSupportActionBar(viewBinding.toolbar)
        pagingAdapter = MainPagingAdapter()
        viewBinding.viewRecycler.adapter = pagingAdapter
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
        updateLayoutStyle()

        recentAdapter = RecentAdapter()
        viewBinding.viewRecent.adapter = recentAdapter
        viewBinding.viewRecent.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recentAdapter.setListener(object : RecentAdapter.AdapterListener {
            override fun onItemClickListener(text: String) {
                searchView?.setQuery(text, true)
            }
        })
    }

    private fun observeData() {
        viewModel.recentLiveData.observe(this) {
            recentAdapter.setData(it)
        }

        viewModel.imageLiveData.observe(this) {
            lifecycleScope.launch {
                pagingAdapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        searchView = menu.findItem(R.id.action_search).actionView as? SearchView
        searchView?.isIconified = false
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView?.clearFocus()
                hideKeyboard()
                search(searchView?.query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                Timber.i("onQueryTextChange: $newText")
//                if (newText.isNullOrEmpty()) {
//                    pagingAdapter.submitData(lifecycle, PagingData.empty())
//                }
                return true
            }

        })
        searchView?.setOnQueryTextFocusChangeListener { v, hasFocus ->
            Timber.i("onSearchViewFocusChange: $hasFocus")
            viewBinding.viewRecent.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_layout -> {
                swapImageLayout()
            }
        }
        return true
    }

    private fun swapImageLayout() {
        gridLayout = !gridLayout
        updateLayoutStyle()
    }

    private fun updateLayoutStyle() {
        if (gridLayout) {
            viewBinding.viewRecycler.layoutManager =
                GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        } else {
            viewBinding.viewRecycler.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun search(query: String) {
        viewModel.search(query)
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