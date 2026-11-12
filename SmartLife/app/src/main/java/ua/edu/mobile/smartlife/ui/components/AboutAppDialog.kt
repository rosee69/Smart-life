package ua.edu.mobile.smartlife.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Про застосунок")
        },
        text = {
            Text(
                "SmartLife — застосунок для ведення журналу " +
                        "показників здоров'я та відстеження особистих даних."
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Зрозуміло")
            }
        }
    )
}