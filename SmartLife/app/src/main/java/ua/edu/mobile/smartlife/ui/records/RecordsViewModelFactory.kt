package ua.edu.mobile.smartlife.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.edu.mobile.smartlife.data.repository.RecordRepository

class RecordsViewModelFactory(
    private val repository: RecordRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}