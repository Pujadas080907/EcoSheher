package com.example.ecosheher.bottomNavPages

import YouTubePlayer
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ecosheher.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwarenessPage(navController: NavController) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val scrollState = rememberScrollState()

        Scaffold(
                topBar = {
                        // Top App Bar
                        TopAppBar(
                                title = { Text("Civic Engagement", color = Color.White, fontSize = 20.sp) },
                                modifier = Modifier.height(80.dp),
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color)),
                        )
                },
                bottomBar = {
                        // Bottom Navigation Bar (You can customize it as per your app's requirements)
                        BottomNavigationBar(navController)
                }
        ) { paddingValues ->
                Column(
                        modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize()
                                .padding(top = 100.dp, end = 15.dp, bottom = 15.dp, start = 15.dp)
                                .padding(bottom = paddingValues.calculateBottomPadding()) // Adjust for the bottom navigation bar
                                .verticalScroll(scrollState) // Enable vertical scrolling
                ) {
                        // YouTube Player
                        YouTubePlayer(videoId = "x6bNwmrBPXI", lifecycleOwner = lifecycleOwner)

                        // Spacer to push the button to the bottom
                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to navigate to Civic Engagement Wikipedia page
                        Button(
                                onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Civic_engagement"))
                                        context.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(id = R.color.main_color)
                                )
                        ) {
                                Text(text = "Know More about Civic Engagement", color = Color.White)
                        }
                }
        }
}
