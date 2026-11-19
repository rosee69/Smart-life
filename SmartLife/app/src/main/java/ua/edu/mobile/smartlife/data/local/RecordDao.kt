package ua.edu.mobile.smartlife.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Query("SELECT * FROM health_records ORDER BY id ASC")
    fun observeRecords(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM health_records WHERE type = :type ORDER BY id ASC")
    fun observeByType(type: String): Flow<List<RecordEntity>>

    @Query("SELECT * FROM health_records WHERE note LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchByNote(query: String): Flow<List<RecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: RecordEntity)

    @Update
    suspend fun update(record: RecordEntity)

    @Query("DELETE FROM health_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<RecordEntity>)
}