package com.example.ecosheher.initialpages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecosheher.R
import com.example.ecosheher.navGraph.Routes


@Composable
fun WelcomePage(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay content inside a centered column
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 40.dp), // Increased bottom padding to move content down
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Welcome to",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "EcoSheher",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF02B561),
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp)) // Increased spacing

            Text(
                text = "Your way to keep your 'sheher' clean and green. Our app lets you do exactly that.",
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp)) // More space before button

            // Centered Full-Width Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {  navController.navigate("welcome2") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Get Started →", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp)) // Adjusted bottom padding
        }
    }
}