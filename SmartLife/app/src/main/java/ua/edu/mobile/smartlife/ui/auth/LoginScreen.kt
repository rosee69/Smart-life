
package ua.edu.mobile.smartlife.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val tealColor = Color(0xFFF600BE)
    val backgroundColor = Color(0xFFFFF7FC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Box(
            modifier = Modifier
                .size(76.dp)
                .background(tealColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(43.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Smart Life",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val pulse = Path().apply {
                        moveTo(size.width * 0.08f, size.height * 0.52f)
                        lineTo(size.width * 0.32f, size.height * 0.52f)
                        lineTo(size.width * 0.43f, size.height * 0.28f)
                        lineTo(size.width * 0.55f, size.height * 0.76f)
                        lineTo(size.width * 0.68f, size.height * 0.43f)
                        lineTo(size.width * 0.77f, size.height * 0.52f)
                        lineTo(size.width * 0.92f, size.height * 0.52f)
                    }

                    drawPath(
                        path = pulse,
                        color = tealColor,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Smart Life",
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Персональний моніторинг здоров'я",
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = login,
            onValueChange = {
                login = it
                errorMessage = ""
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Логін") },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Логін"
                )
            },
            shape = RoundedCornerShape(6.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = tealColor,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Пароль") },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Пароль"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Показати пароль"
                    )
                }
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            shape = RoundedCornerShape(6.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = tealColor,
                unfocusedBorderColor = Color.Gray
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp)
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (login.isBlank() || password.isBlank()) {
                    errorMessage = "Введіть логін і пароль"
                } else {
                    onLogin()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = tealColor
            )
        ) {
            Text("Увійти")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onRegister
        ) {
            Text(
                text = "Немає акаунта? Зареєструватися",
                color = tealColor,
                fontSize = 13.sp
            )
        }
    }
}


