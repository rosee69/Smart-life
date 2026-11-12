
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

private val AccentColor = Color(0xFFF600BE)
private val CardColor = Color(0xFFF1EDF7)
private val ScreenColor = Color(0xFFFFF7FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    records: List<HealthRecord> = FakeData.records,
    onRecordClick: (Long) -> Unit = {},
    onAddRecord: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ScreenColor,
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
                    containerColor = ScreenColor
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
                        text = "Вітаю, Вікторія!",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = "Вода",
                        value = "0.5 л",
                        icon = Icons.Default.WaterDrop,
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Пульс",
                        value = "84",
                        icon = Icons.Default.Favorite,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = "Сон",
                        value = "7.5 год",
                        icon = Icons.Default.Bedtime,
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Вага",
                        value = "55.2 кг",
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
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
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
            containerColor = CardColor
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
                    color = Color.Gray
                )
                Text(
                    text = record.note,
                    fontSize = 10.sp,
                    color = Color.Gray
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
