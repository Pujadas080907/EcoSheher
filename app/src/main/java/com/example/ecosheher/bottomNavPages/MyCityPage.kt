package com.example.ecosheher.bottomNavPages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import com.example.ecosheher.ui.theme.opansnaps
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityPage(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentAddress by remember { mutableStateOf("Fetching location...") }
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, fusedLocationClient) { location ->
                val address = getAddressFromLocation(context, location.latitude, location.longitude)
                currentAddress = address
                fetchReportsByLocation(address) { fetchedReports ->
                    reports = fetchedReports.sortedByDescending { it.upvoteCount ?: 0 }
                }

            }
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(context, fusedLocationClient) { location ->
                val address = getAddressFromLocation(context, location.latitude, location.longitude)
                currentAddress = address
                fetchReportsByLocation(address) { fetchedReports -> reports = fetchedReports }
            }
        }
    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("My City", color = Color.White, fontSize = 20.sp) },
//                modifier = Modifier.height(80.dp),
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color)),
//                actions = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.locationicon),
//                        contentDescription = "Location",
//                        tint = Color.Red,
//                        modifier = Modifier
//                            .size(30.dp)
//                            .clickable {
//                                if (ContextCompat.checkSelfPermission(
//                                        context, Manifest.permission.ACCESS_FINE_LOCATION
//                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//                                ) {
//                                    getCurrentLocation(context, fusedLocationClient) { location ->
//                                        val address = getAddressFromLocation(
//                                            context,
//                                            location.latitude,
//                                            location.longitude
//                                        )
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
//            )
//        },

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Icon
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
                                modifier = Modifier.size(15.dp) // Increased size
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp)) // Space between back icon and title

                        // Title
                        Text(
                            text = "My City",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f) // Ensures title stays centered
                        )

                        // Location Icon (on the right)
                        Icon(
                            painter = painterResource(id = R.drawable.locationicon),
                            contentDescription = "Location",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    if (ContextCompat.checkSelfPermission(
                                            context, Manifest.permission.ACCESS_FINE_LOCATION
                                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                    ) {
                                        getCurrentLocation(context, fusedLocationClient) { location ->
                                            val address = getAddressFromLocation(
                                                context,
                                                location.latitude,
                                                location.longitude
                                            )
                                            currentAddress = address
                                            fetchReportsByLocation(address) { fetchedReports ->
                                                reports = fetchedReports
                                            }
                                        }
                                    } else {
                                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    }
                                }
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
                .padding(16.dp),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Current Location: $currentAddress", fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(9.dp))
            Text(text = "Reported Issues in Your City:", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(reports) { report ->
                    ReportItem(report,navController)
                    Divider(
                        color = Color.LightGray, // Customize the color
                        thickness = 0.6.dp,
                        modifier = Modifier.padding(vertical = 8.dp) // Add spacing
                    )
                }
            }

        }
    }
}

@Composable
fun ReportItem(report: Report, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color.White)
            .padding(8.dp)
            .clickable {
                Log.d("Navigation", "Navigating with Report: $report")  // Debugging
                if (report.reportId.isNotBlank()) {
                    val reportJson = Gson().toJson(report)
                    val encodedJson =
                        URLEncoder.encode(reportJson, StandardCharsets.UTF_8.toString())
                            .replace("+", "%20")
                    navController.navigate("${Routes.IssueDetails.routes}/$encodedJson")
                } else {
                    Log.e("Navigation", "Report ID is empty, cannot navigate")
                }
            },
        shape = RoundedCornerShape(12.dp),
       // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Row(
                    modifier = Modifier.padding(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Title: ${report.title}", fontWeight = FontWeight.Bold)
//            Text("Description: ${report.description}")
//            Text("Category: ${report.category}")
//            Text("Location: ${report.location}")
//            Spacer(modifier = Modifier.height(4.dp))
//            Text("Upvotes: ${report.upvoteCount}", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Column(modifier =
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center

            ) {
//                Text("Title: ${report.title}", fontWeight = FontWeight.Bold)
                Text(
                    text = report.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))

//               Text("Category: ${report.category}")
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
                            append(report.category.ifBlank { "Not Chosen" }) // Show "N/A" if category is empty
                        }
                    },
                )
//                Text("Location: ${report.location}")
                // Location
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray)) {
                            append("Location: ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal,fontSize = 12.sp, color = Color.Black)) {
                            append(report.location)
                        }
                    },
                    //fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

//              Text("Upvotes: ${report.upvoteCount}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                Text(
//                    text = "Upvotes: ${report.upvoteCount}",
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = colorResource(R.color.main_color)
//                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.upvote), // Ensure this is the correct icon in res/drawable
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



@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location) -> Unit
) {
    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location)
            } else {
                Toast.makeText(context, "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
}

fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            "${address.locality ?: "Unknown"}, ${address.postalCode ?: ""}"
        } else {
            "Location Not Found"
        }
    } catch (e: Exception) {
        "Error Fetching Location"
    }
}

fun fetchReportsByLocation(location: String, onResult: (List<Report>) -> Unit) {
    FirestoreHelper.getReportsByLocation(location, onResult)
}