package ua.edu.mobile.smartlife.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_records")
data class RecordEntity(
    @PrimaryKey
    val id: Long,
    val type: String,
    val value: Double,
    val note: String,
    val dateTime: String
)