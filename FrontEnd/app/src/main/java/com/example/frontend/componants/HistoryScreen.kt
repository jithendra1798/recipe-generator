package com.example.frontend.componants

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.FrontEndTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

// Data class to hold item information
data class FoodItem(
    val id: UUID = UUID.randomUUID(), // Unique ID for stable list operations
    val title: String,
    val calories: Int
)

@Composable
fun HistoryScreen(name: String, modifier: Modifier = Modifier) {
    // State now holds a list of FoodItem objects
    val items = remember {
        mutableStateListOf(
            FoodItem(title = "Apple", calories = 95),
            FoodItem(title = "Banana", calories = 105),
            FoodItem(title = "Chicken Breast (100g)", calories = 165),
            FoodItem(title = "Mixed Salad", calories = 80),
            FoodItem(title = "Protein Shake", calories = 250),
        )
    }

    // State to manage which item is being edited. Null means no dialog is shown.
    var itemToEdit by remember { mutableStateOf<FoodItem?>(null) }

    // Automatically calculate the total calories whenever the list changes
    val totalCalories = remember(items) { items.sumOf { it.calories } }


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
            // Displays the date passed into the screen
            Text(
                text = "Sous",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 24.dp)
            )
            // Displays the dynamic total calorie count
            Text(
                text = "Total: $totalCalories cal",
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
                // Use the item's unique id as the key
                items(items = items, key = { it.id }) { item ->
                    ListItem(
                        item = item,
                        onDelete = { items.remove(item) },
                        // Set the item to be edited, which triggers the dialog
                        onEdit = { itemToEdit = item }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Add button now creates a default FoodItem
                Button(
                    onClick = {  },
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
                ) { Text("History") }

                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A482A),
                        contentColor = Color.White
                    )
                ) { Text("\uD83D\uDC4D") } // 👍

                Button(
                    onClick = { /* TODO */ },
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

        // This block will execute when 'itemToEdit' is not null, showing the dialog
        itemToEdit?.let { currentItem ->
            EditItemDialog(
                item = currentItem,
                onDismiss = { itemToEdit = null }, // Hide dialog on dismiss
                onSave = { newTitle, newCalories ->
                    // Find the original item and update it
                    val index = items.indexOf(currentItem)
                    if (index != -1) {
                        items[index] = currentItem.copy(title = newTitle, calories = newCalories)
                    }
                    itemToEdit = null // Hide dialog after saving
                }
            )
        }
    }
}

/**
 * A composable for displaying a single food item.
 * It now accepts a FoodItem object and an onEdit callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(item: FoodItem, onDelete: () -> Unit, onEdit: () -> Unit) {
    // The Card is now clickable to trigger the edit action
    Card(
        onClick = onEdit,
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
            // Column to display title and calories vertically
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${item.calories} cal",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
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

/**
 * An AlertDialog to edit the title and calories of a FoodItem.
 */
@Composable
fun EditItemDialog(
    item: FoodItem,
    onDismiss: () -> Unit,
    onSave: (String, Int) -> Unit
) {
    // State for the text fields within the dialog
    var title by remember { mutableStateOf(item.title) }
    var calories by remember { mutableStateOf(item.calories.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Food Name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                // Safely convert calories to Int, defaulting to 0 if input is invalid
                val calValue = calories.toIntOrNull() ?: 0
                onSave(title, calValue)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        HistoryScreen(name = previewDate, modifier = Modifier.fillMaxSize())
    }
}