package com.example.ecosheher.bottomNavPages


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.R

import com.example.ecosheher.authentication.AuthState
import com.example.ecosheher.authentication.AuthViewModel
import com.example.ecosheher.firebases.FirestoreHelper
import com.example.ecosheher.firebases.Report
import com.example.ecosheher.navGraph.Routes
import com.example.ecosheher.ui.theme.opansnaps
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var reportsByCategory by remember { mutableStateOf<Map<String, List<Report>>>(emptyMap()) }

    val categoryIcons = mapOf(
        "Roads & StreetLights" to R.drawable.homeicon,
        "Waste Management" to R.drawable.mycity,
        "Water & Utilities" to R.drawable.awareness,
        "Parks and Recreation" to R.drawable.acrossindia
    )

    LaunchedEffect(Unit) {
        FirestoreHelper.getAllReports { fetchedReports ->
            if (fetchedReports != null) {
                reports = fetchedReports.sortedByDescending { it.upvoteCount ?: 0 }
                reportsByCategory = fetchedReports
                    .groupBy { it.category ?: "Uncategorized" }
                    .mapValues { entry -> entry.value.sortedByDescending { it.timestamp ?: 0 } }
            } else {
                reports = emptyList()
                reportsByCategory = emptyMap()
            }
        }
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color)),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EcoSheher",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = {
                            navController.navigate(Routes.MyAccount.routes)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.authimg),
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Most Upvoted Posts
            Text(
                text = "Most Upvoted Posts",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = rememberLazyListState(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(reports) { report ->
                    Box(modifier = Modifier.width(screenWidth)) {
                        ReportItems0(report, navController)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space before category-wise posts

            Text(
                text = "Explore All Category Issues",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 10.dp )
            )
            // Category-wise Posts
            reportsByCategory.forEach { (category, reports) ->
                if (categoryIcons.containsKey(category)) { // Ensure category exists in the defined list
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        categoryIcons[category]?.let { iconRes ->
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = category,
                                modifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(Color.Gray)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(
                            text = category,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        state = rememberLazyListState(),
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(reports.filter { it.category == category }) { report ->
                            Box(modifier = Modifier.width(screenWidth)) {
                                ReportItems0(report, navController)
                            }
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun ReportItems0(report: Report, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable {
                val reportJson = Gson().toJson(report)  // Convert object to JSON
                val encodedJson = URLEncoder.encode(reportJson, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20")
                navController.navigate("${Routes.IssueDetails.routes}/$encodedJson")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)

        ) {
            Image(
                painter = rememberAsyncImagePainter(model = report.imageUrl),
                contentDescription = "Report Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Title: ${report.title}", fontWeight = FontWeight.Bold)
//            Text("Description: ${report.description}")
//            Text("Category: ${report.category}")
//            Text("Location: ${report.location}")
//            Spacer(modifier = Modifier.height(4.dp))
//            Text("Upvotes: ${report.upvoteCount}", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = "Upvote",
                    modifier = Modifier
                        .size(24.dp),
                    tint = colorResource(R.color.main_color)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Upvotes: ${report.upvoteCount}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }
    val items = listOf(
        "Home" to R.drawable.homeicon,
        "MyCity" to R.drawable.mycity,
        "Add" to R.drawable.plusicon,
        "AcrossIndia" to R.drawable.acrossindia,
        "Awareness" to R.drawable.awareness
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(
        modifier = Modifier
            .height(60.dp)
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
        containerColor = Color.LightGray,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            if (item.first == "Add") {

                FloatingActionButton(
                    onClick = {
                         navController.navigate(Routes.AddReport.routes)
                    },
                    containerColor = colorResource(id = R.color.main_color),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.second),
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                val isSelected = when (item.first) {
                    "Home" -> currentRoute == Routes.Home.routes
                    "MyCity" -> currentRoute == Routes.MyCity.routes
                    "AcrossIndia" -> currentRoute == Routes.AcrossIndia.routes
                    "Awareness" -> currentRoute == Routes.Awareness.routes
                    else -> false
                }
                IconButton(
                    onClick = {
                        selectedIndex.value = index
                        when (index) {
                            0 -> navController.navigate(Routes.Home.routes)
                            1 -> navController.navigate(Routes.MyCity.routes)
                            3 -> navController.navigate(Routes.AcrossIndia.routes)
                            4 -> navController.navigate(Routes.Awareness.routes)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = item.second),
                            contentDescription = item.first,
                            modifier = Modifier.size(20.dp),

                            tint = if (isSelected) colorResource(id = R.color.main_color) else Color.Black
                        )
                        Text(
                            text = item.first,
                            fontSize = 10.sp,
                            fontFamily = opansnaps,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colorResource(id = R.color.main_color) else Color.Black
                        )
                    }

                }

            }

        }
    }


}