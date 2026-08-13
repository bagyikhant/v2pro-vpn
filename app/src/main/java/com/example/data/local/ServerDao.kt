package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerDao {

    @Query("SELECT * FROM vpn_servers ORDER BY isFavorite DESC, name ASC")
    fun getAllServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM vpn_servers WHERE id = :id")
    suspend fun getServerById(id: Long): ServerEntity?

    @Query("SELECT * FROM vpn_servers WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM vpn_servers WHERE protocol = :protocol ORDER BY name ASC")
    fun getServersByProtocol(protocol: String): Flow<List<ServerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: ServerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(servers: List<ServerEntity>)

    @Update
    suspend fun updateServer(server: ServerEntity)

    @Delete
    suspend fun deleteServer(server: ServerEntity)

    @Query("DELETE FROM vpn_servers WHERE id = :id")
    suspend fun deleteServerById(id: Long)

    @Query("UPDATE vpn_servers SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM vpn_servers")
    suspend fun getServerCount(): Int
}
