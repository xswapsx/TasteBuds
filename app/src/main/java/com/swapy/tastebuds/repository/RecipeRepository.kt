package com.swapy.tastebuds.repository

import com.swapy.tastebuds.api.RecipeWebService
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val recipeService: RecipeWebService) {
    suspend fun getRecipes() =
        recipeService.getRecipes()
}