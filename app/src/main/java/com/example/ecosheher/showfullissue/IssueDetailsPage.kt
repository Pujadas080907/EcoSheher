package com.example.ecosheher.showfullissue

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.example.ecosheher.R
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.firebases.FirestoreHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailsPage(
    reportId: String,
    title: String,
    description: String,
    category: String,
    location: String,
    imageUrl: String,
) {
    var upvoteCount by remember { mutableStateOf(0) }
    var isUpvoted by remember { mutableStateOf(false) }  // Track if user has upvoted
    var reportDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Fetch initial report details
    LaunchedEffect(reportId) {
        FirestoreHelper.getReport(reportId,
            onSuccess = { report ->
                upvoteCount = report.upvoteCount
                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                val date = Date(report.timestamp)
                reportDate = sdf.format(date)

                // Check if user has already upvoted
                isUpvoted = FirestoreHelper.hasUserUpvoted(context,reportId)
            },
            onFailure = { /* Handle error */ }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Issue Details", color = Color.White, fontSize = 20.sp) },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = com.example.ecosheher.R.color.main_color)),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        if (location.isNotEmpty()) {
                            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(location)}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                val fallbackIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                context.startActivity(fallbackIntent)
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.locationicon),
                        contentDescription = "Location Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = location, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Report Image",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        if (!isUpvoted) {  // Ensure user can upvote only once
                            val newUpvoteCount = upvoteCount + 1
                            isUpvoted = true
                            upvoteCount = newUpvoteCount

                            // Update Firestore
                            FirestoreHelper.updateUpvoteCount(
                                reportId = reportId,
                                newCount = newUpvoteCount,
                                onSuccess = { /* Success */ },
                                onFailure = { /* Handle failure */ }
                            )

                            // Mark user as having upvoted
                            FirestoreHelper.markUserUpvoted(context, reportId)
                        } else {  // User is removing upvote
                            val newUpvoteCount = upvoteCount - 1
                            isUpvoted = false
                            upvoteCount = newUpvoteCount

                            // Update Firestore
                            FirestoreHelper.updateUpvoteCount(
                                reportId = reportId,
                                newCount = newUpvoteCount,
                                onSuccess = { /* Success */ },
                                onFailure = { /* Handle failure */ }
                            )

                            // Remove upvote record
                            FirestoreHelper.unmarkUserUpvoted(context, reportId)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.upvotingicon),
                        contentDescription = "Upvote Icon",
                        modifier = Modifier.size(28.dp),
                        tint = if (isUpvoted) colorResource(R.color.main_color) else Color.Gray
                    )
                }
                Text(
                    text = upvoteCount.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Text(text = "Description: $description", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Category: $category", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: $reportDate", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
