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
import com.example.frontend.R
import com.example.frontend.ui.theme.FrontEndTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddIngrediantRec : ComponentActivity() {
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

                    AddIngrediantRec(
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
fun AddIngrediantRec(name: String, modifier: Modifier = Modifier) {
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
            text = "Your Photos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4A4A),
            modifier = Modifier.padding(bottom = 16.dp)
        )







        val imageResources = remember {
            List(6) { R.drawable.images } // Replace R.drawable.images with your actual image resources
        }

        // We group the list of 6 images into chunks of 3 for a 2x3 grid
        val imageRows = imageResources.chunked(3) // <-- Changed back to 3

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(70.dp) // Space between the two rows of image/button pairs
        ) {
            imageRows.forEach { rowImages ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between individual image/button pairs in a row
                ) {
                    rowImages.forEach { imageResId ->
                        // Each image and button pair is wrapped in its own Column
                        Column(
                            modifier = Modifier.weight(1f), // Distributes space evenly in the Row
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp) // Space between image and button
                        ) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = "User uploaded photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth() // Fill width of its parent Column
                                    .aspectRatio(1f) // Keep the image square
                            )
                            Button(
                                onClick = { /* TODO: Implement logic for opening library for this specific image slot */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red, // Red button background
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth() // Button fills the width of its parent Column
                            ) {
                                Text("Open Library", fontSize = 12.sp) // Smaller text for buttons in grid
                            }
                        }
                    }
                }
            }
        }



        // Display items directly in the Column
        // This section replaces the LazyColumn
        Column(
            modifier = Modifier
                .weight(1f) // Allow this Column to take available space
                .padding(horizontal = 16.dp)
        ) {

        }

        // 3. Bottom Section: Control Buttons
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
    }
}

/**
 * A reusable composable for displaying a single row in our list.
 * It contains the item text and a delete button.
 */
@Composable
fun AddIngrediantRecListItem(text: String, onDelete: () -> Unit) {
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
fun AddIngrediantRecPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        // We wrap the preview in a surface to set a background color for better visibility
        Surface(color = Color(0xFFFF7900)) {
            AddIngrediantRec(name = previewDate, modifier = Modifier.fillMaxSize())
        }
    }
}