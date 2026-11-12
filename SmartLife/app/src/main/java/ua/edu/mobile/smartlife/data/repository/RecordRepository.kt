package ua.edu.mobile.smartlife.data.repository

import kotlinx.coroutines.flow.Flow
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType

interface RecordRepository {

    fun observeRecords(): Flow<List<HealthRecord>>

    fun observeRecordsByType(type: RecordType?): Flow<List<HealthRecord>>

    suspend fun addRecord(record: HealthRecord)

    suspend fun updateRecord(record: HealthRecord)

    suspend fun deleteRecord(id: Long)

    suspend fun restoreRecord(record: HealthRecord, index: Int)
}