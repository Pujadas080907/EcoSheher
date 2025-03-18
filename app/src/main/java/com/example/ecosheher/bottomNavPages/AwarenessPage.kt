package com.example.ecosheher.bottomNavPages

import YouTubePlayer
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
                        // Top App Bar with Back Arrow and Centered Title
                        TopAppBar(
                                title = {
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                IconButton(
                                                        onClick = {
                                                                if (navController.previousBackStackEntry != null) {
                                                                        navController.popBackStack()
                                                                }
                                                        }
                                                ) {
                                                        Icon(
                                                                painter = painterResource(R.drawable.backarrow),
                                                                contentDescription = "Back",
                                                                tint = Color.White,
                                                                modifier = Modifier.size(15.dp)
                                                        )
                                                }

                                                Spacer(modifier = Modifier.width(5.dp)) // Adjust spacing between icon and title

                                                Text(
                                                        text = "Civic Engagement",
                                                        color = Color.White,
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.weight(1f), // Ensures centering
                                                )
                                        }
                                },
                                modifier = Modifier.height(80.dp),
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color))
                        )
                },
                bottomBar = {
                        // Bottom Navigation Bar
                        BottomNavigationBar(navController)
                }
        ) { paddingValues ->
                Column(
                        modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize()
                                .padding(top = 100.dp, end = 15.dp, bottom = 15.dp, start = 15.dp)
                                .padding(bottom = paddingValues.calculateBottomPadding()) // Adjust for bottom navigation bar
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
