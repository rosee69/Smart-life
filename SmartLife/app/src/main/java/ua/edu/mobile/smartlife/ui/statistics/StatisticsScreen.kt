
package ua.edu.mobile.smartlife.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.edu.mobile.smartlife.data.model.FakeData
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType

private val BackgroundColor = Color(0xFFFFF7FC)
private val CardColor = Color(0xFFF3EFF7)
private val AccentColor = Color(0xFFF600BE)
private val IconBackground = Color(0xFFFCE0F4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    records: List<HealthRecord> = FakeData.records
) {
    val statistics = listOf(
        RecordType.WATER to "Вода",
        RecordType.PULSE to "Пульс",
        RecordType.SLEEP to "Сон",
        RecordType.WEIGHT to "Вага",
        RecordType.MOOD to "Настрій"
    )

    val totalRecords = records.size

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("Статистика")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentColor
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Усього записів",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = totalRecords.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Ваші показники Smart Life",
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Показники за типами",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    statistics.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { (type, name) ->
                                val typeRecords = records.filter {
                                    it.type == type
                                }

                                val icon = when (type) {
                                    RecordType.WATER -> Icons.Default.WaterDrop
                                    RecordType.PULSE -> Icons.Default.Favorite
                                    RecordType.SLEEP -> Icons.Default.Bedtime
                                    RecordType.WEIGHT -> Icons.Default.FitnessCenter
                                    RecordType.MOOD -> Icons.Default.SentimentSatisfied
                                }

                                StatisticsCard(
                                    name = name,
                                    icon = icon,
                                    count = typeRecords.size,
                                    average = if (typeRecords.isNotEmpty()) {
                                        typeRecords.map { it.value }.average()
                                    } else {
                                        null
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    name: String,
    icon: ImageVector,
    count: Int,
    average: Double?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = IconBackground,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = AccentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Записів",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AccentColor
            )

            Text(
                text = average?.let {
                    "Середнє: ${String.format("%.1f", it)}"
                } ?: "Поки немає даних",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}
