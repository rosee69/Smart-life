
package ua.edu.mobile.smartlife.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType

private val accentColor = Color(0xFFF600BE)
private val cardColor = Color(0xFFF3EFF7)
private val iconBackground = Color(0xFFF0B8EA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    records: List<HealthRecord>,
    selectedType: RecordType?,
    onFilterChange: (RecordType?) -> Unit,
    onRecordClick: (Long) -> Unit,
    onAddRecord: () -> Unit,
    onDeleteRecord: (Long) -> Unit,
    onUndoDelete: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color(0xFFFFF7FC),

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        topBar = {
            TopAppBar(
                title = {
                    Text("Журнал показників")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF7FC)
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecord,
                containerColor = Color(0xFFF0B8EA),
                contentColor = accentColor
            ) {
                Text("+", fontSize = 26.sp)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Усі") }
                )

                FilterChip(
                    selected = selectedType == RecordType.WATER,
                    onClick = { onFilterChange(RecordType.WATER) },
                    label = { Text("Вода") }
                )

                FilterChip(
                    selected = selectedType == RecordType.PULSE,
                    onClick = { onFilterChange(RecordType.PULSE) },
                    label = { Text("Пульс") }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedType == RecordType.SLEEP,
                    onClick = { onFilterChange(RecordType.SLEEP) },
                    label = { Text("Сон") }
                )

                FilterChip(
                    selected = selectedType == RecordType.WEIGHT,
                    onClick = { onFilterChange(RecordType.WEIGHT) },
                    label = { Text("Вага") }
                )

                FilterChip(
                    selected = selectedType == RecordType.MOOD,
                    onClick = { onFilterChange(RecordType.MOOD) },
                    label = { Text("Настрій") }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = records,
                    key = { it.id }
                ) { record ->

                    RecordCard(
                        record = record,
                        onClick = {
                            onRecordClick(record.id)
                        },
                        onDelete = {
                            onDeleteRecord(record.id)

                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Запис видалено",
                                    actionLabel = "Скасувати",
                                    withDismissAction = true
                                )

                                if (result == SnackbarResult.ActionPerformed) {
                                    onUndoDelete()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordCard(
    record: HealthRecord,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val title = when (record.type) {
        RecordType.WATER -> "Вода"
        RecordType.PULSE -> "Пульс"
        RecordType.SLEEP -> "Сон"
        RecordType.WEIGHT -> "Вага"
        RecordType.MOOD -> "Настрій"
    }

    val icon: ImageVector = when (record.type) {
        RecordType.WATER -> Icons.Default.WaterDrop
        RecordType.PULSE -> Icons.Default.Favorite
        RecordType.SLEEP -> Icons.Default.Bedtime
        RecordType.WEIGHT -> Icons.Default.FitnessCenter
        RecordType.MOOD -> Icons.Default.SentimentSatisfied
    }

    val valueText = when (record.type) {
        RecordType.WATER -> "${record.value} л"
        RecordType.PULSE -> "${record.value.toInt()} уд/хв"
        RecordType.SLEEP -> "${record.value} год"
        RecordType.WEIGHT -> "${record.value} кг"
        RecordType.MOOD -> "Настрій"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(iconBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = record.dateTime,
                    fontSize = 9.sp,
                    color = Color.Gray
                )

                Text(
                    text = record.note,
                    fontSize = 10.sp,
                    color = Color.DarkGray
                )
            }

            Text(
                text = valueText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Видалити запис",
                    tint = Color.Gray
                )
            }
        }
    }
}
