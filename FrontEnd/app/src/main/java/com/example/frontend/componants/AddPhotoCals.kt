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
import com.example.frontend.ui.theme.FrontEndTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.frontend.R

class AddPhotoActivit : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFFF7900) // orange background
                ) { innerPadding ->
                    GreetingAddPhotos(
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
fun GreetingAddPhotos(modifier: Modifier = Modifier) {
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
        Text(
            text = "Upload a Photo",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
//            modifier = Modifier.offset(x = offsetX.value.dp)
        )
        // Centered Text
        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Description of the image for accessibility",
        )

        Button(onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A482A), // brown button background
                contentColor = Color.White          // text/icon color
            )) { Text("Open Library") }

        }
    }


@Preview(showBackground = true)
@Composable
fun GreetingAddPhotosPreview() {
    FrontEndTheme {
        GreetingAddPhoto(modifier = Modifier.fillMaxSize())
    }
}
