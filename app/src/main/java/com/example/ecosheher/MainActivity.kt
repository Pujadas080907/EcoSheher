package com.example.ecosheher

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ecosheher.authentication.AuthViewModel
import com.example.ecosheher.cloudinary.CloudinaryHelper
import com.example.ecosheher.navGraph.SetNavGraph
import com.example.ecosheher.ui.theme.EcoSheherTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CloudinaryHelper.initCloudinary(this)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        CoroutineScope(Dispatchers.Main).launch {
            delay(3500L)
            splashScreen.setKeepOnScreenCondition { false }
        }
        val authViewModel : AuthViewModel by viewModels()
        setContent {
            EcoSheherTheme {
                SetNavGraph(modifier = Modifier, authViewModel = authViewModel)
            }
        }
    }
}

