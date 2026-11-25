package ua.edu.mobile.smartlife.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.edu.mobile.smartlife.data.local.RecordDao
import ua.edu.mobile.smartlife.data.local.RecordEntity
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType
import javax.inject.Inject

class RoomRecordRepository @Inject constructor(
    private val recordDao: RecordDao
) : RecordRepository {

    override fun observeRecords(): Flow<List<HealthRecord>> {
        return recordDao.observeRecords().map { records ->
            records.map { it.toModel() }
        }
    }

    override fun observeRecordsByType(type: RecordType?): Flow<List<HealthRecord>> {
        return if (type == null) {
            observeRecords()
        } else {
            recordDao.observeByType(type.name).map { records ->
                records.map { it.toModel() }
            }
        }
    }

    override fun searchRecords(query: String): Flow<List<HealthRecord>> {
        return recordDao.searchByNote(query).map { records ->
            records.map { it.toModel() }
        }
    }

    override suspend fun addRecord(record: HealthRecord) {
        recordDao.insert(record.toEntity())
    }

    override suspend fun updateRecord(record: HealthRecord) {
        recordDao.update(record.toEntity())
    }

    override suspend fun deleteRecord(id: Long) {
        recordDao.deleteById(id)
    }

    override suspend fun restoreRecord(record: HealthRecord, index: Int) {
        recordDao.insert(record.toEntity())
    }

    private fun RecordEntity.toModel() = HealthRecord(
        id = id,
        type = RecordType.valueOf(type),
        value = value,
        note = note,
        dateTime = dateTime
    )

    private fun HealthRecord.toEntity() = RecordEntity(
        id = id,
        type = type.name,
        value = value,
        note = note,
        dateTime = dateTime
    )
}