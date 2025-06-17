package com.swapy.tastebuds.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.swapy.tastebuds.R
import com.swapy.tastebuds.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        observeEvents()
        viewModel.getRecipes()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(emptyList())
        binding.recipeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recipeAdapter
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.recipesEventsFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {

                        RecipesActivityEvents.HideProgressBar -> {
                            Timber.d("Hide ProgressBar")
                        }

                        RecipesActivityEvents.NavigateToDetailsActivity -> {
                            Timber.d("NavigateToDetailsActivity")
                            /*startActivity(Intent(this@MainActivity, DetailsActivity::class.java))
                                finish()*/
                        }

                        is RecipesActivityEvents.ShowFailureMessage -> {
                            Timber.e(it.msg)
                        }

                        RecipesActivityEvents.ShowProgressBar -> {
                            Timber.d("ProgressBar showing")
                        }

                        is RecipesActivityEvents.OnSuccess -> {
                            Timber.d(it.response.toString())
                            if (it.response != null) {
                                recipeAdapter = RecipeAdapter(it.response.meals)
                                binding.recipeRecyclerView.adapter = recipeAdapter
                            }
                        }
                    }
                }
        }
    }
}