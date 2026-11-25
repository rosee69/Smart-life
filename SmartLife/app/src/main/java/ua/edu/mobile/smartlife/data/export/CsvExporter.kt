package ua.edu.mobile.smartlife.data.export

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ua.edu.mobile.smartlife.data.repository.RecordRepository
import java.io.File
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvExporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: RecordRepository
) {

    suspend fun exportRecords(): Uri = withContext(Dispatchers.IO) {
        val records = repository.observeRecords().first()

        val exportDirectory = File(context.filesDir, "exports")
        if (!exportDirectory.exists()) {
            exportDirectory.mkdirs()
        }

        val file = File(
            exportDirectory,
            "smart_life_records_${System.currentTimeMillis()}.csv"
        )

        val csvContent = buildString {
            appendLine("ID,Тип,Значення,Нотатка,Дата і час")

            records.forEach { record ->
                appendLine(
                    listOf(
                        record.id.toString(),
                        record.type.name,
                        record.value.toString(),
                        record.note,
                        record.dateTime
                    ).joinToString(",") { value ->
                        "\"${value.replace("\"", "\"\"")}\""
                    }
                )
            }
        }

        file.writeText(csvContent, StandardCharsets.UTF_8)

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}