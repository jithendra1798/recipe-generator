package com.example.frontend.componants

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.util.UUID

/**
 * A data class to represent a recipe with a title, calorie count, and steps.
 */
data class RecipeItem(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val calories: Int,
    val steps: String
)

@Composable
fun RecommendationsScreenRej(modifier: Modifier = Modifier) {
    // The state now holds a list of RecipeItem objects.
    val items = remember {
        mutableStateListOf(
            RecipeItem(
                title = "Grilled Chicken Salad",
                calories = 350,
                steps = "1. Season 4oz chicken breast with salt and pepper.\n" +
                        "2. Grill for 6-8 minutes per side until cooked through.\n" +
                        "3. Chop romaine lettuce, cherry tomatoes, and cucumber.\n" +
                        "4. Slice the chicken and combine with vegetables."
            ),
            RecipeItem(
                title = "Simple Salmon & Asparagus",
                calories = 450,
                steps = "1. Preheat oven to 400°F (200°C).\n" +
                        "2. Place a 6oz salmon fillet and a handful of asparagus on a baking sheet.\n" +
                        "3. Drizzle with olive oil, lemon juice, salt, and pepper.\n" +
                        "4. Bake for 12-15 minutes."
            ),
            RecipeItem(
                title = "Oatmeal with Berries",
                calories = 250,
                steps = "1. Combine 1/2 cup of rolled oats with 1 cup of water or milk.\n" +
                        "2. Cook on medium heat for 5 minutes, stirring occasionally.\n" +
                        "3. Top with a handful of mixed berries and a drizzle of honey."
            )
        )
    }

    // This state holds the ID of the currently expanded item. Null means no item is expanded.
    var expandedItemId by remember { mutableStateOf<UUID?>(null) }

    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFffc9f1) // light pink background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Passed Recs",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = items, key = { it.id }) { item ->
                    RecipeListItem(
                        item = item,
                        // The item is expanded if its ID matches the one in our state.
                        isExpanded = expandedItemId == item.id,
                        // When an item is clicked...
                        onClick = {
                            // ...if it's already expanded, collapse it (set state to null).
                            // Otherwise, expand it (set state to this item's ID).
                            expandedItemId = if (expandedItemId == item.id) null else item.id
                        }
                    )
                }
            }

            // Bottom control buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) { Text("+") }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) { Text("History") }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) { Text("\uD83D\uDC4D") } // 👍
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) { Text("\uD83D\uDC4E") } // 👎
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) { Text("\uD83D\uDC4B") } // 👋
            }
        }
    }
}

/**
 * A composable for displaying a single recipe item that can be expanded.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListItem(
    item: RecipeItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            // This modifier animates the size change when the content expands or collapses.
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.Red)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Title and Calories are always visible
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${item.calories} cal",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Steps are only visible when the item is expanded.
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.steps,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecommendationsScreenRejPreview() {
    FrontEndTheme {
        RecommendationsScreenRej(modifier = Modifier.fillMaxSize())
    }
}