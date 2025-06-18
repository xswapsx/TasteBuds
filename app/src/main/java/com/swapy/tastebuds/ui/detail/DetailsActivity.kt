package com.swapy.tastebuds.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.swapy.tastebuds.R
import com.swapy.tastebuds.databinding.ActivityDetailsBinding
import com.swapy.tastebuds.model.Ingredient
import com.swapy.tastebuds.model.Meal
import com.swapy.tastebuds.ui.main.MainActivity
import timber.log.Timber
import androidx.core.net.toUri

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var currentMeal: Meal? = null
    private lateinit var ingredientAdapter: IngredientDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentMeal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MainActivity.EXTRA_MEAL_DETAILS, Meal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(MainActivity.EXTRA_MEAL_DETAILS)
        }
        Timber.d("currentMeal $currentMeal")

        currentMeal?.let { bindMealData(it) }
        currentMeal?.let { setupRecyclerView(it) }
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnFavorite.setOnClickListener {
            currentMeal?.let { toggleFavorite(it) }
        }

        binding.btnViewOnYoutube.setOnClickListener {
            currentMeal?.let {
                val intent = Intent(Intent.ACTION_VIEW, it.strYoutube?.toUri())
                startActivity(intent)
            }
        }
    }

    private fun setupRecyclerView(meal: Meal) {
        ingredientAdapter = IngredientDetailsAdapter(meal.getIngredients().mapNotNull { (ingredient, measure) ->
            if (ingredient?.isNotBlank() == true && ingredient != "null") {
                val displayName = if (measure?.isNotBlank() == true && measure != "null") {
                    "$ingredient - $measure"
                } else {
                    ingredient
                }

                val ingredientNameForUrl = ingredient.lowercase().replace(" ", "-")
                val imageUrl = "https://www.themealdb.com/images/ingredients/$ingredientNameForUrl.png"
                Ingredient(displayName, imageUrl)
            } else {
                null
            }
        })

        binding.rvIngredients.apply {
            layoutManager = LinearLayoutManager(this@DetailsActivity)
            adapter = ingredientAdapter
        }
    }

    private fun bindMealData(meal: Meal) {

        Glide.with(this)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.recipe_image_placeholder)
            .into(binding.imgHeader)

        binding.tvTitle.text = meal.strMeal ?: "Unknown Meal"

        binding.tvTime.text = "15 Min"

        binding.tvDescription.text = meal.strInstructions ?: "No instructions available"

        val ingredientCount = meal.getIngredients().count { (ingredient, _) ->
            ingredient?.isNotBlank() == true && ingredient != "null"
        }
        binding.tvItemCount.text =
            resources.getQuantityString(R.plurals.item_plural, ingredientCount, ingredientCount)
    }

    private fun toggleFavorite(meal: Meal) {
        val isFavorite = meal.idMeal?.let { _ ->
            val currentState = binding.btnFavorite.tag as? Boolean ?: false
            val newState = !currentState
            binding.btnFavorite.tag = newState
            binding.btnFavorite.setImageResource(
                if (newState) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
            newState
        } ?: false
        Timber.d("Favorite toggled for ${meal.strMeal} to $isFavorite")
    }

    private fun Meal.getIngredients(): List<Pair<String?, String?>> = listOf(
        strIngredient1 to strMeasure1,
        strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5,
        strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7,
        strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11,
        strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13,
        strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15,
        strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17,
        strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19,
        strIngredient20 to strMeasure20
    )
}