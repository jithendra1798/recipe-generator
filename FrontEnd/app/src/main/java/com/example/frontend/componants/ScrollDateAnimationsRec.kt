package com.example.frontend.componants

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.FrontEndTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScrollDateAnimationRec : ComponentActivity() {
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

                    ScrollDateAnimationRec(
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
fun ScrollDateAnimationRec(name: String, modifier: Modifier = Modifier) {
    // State for our dynamic list of items. We start with 3 items.
    // Compose will "remember" this list across recompositions.
    val items = remember {
        mutableStateListOf(
            "Scrollable Item 1",
            "Scrollable Item 2",
            "Scrollable Item 3",
            "Scrollable Item 4",
            "Scrollable Item 5",
            "Scrollable Item 6",
            "Scrollable Item 7",
            "Scrollable Item 8",
            "Scrollable Item 9",
            "Scrollable Item 10",
            "Scrollable Item 11",
            "Scrollable Item 12",
            "Scrollable Item 13",
            "Scrollable Item 14",
            "Scrollable Item 15",
            "Scrollable Item 16",
            "Scrollable Item 17",
            "Scrollable Item 18",
            "Scrollable Item 19",
            "Scrollable Item 20",


        )
    }

    // Main layout Column
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(vertical = 24.dp)
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // <-- This makes the area flexible and scrollable
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adds space between items
        ) {
            items(items = items, key = { it }) { item ->
                ScrollDateAnimationRecListItem(
                    text = item,
                    onDelete = { items.remove(item) } // Lambda to remove this specific item
                )
            }
        }

        // 3. Bottom Section: Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // This button now adds a new item to our list
            Button(
                onClick = { items.add("Added Item ${items.size + 1}") },
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
fun ScrollDateAnimationRecListItem(text: String, onDelete: () -> Unit) {
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
fun ScrollDateAnimationRecPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        // We wrap the preview in a surface to set a background color for better visibility
        Surface(color = Color(0xFFFF7900)) {
            ScrollDateAnimationRec(name = previewDate, modifier = Modifier.fillMaxSize())
        }
    }
}