
package ua.edu.mobile.smartlife.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.edu.mobile.smartlife.data.model.FakeData
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType
import androidx.compose.material.icons.filled.SentimentSatisfied

private val accentColor = Color(0xFFF600BE)
private val cardColor = Color(0xFFE8E5EE)
private val iconBackground = Color(0xFFF0B8EA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    records: List<HealthRecord> = FakeData.records,
    onRecordClick: (Long) -> Unit = {},
    onAddRecord: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFFFF7FC),
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
                contentColor = Color(0xFFF600BE)
            ) {
                Text("+", fontSize = 26.sp)
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
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
                    }
                )
            }
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
                    .background(
                        color = iconBackground,
                        shape = CircleShape
                    ),
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
        }
    }
}
