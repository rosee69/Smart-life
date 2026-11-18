
package ua.edu.mobile.smartlife.ui.records

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.edu.mobile.smartlife.data.export.CsvExporter
import ua.edu.mobile.smartlife.data.model.HealthRecord
import ua.edu.mobile.smartlife.data.model.RecordType
import ua.edu.mobile.smartlife.data.repository.RecordRepository
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val repository: RecordRepository,
    private val csvExporter: CsvExporter
) : ViewModel() {

    private val _selectedType = MutableStateFlow<RecordType?>(null)
    val selectedType: StateFlow<RecordType?> = _selectedType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _exportedFileUri = MutableStateFlow<Uri?>(null)
    val exportedFileUri: StateFlow<Uri?> = _exportedFileUri.asStateFlow()

    val allRecords = repository.observeRecords()

    val records = combine(_selectedType, _searchQuery) { type, query ->
        type to query
    }.flatMapLatest { (type, query) ->
        if (query.isBlank()) {
            repository.observeRecordsByType(type)
        } else {
            repository.searchRecords(query).map { list ->
                if (type == null) list else list.filter { it.type == type }
            }
        }
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
                    note = note,
                    dateTime = SimpleDateFormat(
                        "dd.MM.yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date())
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

    fun searchRecords(query: String) {
        _searchQuery.value = query
    }

    fun exportRecords() {
        viewModelScope.launch {
            _exportedFileUri.value = csvExporter.exportRecords()
        }
    }

    fun clearExportedFileUri() {
        _exportedFileUri.value = null
    }
}
