
package ua.edu.mobile.smartlife.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ua.edu.mobile.smartlife.data.model.RecordType

private val accentColor = Color(0xFFF600BE)
private val selectedColor = Color(0xFFF0B8EA)
private val chipColor = Color(0xFFF3EFF7)

@Composable
fun AddRecordDialog(
    onDismiss: () -> Unit,
    onSave: (RecordType, Double, String) -> Unit
) {
    var selectedType by remember {
        mutableStateOf(RecordType.PULSE)
    }

    var value by remember {
        mutableStateOf("")
    }

    var note by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val types = listOf(
        RecordType.WATER,
        RecordType.PULSE,
        RecordType.WEIGHT,
        RecordType.SLEEP,
        RecordType.MOOD
    )

    fun typeName(type: RecordType): String {
        return when (type) {
            RecordType.WATER -> "Вода"
            RecordType.PULSE -> "Пульс"
            RecordType.WEIGHT -> "Вага"
            RecordType.SLEEP -> "Сон"
            RecordType.MOOD -> "Настрій"
        }
    }

    fun typeIcon(type: RecordType): ImageVector {
        return when (type) {
            RecordType.WATER -> Icons.Default.WaterDrop
            RecordType.PULSE -> Icons.Default.Favorite
            RecordType.WEIGHT -> Icons.Default.FitnessCenter
            RecordType.SLEEP -> Icons.Default.Bedtime
            RecordType.MOOD -> Icons.Default.SentimentSatisfied
        }
    }

    val valueLabel = when (selectedType) {
        RecordType.WATER -> "Значення, л"
        RecordType.PULSE -> "Значення, уд/хв"
        RecordType.WEIGHT -> "Значення, кг"
        RecordType.SLEEP -> "Значення, год"
        RecordType.MOOD -> "Значення, бал"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Новий запис")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Тип показника")

                // Перший рядок: Вода, Пульс
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    types.take(2).forEach { type ->
                        RecordTypeChip(
                            type = type,
                            selectedType = selectedType,
                            typeName = ::typeName,
                            typeIcon = ::typeIcon,
                            onClick = {
                                selectedType = type
                                value = ""
                                errorMessage = ""
                            }
                        )
                    }
                }

                // Другий рядок: Вага, Сон
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    types.drop(2).take(2).forEach { type ->
                        RecordTypeChip(
                            type = type,
                            selectedType = selectedType,
                            typeName = ::typeName,
                            typeIcon = ::typeIcon,
                            onClick = {
                                selectedType = type
                                value = ""
                                errorMessage = ""
                            }
                        )
                    }
                }

                // Третій рядок: Настрій
                Row {
                    RecordTypeChip(
                        type = RecordType.MOOD,
                        selectedType = selectedType,
                        typeName = ::typeName,
                        typeIcon = ::typeIcon,
                        onClick = {
                            selectedType = RecordType.MOOD
                            value = ""
                            errorMessage = ""
                        }
                    )
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text(valueLabel)
                    }
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = {
                        note = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text("Нотатка (необов'язково)")
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val number = value
                        .replace(',', '.')
                        .toDoubleOrNull()

                    if (number == null || number <= 0) {
                        errorMessage = "Введіть коректне значення"
                    } else {
                        onSave(selectedType, number, note)
                    }
                }
            ) {
                Text(
                    "Зберегти",
                    color = accentColor
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    "Скасувати",
                    color = accentColor
                )
            }
        },
        containerColor = Color(0xFFFFF7FC)
    )
}

@Composable
private fun RecordTypeChip(
    type: RecordType,
    selectedType: RecordType,
    typeName: (RecordType) -> String,
    typeIcon: (RecordType) -> ImageVector,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selectedType == type,
        onClick = onClick,
        label = {
            Text(typeName(type))
        },
        leadingIcon = {
            Icon(
                imageVector = typeIcon(type),
                contentDescription = null
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = chipColor,
            labelColor = Color.DarkGray,
            iconColor = accentColor,
            selectedContainerColor = selectedColor,
            selectedLabelColor = accentColor,
            selectedLeadingIconColor = accentColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = accentColor
        )
    )
}
