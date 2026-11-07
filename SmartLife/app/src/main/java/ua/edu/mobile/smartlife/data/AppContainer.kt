package ua.edu.mobile.smartlife.data

import ua.edu.mobile.smartlife.data.repository.InMemoryRecordRepository
import ua.edu.mobile.smartlife.data.repository.RecordRepository

class AppContainer {
    val recordRepository: RecordRepository = InMemoryRecordRepository()
}