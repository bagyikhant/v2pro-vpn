package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ServerEntity
import com.example.data.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServerRepository
    val selectedFilter = MutableStateFlow("All")

    val servers: StateFlow<List<ServerEntity>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ServerRepository(db.serverDao())

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        servers = combine(repository.allServers, selectedFilter) { allList, filter ->
            when (filter) {
                "Favorites" -> allList.filter { it.isFavorite }
                "WireGuard" -> allList.filter { it.protocol.equals("WireGuard", ignoreCase = true) }
                "OpenVPN" -> allList.filter { it.protocol.equals("OpenVPN", ignoreCase = true) }
                "IKEv2" -> allList.filter { it.protocol.equals("IKEv2", ignoreCase = true) }
                else -> allList
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun setFilter(filter: String) {
        selectedFilter.value = filter
    }

    fun toggleFavorite(server: ServerEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(server)
        }
    }

    fun addServer(
        name: String,
        country: String,
        countryCode: String,
        address: String,
        port: Int,
        protocol: String,
        config: String
    ) {
        viewModelScope.launch {
            val newServer = ServerEntity(
                name = name,
                country = country,
                countryCode = countryCode.uppercase(),
                serverAddress = address,
                port = port,
                protocol = protocol,
                configData = config,
                pingMs = (15..120).random(),
                isCustom = true
            )
            repository.insert(newServer)
        }
    }

    fun deleteServer(server: ServerEntity) {
        viewModelScope.launch {
            repository.delete(server)
        }
    }
}
