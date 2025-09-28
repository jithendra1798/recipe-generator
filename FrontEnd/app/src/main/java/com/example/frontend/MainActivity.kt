package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
        mutableStateListOf("Click me to edit", "Swipe to delete", "Add more items below")
    }

    // --- State to manage editing ---
    // Tracks the index of the item being edited. null means no item is in edit mode.
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    // Holds the text of the item while it's being edited.
    var editingText by remember { mutableStateOf("") }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Use itemsIndexed to get the index of each item
            itemsIndexed(items, key = { index, item -> "$index-$item" }) { index, item ->
                EditableListItem(
                    text = item,
                    isEditing = editingIndex == index,
                    editingText = editingText,
                    onEditingTextChange = { editingText = it },
                    onEdit = {
                        editingIndex = index
                        editingText = item // Pre-fill with current text
                    },
                    onSave = {
                        items[index] = editingText
                        editingIndex = null // Exit edit mode
                    },
                    onDelete = { items.remove(item) }
                )
            }
        }

        // --- Bottom (Buttons) Section ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* TODO: Handle 'No' action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) { Text("N") }

            // New "Add" button
            Button(
                onClick = { items.add("New Item ${items.size + 1}") },
                modifier = Modifier.weight(1f)
            ) { Text("Add") }

            Button(
                onClick = { /* TODO: Handle 'Yes' action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green, contentColor = Color.White),
                modifier = Modifier.weight(1f)
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

/**
 * An item that can be in "display" or "edit" mode.
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

    // Automatically request focus when entering edit mode.
    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Red)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                // --- Edit Mode ---
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onSave()
                        focusManager.clearFocus() // Hide keyboard
                    }),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                IconButton(onClick = {
                    onSave()
                    focusManager.clearFocus()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Save Item", tint = Color.White)
                }
            } else {
                // --- Display Mode ---
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onEdit), // Tap text to edit
                    color = Color.White,
                    fontSize = 16.sp,
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = Color.White)
                }
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