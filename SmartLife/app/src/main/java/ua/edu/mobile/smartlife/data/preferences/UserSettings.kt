package ua.edu.mobile.smartlife.data.preferences

data class UserSettings (
    val userName: String = "",
    val theme: String = "SYSTEM",
    val dailyWaterGoal: Int = 2000
)