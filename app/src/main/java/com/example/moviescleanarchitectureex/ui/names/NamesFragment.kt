package com.example.moviescleanarchitectureex.ui.names

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.appComponent
import com.example.moviescleanarchitectureex.databinding.FragmentNamesBinding
import com.example.moviescleanarchitectureex.domen.api.NamesInteractor
import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel
import com.example.moviescleanarchitectureex.presentation.names.NamesState
import com.example.moviescleanarchitectureex.presentation.names.NamesViewModel
import com.example.moviescleanarchitectureex.presentation.names.NamesViewModel_Factory
import javax.inject.Inject

class NamesFragment : Fragment() {

    //val viewModel: NamesViewModel by viewModels()
    lateinit var viewModel: NamesViewModel
    @Inject
    lateinit var namesInteractor: NamesInteractor


    /*var viewModelFactory = NamesViewModel.getViewModelFactory(namesInteractor)

    private val viewModel: NamesViewModel by viewModels { viewModelFactory }*/


    private val adapter = PersonsAdapter()

    private lateinit var binding: FragmentNamesBinding

    private lateinit var textWatcher: TextWatcher

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.personsList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher.let { binding.queryInput.addTextChangedListener(it) }

        viewModel = ViewModelProvider(this, NamesViewModel.getViewModelFactory(namesInteractor))[NamesViewModel::class.java]

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { binding.queryInput.removeTextChangedListener(it) }
    }

    private fun showToast(additionalMessage: String?) {
        // Здесь пришлось поправить использование Context
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun render(state: NamesState) {
        when (state) {
            is NamesState.Content -> showContent(state.persons)
            is NamesState.Empty -> showEmpty(state.message)
            is NamesState.Error -> showError(state.message)
            is NamesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        with(binding) {
            personsList.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            personsList.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            placeholderMessage.text = errorMessage
        }
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(persons: List<Person>) {
        with(binding) {
            personsList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        adapter.persons.clear()
        adapter.persons.addAll(persons)
        adapter.notifyDataSetChanged()
    }
}