package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.frontend.componants.HistoryScreen
import com.example.frontend.componants.RecommendationsScreenRej // Import the new screen
import com.example.frontend.componants.VerifyScreen
import com.example.frontend.ui.theme.FrontEndTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { GreetingScreen(navController) }
        composable("verify") {
            VerifyScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("history") {
            val currentDate = LocalDate.now().format(
                DateTimeFormatter.ofPattern("MMMM d, yyyy")
            )
            HistoryScreen(name = currentDate)
        }
        // CHANGE #1: Add the route for the "rejected" recommendations screen
        composable("recommendations_rejected") {
            RecommendationsScreenRej()
        }
    }
}

@Composable
fun GreetingScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFF7900) // orange background
    ) { innerPadding ->
        Greeting(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController
        )
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, navController: NavController) {
    val offsetX = remember { Animatable(-500f) }

    LaunchedEffect(Unit) {
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Hello Sous!",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(x = offsetX.value.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("verify") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A),
                    contentColor = Color.White
                )
            ) { Text("+") }

            Button(
                onClick = { navController.navigate("history") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A),
                    contentColor = Color.White
                )
            ) { Text("History") }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A),
                    contentColor = Color.White
                )
            ) { Text("\uD83D\uDC4D") } // 👍

            // CHANGE #2: Make the "Thumbs Down" button navigate to the new screen
            Button(
                onClick = { navController.navigate("recommendations_rejected") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A),
                    contentColor = Color.White
                )
            ) { Text("\uD83D\uDC4E") } // 👎

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A),
                    contentColor = Color.White
                )
            ) { Text("\uD83D\uDC4B") } // 👋
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontEndTheme {
        Greeting(
            modifier = Modifier.fillMaxSize(),
            navController = rememberNavController()
        )
    }
}