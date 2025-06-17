package com.swapy.tastebuds.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapy.tastebuds.model.RecipeResponse
import com.swapy.tastebuds.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(private val recipesRepository: RecipeRepository) :
    ViewModel() {
    private val recipesEventsChannel = Channel<RecipesActivityEvents>()
    val recipesEventsFlow = recipesEventsChannel.receiveAsFlow()

    fun getRecipes() = viewModelScope.launch {
        recipesEventsChannel.send(RecipesActivityEvents.ShowProgressBar)

        try {
            val response = recipesRepository.getRecipes()
            viewModelScope.launch {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val responseBody = response.body()
                        recipesEventsChannel.send(
                            RecipesActivityEvents.OnSuccess(
                                responseBody
                            )
                        )
                    } else {
                        recipesEventsChannel.send(
                            RecipesActivityEvents.ShowFailureMessage(
                                null,
                                "Empty Response"
                            )
                        )
                    }
                }
                recipesEventsChannel.send(RecipesActivityEvents.HideProgressBar)
            }
        } catch (t: Throwable) {
            recipesEventsChannel.send(RecipesActivityEvents.HideProgressBar)
            when (t) {
                is IOException ->
                    recipesEventsChannel.send(
                        RecipesActivityEvents.ShowFailureMessage(
                            null,
                            "Connection Timeout"
                        )
                    )

                else ->
                    recipesEventsChannel.send(
                        RecipesActivityEvents.ShowFailureMessage(
                            null,
                            "Conversion Error"
                        )
                    )
            }
        }
    }
}

sealed class RecipesActivityEvents {
    data object ShowProgressBar : RecipesActivityEvents()
    data object HideProgressBar : RecipesActivityEvents()
    data class ShowFailureMessage(val response: RecipeResponse?, val msg: String?) :
        RecipesActivityEvents()
    data class OnSuccess(val response: RecipeResponse?) : RecipesActivityEvents()
    data object NavigateToDetailsActivity : RecipesActivityEvents()
}