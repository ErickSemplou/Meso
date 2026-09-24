package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the 'units' Room entity.
 * Provides reactive Flow queries and asynchronous suspend operations.
 */
@Dao
interface UnitDao {

    @Query("SELECT * FROM units ORDER BY attack DESC")
    fun getAllUnits(): Flow<List<UnitEntity>>

    @Query("SELECT * FROM units WHERE factionId = :factionId ORDER BY attack DESC")
    fun getUnitsByFaction(factionId: String): Flow<List<UnitEntity>>

    @Query("SELECT * FROM units WHERE id = :id")
    fun getUnitById(id: String): Flow<UnitEntity?>

    @Query("SELECT * FROM units WHERE id = :id LIMIT 1")
    suspend fun getUnitByIdOnce(id: String): UnitEntity?

    @Query("SELECT * FROM units WHERE factionId = :factionId AND unitTypeId = :unitTypeId LIMIT 1")
    suspend fun getUnitByFactionAndType(factionId: String, unitTypeId: String): UnitEntity?

    @Query("SELECT * FROM units WHERE cityId = :cityId")
    fun getUnitsByCity(cityId: String): Flow<List<UnitEntity>>

    @Query("SELECT COUNT(*) FROM units")
    suspend fun getUnitsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: UnitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<UnitEntity>)

    @Update
    suspend fun updateUnit(unit: UnitEntity)

    @Delete
    suspend fun deleteUnit(unit: UnitEntity)

    @Query("DELETE FROM units")
    suspend fun deleteAllUnits()
}
