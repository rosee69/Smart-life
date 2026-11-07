package ua.edu.mobile.smartlife.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ua.edu.mobile.smartlife.data.model.FakeData
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType
import javax.inject.Inject

class InMemoryRecordRepository @Inject constructor() : RecordRepository {

    private val records = MutableStateFlow(FakeData.records)

    override fun observeRecords(): Flow<List<HealthRecord>> {
        return records
    }

    override fun observeRecordsByType(
        type: RecordType?
    ): Flow<List<HealthRecord>> {
        return records.map { list ->
            if (type == null) {
                list
            } else {
                list.filter { it.type == type }
            }
        }
    }

    override suspend fun addRecord(record: HealthRecord) {
        records.value = records.value + record
    }

    override suspend fun updateRecord(record: HealthRecord) {
        records.value = records.value.map {
            if (it.id == record.id) record else it
        }
    }

    override suspend fun deleteRecord(id: Long) {
        records.value = records.value.filterNot { it.id == id }
    }

    override suspend fun restoreRecord(record: HealthRecord, index: Int) {
        val updatedRecords = records.value.toMutableList()
        updatedRecords.add(index.coerceIn(0, updatedRecords.size), record)
        records.value = updatedRecords
    }
}