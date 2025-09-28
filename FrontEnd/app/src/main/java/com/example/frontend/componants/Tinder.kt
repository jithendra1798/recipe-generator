package com.example.frontend.componants

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.FrontEndTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp



class Tinder : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFffc9f1) // orange background
                ) { innerPadding ->
                    val currentDate = LocalDate.now().format(
                        DateTimeFormatter.ofPattern("MMMM d, yyyy")
                    )

                    Tinder(
                        name = currentDate,
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
fun Tinder(name: String, modifier: Modifier = Modifier) {
    // State for our dynamic list of items.


    // Main layout Column
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Add this if the content might exceed screen height
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Text(
            text = "Recipies",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4A4A),
            modifier = Modifier.padding(bottom = 16.dp)
        )



        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .offset(30.dp, 10.dp) // position
                    .size(width = 350.dp, height = 550.dp)
                    .background(Color.Blue) // rectangle color
                    .padding(16.dp), // space inside rectangle
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }







        Column(
            modifier = Modifier
                .weight(1f) // Allow this Column to take available space
                .padding(horizontal = 16.dp)
        ) {

        }



        Column(
            modifier = Modifier
                .weight(1f) // Allow this Column to take available space
                .padding(horizontal = 16.dp)
        ) {

        }










        // Yes/No Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Red
                )
            ) { Text("N") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                )) { Text("Y") }

        }

        // Start of Bottom Comps
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) { Text("+") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )) { Text("Today") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )) { Text("Past") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )) { Text("Like") }
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )) { Text("Pass") }
        }
        // End of Bottom Comps
    }
}


@Composable
fun TinderListItem(text: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Red) // Red card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f) // Ensure text doesn't push the button away
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = Color.White
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun TinderPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        // We wrap the preview in a surface to set a background color for better visibility
        Surface(color = Color(0xFFFF7900)) {
            Tinder(name = previewDate, modifier = Modifier.fillMaxSize())
        }
    }
}