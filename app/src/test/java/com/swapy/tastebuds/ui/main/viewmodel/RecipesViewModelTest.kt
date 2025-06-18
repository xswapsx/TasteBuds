package com.swapy.tastebuds.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.swapy.tastebuds.model.Meal
import com.swapy.tastebuds.model.RecipeResponse
import com.swapy.tastebuds.repository.RecipeRepository
import com.swapy.tastebuds.ui.main.RecipesActivityEvents
import com.swapy.tastebuds.ui.main.RecipesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class RecipesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var recipesRepository: RecipeRepository

    private lateinit var viewModel: RecipesViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipesViewModel(recipesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits ShowProgressBar, OnSuccess, and HideProgressBar on success`() = runTest {
        val fakeResponse = RecipeResponse(
            meals = listOf(
                Meal(
                    "1",
                    "Test Meal",
                    "Test Category",
                    "Test Area",
                    "Test Instructions",
                    "Test Thumbnail",
                    "Test Tags",
                    "Test Youtube",
                    "Test Ingredient 1",
                    "Test Ingredient 2",
                    "Test Ingredient 3",
                    "Test Ingredient 4",
                    "Test Ingredient 5",
                    "Test Ingredient 6",
                    "Test Ingredient 7",
                    "Test Ingredient 8",
                    "Test Ingredient 9",
                    "Test Ingredient 10",
                    "Test Ingredient 11",
                    "Test Ingredient 12",
                    "Test Ingredient 13",
                    "Test Ingredient 14",
                    "Test Ingredient 15",
                    "Test Ingredient 16",
                    "Test Ingredient 17",
                    "Test Ingredient 18",
                    "Test Ingredient 19",
                    "Test Ingredient 20",
                    "Test Measure 1",
                    "Test Measure 2",
                    "Test Measure 3",
                    "Test Measure 4",
                    "Test Measure 5",
                    "Test Measure 6",
                    "Test Measure 7",
                    "Test Measure 8",
                    "Test Measure 9",
                    "Test Measure 10",
                    "Test Measure 11",
                    "Test Measure 12",
                    "Test Measure 13",
                    "Test Measure 14",
                    "Test Measure 15",
                    "Test Measure 16",
                    "Test Measure 17",
                    "Test Measure 18",
                    "Test Measure 19",
                    "Test Measure 20",
                    "Test Source",
                    "Test Image Source",
                    "Test Creative Commons Confirmed",
                    "Test Date Modified", strYoutube = ""
                )
            )
        )
        val response = Response.success(fakeResponse)

        Mockito.`when`(recipesRepository.getRecipes()).thenReturn(response)

        viewModel.getRecipes()

        viewModel.recipesEventsFlow.test {
            assertEquals(RecipesActivityEvents.ShowProgressBar, awaitItem())
            assertEquals(RecipesActivityEvents.OnSuccess(fakeResponse), awaitItem())
            assertEquals(RecipesActivityEvents.HideProgressBar, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits ShowProgressBar, ShowFailureMessage, and HideProgressBar on empty body`() = runTest {
        val response = Response.success<RecipeResponse>(null)

        Mockito.`when`(recipesRepository.getRecipes()).thenReturn(response)

        viewModel.getRecipes()

        viewModel.recipesEventsFlow.test {
            assertEquals(RecipesActivityEvents.ShowProgressBar, awaitItem())
            val errorEvent = awaitItem() as RecipesActivityEvents.ShowFailureMessage
            assertEquals("Empty Response", errorEvent.msg)
            assertEquals(RecipesActivityEvents.HideProgressBar, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits ShowFailureMessage on unexpected error`() = runTest {
        Mockito.`when`(recipesRepository.getRecipes()).thenThrow(RuntimeException("Server error"))

        viewModel.getRecipes()

        viewModel.recipesEventsFlow.test {
            assertEquals(RecipesActivityEvents.ShowProgressBar, awaitItem())
            assertEquals(RecipesActivityEvents.HideProgressBar, awaitItem())
            val errorEvent = awaitItem() as RecipesActivityEvents.ShowFailureMessage
            assertEquals("Conversion Error", errorEvent.msg)
            cancelAndIgnoreRemainingEvents()
        }
    }

}