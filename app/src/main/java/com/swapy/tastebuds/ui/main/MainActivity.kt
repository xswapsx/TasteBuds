package com.swapy.tastebuds.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.swapy.tastebuds.ui.detail.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    companion object {
        const val EXTRA_MEAL_DETAILS = "com.swapy.tastebuds.ui.main.MEAL_DETAILS"
    }

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
        recipeAdapter = RecipeAdapter(emptyList()) {}
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
                            binding.pbMain.visibility = View.GONE
                        }

                        is RecipesActivityEvents.ShowFailureMessage -> {
                            Timber.e(it.msg)
                        }

                        RecipesActivityEvents.ShowProgressBar -> {
                            binding.pbMain.visibility = View.VISIBLE
                        }

                        is RecipesActivityEvents.OnSuccess -> {
                            Timber.d(it.response.toString())
                            if (it.response != null) {
                                recipeAdapter = RecipeAdapter(it.response.meals) { meal ->

                                    val intent =
                                        Intent(this@MainActivity, DetailsActivity::class.java).apply {
                                            putExtra(EXTRA_MEAL_DETAILS, meal)
                                        }
                                    startActivity(intent)
                                }
                                binding.recipeRecyclerView.adapter = recipeAdapter
                            }
                        }
                    }
                }
        }
    }
}