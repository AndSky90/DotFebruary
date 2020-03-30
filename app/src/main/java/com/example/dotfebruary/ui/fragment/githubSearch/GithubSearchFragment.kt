package com.example.dotfebruary.ui.fragment.githubSearch

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotfebruary.R
import com.example.dotfebruary.common.AppSettings
import com.example.dotfebruary.common.AppSettings.LOG_TAG
import com.example.dotfebruary.model.RequestState.*
import com.example.dotfebruary.ui.fragment.githubSearch.adapter.UserPagedListAdapter
import com.example.dotfebruary.ui.fragment.githubUser.GithubUserFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_github_search.*
import java.util.concurrent.TimeUnit


class GithubSearchFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private val viewModel: GithubSearchViewModel by viewModels()
    private var searchView: SearchView? = null
    private val userAdapter: UserPagedListAdapter by lazy {
        UserPagedListAdapter { navigateToUserDetailsFragment(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_github_search, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.github_search_menu, menu)
        val searchMenuItem = menu.findItem(R.id.app_bar_search)!!
        searchView = searchMenuItem.actionView as SearchView
        searchView?.apply {
            if (viewModel.searchViewExpanded)
                onActionViewExpanded()
            else
                onActionViewCollapsed()

            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            queryHint = getString(R.string.search_hint)

            disposables.add(handleSearchRequest(this)
                .debounce(AppSettings.DEBOUNCE_SEARCH_MILLISECONDS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { it.isBlank().not() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(LOG_TAG, "new query $it")
                        viewModel.requestNewSearch(it)
                        userAdapter.notifyDataSetChanged()
                    }, {
                        Log.d(LOG_TAG, "error searchView, $it")
                    }
                ))
            populateSearchViewText(this)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun handleSearchRequest(searchView: SearchView): Observable<String> =
        Observable.create { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    searchView.onActionViewCollapsed()
                    viewModel.searchViewExpanded = false
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    val text = p0 ?: ""
                    if (viewModel.searchText != text) {
                        viewModel.searchText = text
                        subscriber.onNext(text)
                    } else Log.d(LOG_TAG, "avoid extra request after rotate")
                    return true
                }
            })

            searchView.setOnSearchClickListener {
                viewModel.searchViewExpanded = true
            }
        }

    private fun populateSearchViewText(searchView: SearchView) {
        val oldText = viewModel.searchText
        if (oldText.isNotBlank()) {
            searchView.post {
                searchView.setQuery(oldText, false)
            }
            Log.d(LOG_TAG, "after rotate, ${searchView.query}")
        }
    }

    override fun onDestroyOptionsMenu() {
        disposables.clear()
        super.onDestroyOptionsMenu()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        viewModel.userPagedList.observe(viewLifecycleOwner, Observer {
            userAdapter.submitList(it)
        })

        viewModel.requestState.observe(viewLifecycleOwner, Observer {
            if (it == IN_PROGRESS) {
                stubMessageView.isVisible = false
                loadingBar.isVisible = true
            } else {
                loadingBar.isVisible = false
                when {
                    viewModel.isListEmpty() -> {
                        showStubMessage(getString(R.string.state_no_user_results_title))
                    }
                    it == FAIL -> {
                        showStubMessage(getString(R.string.error_loading_list))
                    }
                    it == FORBIDDEN -> {
                        Toast.makeText(
                            context, R.string.error_forbidden_search_title, Toast.LENGTH_SHORT
                        ).apply { setGravity(Gravity.CENTER, 0, 0) }.show()
                    }
                    else -> {
                        stubMessageView.isVisible = false
                    }
                }
            }
            if (viewModel.isListEmpty().not()) userAdapter.setNetworkState(it)
        })

    }

    private fun navigateToUserDetailsFragment(name: String) {
        searchView?.onActionViewCollapsed()
        findNavController().navigate(
            R.id.action_githubSearchFragment_to_githubUserFragment,
            bundleOf(GithubUserFragment.KEY_USER_NAME to name)
        )
    }

    private fun showStubMessage(message: String) {
        stubMessageView.isVisible = true
        stubMessageView.text = message
    }

}