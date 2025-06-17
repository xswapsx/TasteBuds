package com.swapy.tastebuds.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.swapy.tastebuds.R
import com.swapy.tastebuds.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: RecipesViewModel by viewModels()

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
        observeEvents()
        viewModel.getRecipes()
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.recipesEventsFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        /*LoginEvents.DisableLoginButton -> {
                            disableLoginButton()
                        }

                        LoginEvents.EnableLoginButton -> {
                            enableLoginButton()
                        }

                        LoginEvents.HideProgressBar -> {
                            binding.pbLogin.visibility = View.GONE
                        }

                        LoginEvents.NavigateToDashboardActivity -> {
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        }

                        is LoginEvents.OnLoginSuccess -> {
                            Timber.d("${it.response}")
                            showApiSuccessMessage(
                                it.response.message,
                                it.response.messageMar,
                                it.response.messageHindi
                            )
                            if (rememberPasswordEnabled) {
                                val mobile = binding.etdUsername.text.toString()
                                val password1 = binding.etPassword.text.toString()
                                storeCredentials(this@LoginActivity, mobile, password1)
                            } else {
                                clearCredentials(this@LoginActivity)
                            }
                        }

                        is LoginEvents.ShowFailureMessage -> {
                            if (it.response != null)
                                showApiErrorMessage(
                                    msg = it.response.message,
                                    msgMr = it.response.messageMar,
                                    msgHi = it.response.messageHindi
                                )
                            else
                                it.msg?.let { it1 -> errorMsg(this@LoginActivity, it1) }
                        }

                        LoginEvents.ShowProgressBar -> {
                            binding.pbLogin.visibility = View.VISIBLE
                        }*/
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
                        }
                    }
                }
        }
    }
}