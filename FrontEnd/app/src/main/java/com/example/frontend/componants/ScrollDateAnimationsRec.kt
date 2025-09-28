package com.example.frontend.componants

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
                    containerColor = Color(0xFFffc9f1) // light pink background
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
    // State for the original dynamic list
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

    // --- KEY CHANGE 1: State to control visibility ---
    // This boolean will decide whether to show the updatable list or not.
    var showUpdatableList by remember { mutableStateOf(false) }

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

        // --- KEY CHANGE 2: Conditional UI block ---
        // This Box takes up the flexible space and shows one of the two lists.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Keep the weight to ensure the bottom bar stays at the bottom
        ) {
            if (showUpdatableList) {
                // If true, show the new updatable list component
                UpdatableListScreen(modifier = Modifier.padding(horizontal = 16.dp))
            } else {
                // If false, show the original simple scrolling list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = items, key = { it }) { item ->
                        ScrollDateAnimationRecListItem(
                            text = item,
                            onDelete = { items.remove(item) }
                        )
                    }
                }
            }
        }

        // Bottom Section: Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { items.add("Added Item ${items.size + 1}") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("+") }
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Today") }
            // --- KEY CHANGE 3: The "Past" button now toggles the list visibility ---
            Button(
                onClick = { showUpdatableList = !showUpdatableList },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Past") }
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Like") }
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Pass") }
        }
    }
}

@Composable
fun ScrollDateAnimationRecListItem(text: String, onDelete: () -> Unit) {
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
fun ScrollDateAnimationRecPreview() {
    FrontEndTheme {
        val previewDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
        Surface(color = Color(0xFFffc9f1)) {
            ScrollDateAnimationRec(name = previewDate, modifier = Modifier.fillMaxSize())
        }
    }
}


// --- KEY CHANGE 4: Added the updatable list components from the previous example ---

/**
 * A fully interactive and updatable list screen.
 */
@Composable
fun UpdatableListScreen(modifier: Modifier = Modifier) {
    val items = remember {
        mutableStateListOf("Buy milk", "Walk the dog", "Write code")
    }
    var newItemText by remember { mutableStateOf("") }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var editingText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newItemText,
                onValueChange = { newItemText = it },
                label = { Text("New To-Do Item") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newItemText.isNotBlank()) {
                        items.add(newItemText)
                        newItemText = ""
                    }
                },
                enabled = newItemText.isNotBlank()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items, key = { _, item -> item.hashCode() }) { index, item ->
                EditableListItem(
                    text = item,
                    isEditing = (editingIndex == index),
                    editingText = editingText,
                    onEditingTextChange = { editingText = it },
                    onEdit = {
                        editingIndex = index
                        editingText = item
                    },
                    onSave = {
                        items[index] = editingText
                        editingIndex = null
                    },
                    onDelete = {
                        items.removeAt(index)
                    }
                )
            }
        }
    }
}

/**
 * A single list item that can be in either "display" or "edit" mode.
 */
@Composable
fun EditableListItem(
    text: String,
    isEditing: Boolean,
    editingText: String,
    onEditingTextChange: (String) -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onSave()
                        focusManager.clearFocus()
                    }),
                    singleLine = true
                )
                IconButton(onClick = {
                    onSave()
                    focusManager.clearFocus()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Save Item")
                }
            } else {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onEdit),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Item")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}