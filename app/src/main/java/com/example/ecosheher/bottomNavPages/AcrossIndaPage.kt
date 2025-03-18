package com.example.ecosheher.bottomNavPages

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.R
import com.example.ecosheher.firebases.FirestoreHelper
import com.example.ecosheher.firebases.Report
import com.example.ecosheher.navGraph.Routes
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context



//@Composable
//fun AcrossIndiaPage(navController: NavController) {
//    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
//
//    // Fetch reports when the page is loaded
//    LaunchedEffect(Unit) {
//        FirestoreHelper.getAllReports { fetchedReports ->
//            reports = fetchedReports
//            Log.d("Firestore", "Fetched ${reports.size} reports")
//        }
//    }
//
//    if (reports.isEmpty()) {
//        Text("No reports available", modifier = Modifier.padding(16.dp))
//    } else {
//        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
//            items(reports) { report ->
//                ReportItems(report, navController)
//            }
//        }
//    }
//    Scaffold(
//        topBar = {
//            TopAppBar1(
//                title = { Text("Across India", color = Color.White, fontSize = 20.sp) },
//                modifier = Modifier.height(80.dp),
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color)),
//                actions = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.locationicon),
//                        contentDescription = "Location",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(30.dp)
//                            .clickable {
//                                if (ContextCompat.checkSelfPermission(
//                                        context, Manifest.permission.ACCESS_FINE_LOCATION
//                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//                                ) {
//
//                                    com.example.ecosheher.addReportPage.getCurrentLocation(
//                                        context,
//                                        fusedLocationClient
//                                    ) { location ->
//                                        val address =
//                                            com.example.ecosheher.addReportPage.getAddressFromLocation(
//                                                context,
//                                                location.latitude,
//                                                location.longitude
//                                            )
//                                        currentAddress = address
//                                        fetchReportsByLocation(address) { fetchedReports ->
//                                            reports = fetchedReports
//                                        }
//                                    }
//                                } else {
//                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//                                }
//                            }
//                    )
//                }
//
//            )
//        },
//
//        bottomBar = { BottomNavigationBar(navController) }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(paddingValues)
//                .padding(16.dp),
//            //horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Current Location: $currentAddress", fontSize = 12.sp, color = Color.Black)
//            Spacer(modifier = Modifier.height(9.dp))
//            Text(text = "Reported Issues in Your City:", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(12.dp))
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(vertical = 10.dp)
//            ) {
//                items(reports) { report ->
//                    ReportItem1(report,navController)
//                }
//            }
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcrossIndiaPage(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var currentAddress by remember { mutableStateOf("Fetching location...") }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, fusedLocationClient) { location ->
                val address = getAddressFromLocation(context, location.latitude, location.longitude)
                currentAddress = address
                fetchReportsByLocation(address) { fetchedReports -> reports = fetchedReports }
            }
        }
    }

    LaunchedEffect(Unit) {
        FirestoreHelper.getAllReports { fetchedReports ->
            reports = fetchedReports
            Log.d("Firestore", "Fetched ${reports.size} reports")
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
                                modifier = Modifier.size(15.dp) // Increased size for visibility
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp)) // Space between icon and title

                        Text(
                            text = "Across India",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f) // Ensures title is centered
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
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = "Reported Issues Across India:",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (reports.isEmpty()) {
                Text("No reports available", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(reports) { report ->
                        ReportItems(report, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ReportItems(report: Report, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Allows dynamic resizing
            .padding(6.dp)
            .background(Color.White)
            .clickable {
                val reportJson = Gson().toJson(report)
                val encodedJson = URLEncoder.encode(reportJson, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20")
                navController.navigate("${Routes.IssueDetails.routes}/$encodedJson")
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Row(
            modifier = Modifier.padding(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Report Image
            Image(
                painter = rememberAsyncImagePainter(model = report.imageUrl),
                contentDescription = "Report Image",
                modifier = Modifier
                    .height(130.dp)
                    .width(160.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Title
                Text(
                    text = report.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // *Category (Always shown, even if empty)*
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        ) {
                            append("Category: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        ) {
                            append(report.category.ifBlank { "N/A" }) // Show "N/A" if category is empty
                        }
                    },
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        ) {
                            append("Location: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        ) {
                            append(report.location)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Upvote Section
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.upvote),
                        contentDescription = "Upvote",
                        modifier = Modifier.size(14.dp),
                        tint = colorResource(R.color.main_color)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Upvotes: ${report.upvoteCount}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.main_color)
                    )
                }
            }
        }
    }
}