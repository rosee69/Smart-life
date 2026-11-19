
package ua.edu.mobile.smartlife.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import ua.edu.mobile.smartlife.data.model.FakeData
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType
import ua.edu.mobile.smartlife.ui.components.AboutAppDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.shape.CircleShape

private val AccentColor = Color(0xFFF600BE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    weatherState: WeatherUiState = WeatherUiState.Loading,
    records: List<HealthRecord> = FakeData.records,
    userName: String = "",
    dailyWaterGoal: Int = 2000,
    onRecordClick: (Long) -> Unit = {},
    onAddRecord: () -> Unit = {},
    onAddWaterGlass: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    val today = remember {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
    }

    val todayRecords = records.filter {
        it.dateTime.startsWith(today)
    }

    val waterAmount = todayRecords
        .filter { it.type == RecordType.WATER }
        .sumOf { it.value }

    val latestPulse = records
        .filter { it.type == RecordType.PULSE }
        .maxByOrNull { it.id }

    val latestSleep = records
        .filter { it.type == RecordType.SLEEP }
        .maxByOrNull { it.id }

    val latestWeight = records
        .filter { it.type == RecordType.WEIGHT }
        .maxByOrNull { it.id }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Smart Life")
                },
                actions = {
                    IconButton(
                        onClick = { menuExpanded = true }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Меню"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Про застосунок") },
                            onClick = {
                                menuExpanded = false
                                showAboutDialog = true
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },

    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column {
                    Text(
                        text = if (userName.isBlank()) {
                            "Вітаю!"
                        } else {
                            "Вітаю, $userName!"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Ваші показники за сьогодні",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            item {
                WeatherCard(state = weatherState)
            }

            item {
                val progress = if (dailyWaterGoal > 0) {
                    (waterAmount / (dailyWaterGoal / 1000.0))
                        .toFloat()
                        .coerceIn(0f, 1f)
                } else {
                    0f
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = AccentColor
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Вода: ${"%.2f".format(waterAmount)} з ${"%.2f".format(dailyWaterGoal / 1000.0)} л",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = AccentColor,
                            trackColor = Color(0xFFF0B8EA)
                        )

                        Button(
                            onClick = onAddWaterGlass,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF0B8EA),
                                contentColor = AccentColor
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Склянка води (0.25 л)")
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = "Пульс",
                        value = latestPulse?.let { "${it.value.toInt()} уд/хв" } ?: "—",
                        icon = Icons.Default.Favorite,
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Сон",
                        value = latestSleep?.let { "${it.value} год" } ?: "—",
                        icon = Icons.Default.Bedtime,
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Вага",
                        value = latestWeight?.let { "${it.value} кг" } ?: "—",
                        icon = Icons.Default.MonitorWeight,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Останні записи",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Усі записи",
                        color = AccentColor,
                        fontSize = 12.sp
                    )
                }
            }

            items(
                records.take(3),
                key = { it.id }
            ) { record ->
                RecordCard(
                    record = record,
                    onClick = {
                        onRecordClick(record.id)
                    }
                )
            }
        }
    }

    if (showAboutDialog) {
        AboutAppDialog(
            onDismiss = {
                showAboutDialog = false
            }
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.heightIn(min = 105.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = Color(0xFFF0B8EA)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AccentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RecordCard(
    record: HealthRecord,
    onClick: () -> Unit
) {
    val title = when (record.type) {
        RecordType.WATER -> "Вода"
        RecordType.PULSE -> "Пульс"
        RecordType.SLEEP -> "Сон"
        RecordType.WEIGHT -> "Вага"
        RecordType.MOOD -> "Настрій"
    }

    val icon = when (record.type) {
        RecordType.WATER -> Icons.Default.WaterDrop
        RecordType.PULSE -> Icons.Default.Favorite
        RecordType.SLEEP -> Icons.Default.Bedtime
        RecordType.WEIGHT -> Icons.Default.MonitorWeight
        RecordType.MOOD -> Icons.Default.SentimentSatisfied
    }

    val value = when (record.type) {
        RecordType.WATER -> "${record.value} л"
        RecordType.PULSE -> "${record.value.toInt()} уд/хв"
        RecordType.SLEEP -> "${record.value} год"
        RecordType.WEIGHT -> "${record.value} кг"
        RecordType.MOOD -> "Настрій"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(50),
                color = Color(0xFFF0B8EA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AccentColor,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = record.dateTime,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.note,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
