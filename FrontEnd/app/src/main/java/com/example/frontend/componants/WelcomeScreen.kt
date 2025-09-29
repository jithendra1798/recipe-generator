package com.example.frontend.componants

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import com.example.frontend.Greeting
import com.example.frontend.ui.theme.FrontEndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFFF7900) // orange background
                ) { innerPadding ->
                    Greeting(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    // Animation state
    val offsetX = remember { Animatable(-500f) }

    LaunchedEffect(Unit) {
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
    }

    // Whole screen layout
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween, // push text to center, buttons to bottom
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer to push content down
        Spacer(modifier = Modifier.weight(1f))

        // Centered Text
        Text(
            text = "Hello Sous!",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(x = offsetX.value.dp)
        )

        // Spacer to separate text and buttons
        Spacer(modifier = Modifier.weight(1f))

        // Row for buttons at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
//                .background(color = Color(color = 0xFF6A482A)),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A482A), // brown button background
                contentColor = Color.White          // text/icon color
            )) { Text("Current") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A), // brown button background
                    contentColor = Color.White          // text/icon color
                )) { Text("History") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A), // brown button background
                    contentColor = Color.White          // text/icon color
                )) { Text("Liked") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A482A), // brown button background
                    contentColor = Color.White          // text/icon color
                )) { Text("Passed") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontEndTheme {
        Greeting(modifier = Modifier.fillMaxSize())
    }
}
