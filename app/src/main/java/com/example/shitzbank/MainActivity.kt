package com.example.shitzbank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shitzbank.ui.theme.ShitzbankTheme
import com.example.shitzbank.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private var keepSplashScreenOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashScreenOn }
        CoroutineScope(Dispatchers.Main).launch {
            keepSplashScreenOn = false
        }
        enableEdgeToEdge()
        setContent {
            ShitzbankTheme {
                App(viewModel)
            }
        }
    }
}