package com.swapy.tastebuds.api

import com.swapy.tastebuds.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.POST

interface RecipeWebService {
    @POST("/api/json/v2/1/randomselection.php")
    suspend fun getRecipes(): Response<RecipeResponse>
}