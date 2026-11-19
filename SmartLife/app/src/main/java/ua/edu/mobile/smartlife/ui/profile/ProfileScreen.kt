
package ua.edu.mobile.smartlife.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.edu.mobile.smartlife.data.preferences.UserSettings

private val AccentColor = Color(0xFFF600BE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userSettings: UserSettings,
    onUserNameChange: (String) -> Unit,
    onThemeChange: (String) -> Unit,
    onWaterGoalChange: (Int) -> Unit,
    onLogout: () -> Unit
) {
    var userName by remember { mutableStateOf(userSettings.userName) }
    var waterGoal by remember {
        mutableFloatStateOf(userSettings.dailyWaterGoal.toFloat())
    }
    var selectedTheme by remember { mutableStateOf(userSettings.theme) }

    LaunchedEffect(userSettings) {
        userName = userSettings.userName
        waterGoal = userSettings.dailyWaterGoal.toFloat()
        selectedTheme = userSettings.theme
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Налаштування") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Особистий профіль",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ваше ім'я") },
                        singleLine = true
                    )

                    Button(
                        onClick = { onUserNameChange(userName) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentColor
                        )
                    ) {
                        Text("Зберегти ім'я")
                    }
                }
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Тема оформлення",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    listOf(
                        "SYSTEM" to "Як у системі",
                        "LIGHT" to "Світла",
                        "DARK" to "Темна"
                    ).forEach { (theme, title) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTheme == theme,
                                onClick = {
                                    selectedTheme = theme
                                    onThemeChange(theme)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AccentColor
                                )
                            )
                            Text(title)
                        }
                    }
                }
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Денна норма води",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Денна ціль води: ${
                            "%.2f".format(waterGoal / 1000f)
                        } л",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AccentColor,
                        fontWeight = FontWeight.Bold
                    )

                    Slider(
                        value = waterGoal,
                        onValueChange = { waterGoal = it },
                        valueRange = 1000f..5000f,
                        steps = 15,
                        colors = SliderDefaults.colors(
                            thumbColor = AccentColor,
                            activeTrackColor = AccentColor,
                            inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1.00 л", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("5.00 л", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Button(
                        onClick = {
                            onWaterGoalChange(waterGoal.toInt())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentColor
                        )
                    ) {
                        Text("Зберегти норму води")
                    }
                }
            }

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentColor
                )
            ) {
                Text("Вийти з акаунта")
            }
        }
    }
}
