package com.example.ecosheher.initialpages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecosheher.R

@Composable
fun Welcomepage3(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 160.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Illustration
        Image(
            painter = painterResource(id = R.drawable.welcome3),
            contentDescription = "Illustration",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Styled Text with More Line Spacing
        Text(
            text = buildAnnotatedString {
                append("You ")
                withStyle(style = SpanStyle(color = Color(0xFF00A651))) {
                    append("report")
                }
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = buildAnnotatedString {
                append("We ")
                withStyle(style = SpanStyle(color = Color(0xFF00A651))) {
                    append("Change")
                }
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = buildAnnotatedString {
                append("Together we")
                withStyle(style = SpanStyle(color = Color(0xFF00A651))) {
                    append("Build")
                }
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121), // Dark Gray for the rest
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

//
        Spacer(modifier = Modifier.height(20.dp))

        // Dots Indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 10.dp else 8.dp)
                        .padding(horizontal = 5.dp) // Increased spacing between dots
                        .background(
                            if (index == 0) Color(0xFF00A651) else Color(0xFFBDBDBD), // Green active dot, Gray inactive
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Centered Full-Width Button
        Button(
            onClick = { /* Handle button click */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651)), // Green button
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Next →", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}