package com.example.frontend.componants

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.FrontEndTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// region: AddPhotoActivity Code
class AddPhotoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFFF7900) // orange background
                ) { innerPadding ->
                    GreetingAddPhoto(
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
fun GreetingAddPhoto(modifier: Modifier = Modifier) {
    // Get the current context to start a new activity
    val context = LocalContext.current

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Upload a Photo",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Upload placeholder image",
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // --- MODIFIED BUTTON ---
        // This button now launches the Verify activity
        Button(
            onClick = {
                val intent = Intent(context, Verify::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A482A), // brown button background
                contentColor = Color.White          // text/icon color
            )
        ) {
            Text("Open Library")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingAddPhotoPreview() {
    FrontEndTheme {
        GreetingAddPhoto(modifier = Modifier.fillMaxSize())
    }
}
// endregion

// region: VerifyActivity Code
class Verify : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFffc9f1) // light pink background
                ) { innerPadding ->
                    VerifyScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun VerifyScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    val items = remember {
        mutableStateListOf("Click me to edit", "Swipe to delete", "Add more items below")
    }

    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var editingText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Verify",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4A4A),
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items, key = { index, item -> "$index-$item" }) { index, item ->
                EditableListItem(
                    text = item,
                    isEditing = editingIndex == index,
                    editingText = editingText,
                    onEditingTextChange = { editingText = it },
                    onEdit = {
                        editingIndex = index
                        editingText = item
                    },
                    onSave = {
                        if (index < items.size) {
                            items[index] = editingText
                        }
                        editingIndex = null
                    },
                    onDelete = { items.remove(item) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onNavigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) { Text("N") }

            Button(
                onClick = { items.add("New Item ${items.size + 1}") },
                modifier = Modifier.weight(1f)
            ) { Text("Add") }

            Button(
                onClick = onNavigateBack,
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
            kotlinx.coroutines.delay(100)
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
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onEdit),
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
fun VerifyPreview() {
    FrontEndTheme {
        Surface(color = Color(0xFFffc9f1)) {
            VerifyScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = {}
            )
        }
    }
}
// endregion