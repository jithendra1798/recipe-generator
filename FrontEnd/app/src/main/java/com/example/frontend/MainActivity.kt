package com.example.frontend

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFffc9f1) // light pink background
                ) { innerPadding ->
                    val currentDate = LocalDate.now().format(
                        DateTimeFormatter.ofPattern("MMMM d, yyyy")
                    )

                    Greeting(
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val items = remember {
        mutableStateListOf(
            "Scrollable Item 1", "Scrollable Item 2", "Scrollable Item 3",
            "Scrollable Item 4", "Scrollable Item 5", "Scrollable Item 6",
            "Scrollable Item 7", "Scrollable Item 8", "Scrollable Item 9",
            "Scrollable Item 10", "Scrollable Item 11", "Scrollable Item 12",
            "Scrollable Item 13", "Scrollable Item 14", "Scrollable Item 15",
            "Scrollable Item 16", "Scrollable Item 17", "Scrollable Item 18",
            "Scrollable Item 19", "Scrollable Item 20",
        )
    }

    // The main Column now arranges the major sections of the screen.
    // The `verticalScroll` has been removed to prevent nested scrolling issues.
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Top Section ---
        Text(
            text = name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        )
        Text(
            text = "Verify",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4A4A),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- Middle (List) Section ---
        // LazyColumn now uses weight(1f) to fill all available space.
        // This pushes the buttons to the bottom correctly.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = items, key = { it }) { item ->
                ListItem(
                    text = item,
                    onDelete = { items.remove(item) }
                )
            }
        }

        // --- Bottom (Buttons) Section ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* TODO: Handle 'No' action */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Red
                ),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) { Text("N") }
            Button(
                onClick = { /* TODO: Handle 'Yes' action */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) { Text("Y") }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("+") }
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Today") }
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Past") }
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Like") }
            Button(onClick = { /* TODO */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Pass") }
        }
    }
}

@Composable
fun ListItem(text: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Red)
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
                modifier = Modifier.weight(1f)
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
fun GreetingPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        Surface(color = Color(0xFFffc9f1)) {
            Greeting(name = previewDate, modifier = Modifier.fillMaxSize())
        }
    }
}