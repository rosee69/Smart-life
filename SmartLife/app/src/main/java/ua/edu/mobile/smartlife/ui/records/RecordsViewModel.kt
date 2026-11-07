
package ua.edu.mobile.smartlife.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType
import ua.edu.mobile.smartlife.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {
    private val _selectedType = MutableStateFlow<RecordType?>(null)
    val selectedType: StateFlow<RecordType?> = _selectedType.asStateFlow()

    val records = _selectedType.flatMapLatest { type ->
        repository.observeRecordsByType(type)
    }

    private var recentlyDeletedRecord: Pair<Int, HealthRecord>? = null

    fun addRecord(type: RecordType, value: Double, note: String) {
        viewModelScope.launch {
            val currentRecords = repository.observeRecords().first()
            val newId = (currentRecords.maxOfOrNull { it.id } ?: 0L) + 1L

            repository.addRecord(
                HealthRecord(
                    id = newId,
                    type = type,
                    value = value,
                    note = note
                )
            )
        }
    }

    fun addWaterGlass() {
        addRecord(RecordType.WATER, 0.25, "Склянка води")
    }

    fun deleteRecord(id: Long) {
        viewModelScope.launch {
            val currentRecords = repository.observeRecords().first()
            val index = currentRecords.indexOfFirst { it.id == id }

            if (index != -1) {
                recentlyDeletedRecord = index to currentRecords[index]
                repository.deleteRecord(id)
            }
        }
    }

    fun undoDelete() {
        val deleted = recentlyDeletedRecord ?: return

        viewModelScope.launch {
            val (index, record) = deleted
            repository.restoreRecord(record, index)
            recentlyDeletedRecord = null
        }
    }

    fun setFilter(type: RecordType?) {
        _selectedType.value = type
    }
}
