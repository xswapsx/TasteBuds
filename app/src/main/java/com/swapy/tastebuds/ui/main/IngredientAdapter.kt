package com.swapy.tastebuds.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swapy.tastebuds.R
import com.swapy.tastebuds.model.Ingredient

class IngredientAdapter(private val ingredients: List<Ingredient>) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientImage: ImageView = itemView.findViewById(R.id.ingredient_image)
        val ingredientName: TextView = itemView.findViewById(R.id.ingredient_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient.name

        Glide.with(holder.ingredientImage.context)
            .load(ingredient.imageUrl)
            .placeholder(R.drawable.ingredient_placeholder)
            .error(R.drawable.ingredient_placeholder)
            .into(holder.ingredientImage)
    }

    override fun getItemCount(): Int = ingredients.size
}