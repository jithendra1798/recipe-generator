package com.example.frontend.componants

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

@Composable
fun HistoryScreen(name: String, modifier: Modifier = Modifier) {
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFF7900) // orange background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 24.dp)
            )
            Text(
                text = "total",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 12.dp)
            )

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { items.add("Added Item ${items.size + 1}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A482A),
                        contentColor = Color.White
                    )
                ) { Text("+") }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A482A),
                        contentColor = Color.White
                    )
                ) { Text("Today") }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A482A),
                        contentColor = Color.White
                    )
                ) { Text("Past") }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A482A),
                        contentColor = Color.White
                    )
                ) { Text("Like") }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A482A),
                        contentColor = Color.White
                    )
                ) { Text("Pass") }
            }
        }
    }
}

@Composable
fun ListItem(text: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6A482A)) // Brown card
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
fun HistoryScreenPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        HistoryScreen(name = previewDate, modifier = Modifier.fillMaxSize())
    }
}