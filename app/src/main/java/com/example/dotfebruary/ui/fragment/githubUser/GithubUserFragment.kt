package com.example.dotfebruary.ui.fragment.githubUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dotfebruary.R
import com.example.dotfebruary.databinding.FragmentGithubUserBinding
import com.example.dotfebruary.model.RequestState
import kotlinx.android.synthetic.main.fragment_github_user.*

class GithubUserFragment : Fragment() {

    companion object {
        const val KEY_USER_NAME = "KEY_USER_NAME"
    }

    private val viewModel: GithubUserViewModel by viewModels()
    private lateinit var binding: FragmentGithubUserBinding
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments?.getString(KEY_USER_NAME) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_github_user, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserDetails(name)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.user = it
        })

        viewModel.requestState.observe(viewLifecycleOwner, Observer {
            loadingBar.isVisible = (it == RequestState.IN_PROGRESS)
            errorView.isVisible = (it == RequestState.FAIL)
        })

    }

}
