package com.swapy.tastebuds.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swapy.tastebuds.model.Meal
import com.swapy.tastebuds.R
import com.swapy.tastebuds.model.Ingredient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class RecipeAdapter(private val meals: List<Meal>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val favoriteStates = mutableMapOf<String, Boolean>()
    private val baseIngredientImageUrl = "https://www.themealdb.com/images/ingredients/"

    init {
        meals.forEach { meal ->
            meal.idMeal?.let { id ->
                favoriteStates[id] = false
            }
        }
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite_icon)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipe_title)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipe_description)
        val viewMore: TextView = itemView.findViewById(R.id.view_more)
        val ingredientsRecyclerView: RecyclerView = itemView.findViewById(R.id.ingredients_recycler_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val meal = meals[position]

        Glide.with(holder.recipeImage.context)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.recipe_image_placeholder)
            .into(holder.recipeImage)

        holder.recipeTitle.text = meal.strMeal

        val description = meal.strInstructions?.take(60) + meal.strInstructions?.length?.let { if (it > 60) "..." else "" }
        holder.recipeDescription.text = description

        val ingredients = mutableListOf<Ingredient>()
        val ingredientFields = listOf(
            meal.strIngredient1 to meal.strMeasure1,
            meal.strIngredient2 to meal.strMeasure2,
            meal.strIngredient3 to meal.strMeasure3,
            meal.strIngredient4 to meal.strMeasure4,
            meal.strIngredient5 to meal.strMeasure5,
            meal.strIngredient6 to meal.strMeasure6,
            meal.strIngredient7 to meal.strMeasure7,
            meal.strIngredient8 to meal.strMeasure8,
            meal.strIngredient9 to meal.strMeasure9,
            meal.strIngredient10 to meal.strMeasure10,
            meal.strIngredient11 to meal.strMeasure11,
            meal.strIngredient12 to meal.strMeasure12,
            meal.strIngredient13 to meal.strMeasure13,
            meal.strIngredient14 to meal.strMeasure14,
            meal.strIngredient15 to meal.strMeasure15,
            meal.strIngredient16 to meal.strMeasure16,
            meal.strIngredient17 to meal.strMeasure17,
            meal.strIngredient18 to meal.strMeasure18,
            meal.strIngredient19 to meal.strMeasure19,
            meal.strIngredient20 to meal.strMeasure20
        )

        ingredientFields.forEach { (ingredient, measure) ->

            if (ingredient?.isNotBlank() == true && ingredient != "null") {
                val displayName = if (measure?.isNotBlank() == true && measure != "null") {
                    "$ingredient - $measure"
                } else {
                    ingredient
                }

                val ingredientNameForUrl = ingredient.lowercase().replace(" ", "-")
                val encodedIngredientName = URLEncoder.encode(ingredientNameForUrl, StandardCharsets.UTF_8.toString())
                val imageUrl = "$baseIngredientImageUrl$encodedIngredientName.png"
                ingredients.add(Ingredient(displayName, imageUrl))
            }
        }

        holder.ingredientsRecyclerView.layoutManager = LinearLayoutManager(
            holder.ingredientsRecyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.ingredientsRecyclerView.adapter = IngredientAdapter(ingredients)

        holder.viewMore.setOnClickListener {
            // open recipe details
        }

        val isFavorite = meal.idMeal?.let { favoriteStates[it] } ?: false
        holder.favoriteIcon.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        holder.favoriteIcon.setOnClickListener {
            meal.idMeal?.let { id ->
                val currentState = favoriteStates[id] ?: false
                val newState = !currentState
                favoriteStates[id] = newState
                holder.favoriteIcon.setImageResource(
                    if (newState) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                )
            }
        }
    }

    override fun getItemCount(): Int = meals.size
}