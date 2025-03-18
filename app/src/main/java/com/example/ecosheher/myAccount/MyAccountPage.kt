package com.example.ecosheher.myAccount

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.bottomNavPages.BottomNavigationBar
import com.example.ecosheher.firebases.FirestoreHelper
import com.example.ecosheher.firebases.FirestoreHelper.deleteReport
import com.example.ecosheher.firebases.Report
import com.example.ecosheher.navGraph.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.ecosheher.R
import com.example.ecosheher.authentication.AuthViewModel
import com.example.ecosheher.ui.theme.opansnaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountPage(navController: NavController,authViewModel : AuthViewModel) {
    val context = LocalContext.current
    var reports by remember { mutableStateOf(emptyList<Report>()) }
    var userName by remember { mutableStateOf("User") }
    var userEmail by remember { mutableStateOf("No email") }
    var showDialog by remember { mutableStateOf(false) }

    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    LaunchedEffect(Unit) {
        user?.let {
            userName = it.displayName ?: "User"
            userEmail = it.email ?: "No email"
        }

        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("reports").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { documents ->
                    val reportList = documents.mapNotNull { it.toObject(Report::class.java) }
                    reports = reportList
                }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Failed to fetch reports: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "My Reports", color = Color.White,
                            fontSize = 20.sp, fontWeight = FontWeight.Bold,
                            fontFamily = opansnaps,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "Logout",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 10.dp)
                                .clickable { showDialog = true },
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color))
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)

        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Name: ")
                        }
                        append(userName)
                    },
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Email: ")
                        }
                        append(userEmail)
                    },
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }



             // Display Reports
            if (reports.isEmpty()) {
                Text("No reports available", fontSize = 16.sp, color = Color.Gray)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(reports.size) { index ->
                        ReportItem(
                            report = reports[index],
                            navController = navController,
                            onDelete = { reportId ->
                                reports = reports.filter { it.reportId != reportId }
                            }
                        )
                    }
                }
            }
        }

//        // Show confirmation dialog before logout
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text("Confirm Logout") },
//                text = { Text("Are you sure you want to log out?") },
//                confirmButton = {
//                    Button(onClick = {
//                        authViewModel.signout()
//                        navController.navigate(Routes.Login.routes) {
//                            popUpTo(Routes.Home.routes) { inclusive = true }
//                        }
//                        showDialog = false
//
//                    }) {
//                        Text("Yes")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showDialog = false }) {
//                        Text("No")
//                    }
//                }
//            )
//        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                containerColor = Color.White, // Background color
                shape = RoundedCornerShape(18.dp), // Smooth UI with rounded corners
                title = {
                    Text(
                        text = "Confirm Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center the text
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to log out?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center the text
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally) // Space buttons side by sidehorizontally
                    ) {
                        Button(
                            onClick = {
                                authViewModel.signout()
                        navController.navigate(Routes.Login.routes) {
                            popUpTo(Routes.Home.routes) { inclusive = true }
                        }
                        showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green color
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Yes", color = Color.White)
                        }
                    }
                },
                dismissButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        //horizontalArrangement = Arrangement.Center // Center buttons horizontally
                    ) {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)), // Gray color
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("No", color = Color.Black)
                        }
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun ReportItem(report: Report, navController: NavController, onDelete: (String)-> Unit) {
    val context = LocalContext.current // Get context here
    val userId = getCurrentUserId() // Fetch current user's ID
    var longPressDetected by remember { mutableStateOf(false) }  // Track long press
    var showDeleteDialog by remember { mutableStateOf(false) } // Show confirmation dialog

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    Log.d("Navigation", "Navigating with Report: $report")  // Debugging
                    if (report.reportId.isNotBlank()) {
                        val reportJson = Gson().toJson(report)
                        val encodedJson = URLEncoder.encode(reportJson, StandardCharsets.UTF_8.toString())
                            .replace("+", "%20")
                        navController.navigate("${Routes.IssueDetails.routes}/$encodedJson")
                    } else {
                        Log.e("Navigation", "Report ID is empty, cannot navigate")
                    }
                },
                onLongClick = {
                    longPressDetected = true
                    if (report.userId == userId) {  // Check if current user is the report's creator
                        showDeleteDialog = true // Show confirmation dialog
                    }
                }
            ),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Image(
                painter = rememberAsyncImagePainter(model = report.imageUrl),
                contentDescription = "Report Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

        }
    }

//    // Show confirmation dialog on long press
//    if (showDeleteDialog) {
//        AlertDialog(
//            onDismissRequest = { showDeleteDialog = false },
//            title = { Text("Delete Report") },
//            text = { Text("Are you sure you want to delete this report?") },
//            confirmButton = {
//                Button(onClick = {
//                    deleteReport(report.reportId, context)
//                    onDelete(report.reportId)
//                    showDeleteDialog = false
//                }) {
//                    Text("Yes")
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showDeleteDialog = false }) {
//                    Text("No")
//                }
//            }
//        )
//    }

    // Show confirmation dialog on long press
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            modifier = Modifier
                .fillMaxWidth(),  // Reduced padding around the dialog
            containerColor = Color.White, // Background color
            shape = RoundedCornerShape(16.dp), // Rounded corners
            title = {
                Text(
                    text = "Delete Report",
                    fontSize = 18.sp,  // Smaller font size for the title
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center the title text
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this report?",
                    fontSize = 15.sp,  // Smaller font size for the text
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center the text
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        deleteReport(report.reportId, context)
                        onDelete(report.reportId)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green color
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth() // Reduced padding between buttons
                ) {
                    Text("Yes", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)), // Gray color
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth() // Reduced padding between buttons
                ) {
                    Text("No", color = Color.Black)
                }
            }
        )
    }
}

fun getCurrentUserId(): String {
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    return firebaseUser?.uid ?: ""
}
