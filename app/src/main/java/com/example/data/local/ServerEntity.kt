package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vpn_servers")
data class ServerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val country: String,
    val countryCode: String,
    val serverAddress: String,
    val port: Int = 51820,
    val protocol: String = "WireGuard",
    val configData: String = "",
    val pingMs: Int = 0,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
