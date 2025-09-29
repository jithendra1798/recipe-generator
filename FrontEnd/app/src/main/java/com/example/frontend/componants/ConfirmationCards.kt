import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A card component that presents a question and two action buttons.
 *
 * @param modifier The modifier to be applied to the card.
 * @param title The text to display as the primary question or statement.
 * @param onYesClick The lambda to be executed when the "Yes" button is clicked.
 * @param onNoClick The lambda to be executed when the "No" button is clicked.
 */
@Composable
fun ConfirmationCards(
    modifier: Modifier = Modifier,
    title: String,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // The main text or question
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Row for the action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // "No" button is often styled as a secondary action
                OutlinedButton(
                    onClick = onNoClick,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("No")
                }

                // "Yes" button is the primary action
                Button(
                    onClick = onYesClick,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Yes")
                }
            }
        }
    }
}