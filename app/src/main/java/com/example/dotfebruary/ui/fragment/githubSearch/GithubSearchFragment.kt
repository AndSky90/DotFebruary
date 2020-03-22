package com.example.dotfebruary.ui.fragment.githubSearch

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotfebruary.R
import com.example.dotfebruary.common.AppSettings.LOG_TAG
import com.example.dotfebruary.common.getSearchObservable
import com.example.dotfebruary.model.RequestState.*
import com.example.dotfebruary.ui.fragment.githubSearch.adapter.UserPagedListAdapter
import com.example.dotfebruary.ui.fragment.githubUser.GithubUserFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_github_search.*


class GithubSearchFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private val viewModel: GithubSearchViewModel by viewModels()

    private val userAdapter: UserPagedListAdapter by lazy {
        UserPagedListAdapter { showUserDetailsFragment(it) }
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
        val searchView = menu.findItem(R.id.app_bar_search)!!.actionView as SearchView
        disposables.add(searchView.getSearchObservable()
            .subscribe(
                {
                    Log.d(LOG_TAG, it)
                    viewModel.requestNewSearch(it)
                    userAdapter.notifyDataSetChanged()
                }, {
                    Log.d(LOG_TAG, "error sv, $it")
                }
            ))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyOptionsMenu() {
        disposables.dispose()
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

         /*viewModel.requestState.observe(viewLifecycleOwner, Observer {
             emptyResultsView.isVisible = false
             loadingBar.isVisible = false

             when (it) {
                 FAIL -> if (viewModel.isListEmpty()) emptyResultsView.isVisible = true
                 SUCCESS -> { }
                 IN_PROGRESS -> if (viewModel.isListEmpty()) loadingBar.isVisible = true
                 else -> { }
             }

             if (viewModel.isListEmpty().not()) userAdapter.setNetworkState(it)
         })*/

    }

    private fun showUserDetailsFragment(name: String) {
        findNavController().navigate(
            R.id.action_githubSearchFragment_to_githubUserFragment,
            bundleOf(GithubUserFragment.KEY_USER_NAME to name)
        )
    }

}


