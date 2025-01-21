package com.example.geminiapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geminiapp.chatBot.ChatBot
import com.example.geminiapp.imageAndText.ImageAndText
import com.example.geminiapp.textTotext.TextToText
import com.example.geminiapp.ui.theme.GeminiAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            Header(navController)
                        }
                        composable("screen_A") {
                            ChatBot(
                                navController = navController
                            )
                        }
                        composable("screen_B"){
                            ImageAndText(navController = navController)
                        }
                        composable("screen_C"){
                            TextToText(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(navController: NavController) {
    val isDarkTheme = isSystemInDarkTheme()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon2), // Your image
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp), // Adjust size and padding as needed
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Aether Realm",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    navigationIconContentColor = if (isDarkTheme) Color.White else Color.Black,
                    titleContentColor = if (isDarkTheme) Color.White else Color.Black
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg3),
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Content Area
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeScreen(navController)
                }
            }
        }
    )
}
@Composable
fun HomeScreen(navController: NavController){
    val apps = mutableListOf(
        AppInfo(
            "Chat Bot", "An intelligent chatbot designed to assist with " +
                    "your queries and provide instant solutions." +
                    " A smart companion for everyday questions!", "screen_A",
            R.drawable.chatbot,
        ),

        AppInfo(
            "Image And Text", "A fun and interactive app that combines images with text," +
                    " creating an engaging experience." +
                    " Perfect for enhancing your creativity during free time!", "screen_B",
            R.drawable.itt,
        ),

        AppInfo(
            "Text-To-Text", "A simple yet powerful app that transforms" +
                    " text-based inputs into useful outputs. From Current Affairs" +
                    " to quick information, it’s your go-to tool for instant" +
                    " text-based responses!", "screen_C",
            R.drawable.txt,
        )
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(apps.size){
            AppCard(
                title =  apps[it].title,
                description = apps[it].description,
                navController = navController,
                targetScreen = apps[it].targetScreen,
                imageResId = apps[it].imageResId
            )
        }

    }
}

@Composable
fun AppCard(
    title : String,
    description: String,
    navController: NavController,
    targetScreen : String,
    imageResId : Int,
) {
    var expanded by remember { mutableStateOf(false) }
    Card (
        modifier = Modifier
            .padding(9.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "App Image",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (expanded) description else "${description.take(50)}...",
                style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {

                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        tint = Color.Gray
                    )
                }

                IconButton(
                    onClick = { navController.navigate(targetScreen) },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to $title",
                        tint = Color.Black
                    )
                }
            }
        }
    }

}


