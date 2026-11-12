
package ua.edu.mobile.smartlife.ui.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType

private val BackgroundColor = Color(0xFFFFF7FC)
private val CardColor = Color(0xFFF3EFF7)
private val AccentColor = Color(0xFFF600BE)
private val TextColor = Color(0xFF29252D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailsScreen(
    record: HealthRecord,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val recordType = when (record.type) {
        RecordType.WATER -> "Вода"
        RecordType.PULSE -> "Пульс"
        RecordType.SLEEP -> "Сон"
        RecordType.WEIGHT -> "Вага"
        RecordType.MOOD -> "Настрій"
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Деталі запису",
                        color = TextColor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CardColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = recordType,
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Номер запису: ${record.id}",
                        color = TextColor
                    )
                    Text(
                        text = "Значення: ${record.value}",
                        color = TextColor
                    )
                    Text(
                        text = "Примітка: ${record.note}",
                        color = TextColor
                    )
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "$recordType: ${record.value}. ${record.note}"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentColor,
                    contentColor = Color.White
                )
            ) {
                Text("Поділитися")
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentColor,
                    contentColor = Color.White
                )
            ) {
                Text("Назад")
            }
        }
    }
}
