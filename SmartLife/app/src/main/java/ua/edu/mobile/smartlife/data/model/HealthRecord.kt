package ua.edu.mobile.smartlife.data.model

data class HealthRecord(
    val id: Long,
    val type: RecordType,
    val value: Double,
    val note: String,
    val dateTime: String = ""
)