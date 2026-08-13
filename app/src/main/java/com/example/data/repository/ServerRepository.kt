package com.example.data.repository

import com.example.data.local.ServerDao
import com.example.data.local.ServerEntity
import kotlinx.coroutines.flow.Flow

class ServerRepository(private val serverDao: ServerDao) {

    val allServers: Flow<List<ServerEntity>> = serverDao.getAllServers()
    val favoriteServers: Flow<List<ServerEntity>> = serverDao.getFavoriteServers()

    fun getServersByProtocol(protocol: String): Flow<List<ServerEntity>> {
        return serverDao.getServersByProtocol(protocol)
    }

    suspend fun getServerById(id: Long): ServerEntity? {
        return serverDao.getServerById(id)
    }

    suspend fun insert(server: ServerEntity): Long {
        return serverDao.insertServer(server)
    }

    suspend fun update(server: ServerEntity) {
        serverDao.updateServer(server)
    }

    suspend fun delete(server: ServerEntity) {
        serverDao.deleteServer(server)
    }

    suspend fun deleteById(id: Long) {
        serverDao.deleteServerById(id)
    }

    suspend fun toggleFavorite(server: ServerEntity) {
        serverDao.setFavorite(server.id, !server.isFavorite)
    }

    suspend fun seedInitialDataIfEmpty() {
        if (serverDao.getServerCount() == 0) {
            val sampleServers = listOf(
                ServerEntity(
                    name = "US East - New York",
                    country = "United States",
                    countryCode = "US",
                    serverAddress = "useast1.vpn.network",
                    port = 51820,
                    protocol = "WireGuard",
                    pingMs = 24,
                    isFavorite = true,
                    configData = "[Interface]\nPrivateKey = SAMPLE_KEY_1\nAddress = 10.0.0.2/32\n\n[Peer]\nPublicKey = SERVER_PUB_KEY\nEndpoint = useast1.vpn.network:51820"
                ),
                ServerEntity(
                    name = "US West - Los Angeles",
                    country = "United States",
                    countryCode = "US",
                    serverAddress = "uswest1.vpn.network",
                    port = 51820,
                    protocol = "WireGuard",
                    pingMs = 42,
                    configData = "[Interface]\nPrivateKey = SAMPLE_KEY_2\nAddress = 10.0.0.3/32\n\n[Peer]\nPublicKey = SERVER_PUB_KEY\nEndpoint = uswest1.vpn.network:51820"
                ),
                ServerEntity(
                    name = "Europe - Frankfurt",
                    country = "Germany",
                    countryCode = "DE",
                    serverAddress = "fra1.vpn.network",
                    port = 1194,
                    protocol = "OpenVPN",
                    pingMs = 85,
                    isFavorite = true,
                    configData = "client\ndev tun\nproto udp\nremote fra1.vpn.network 1194\nresolv-retry infinite\nnobind"
                ),
                ServerEntity(
                    name = "Asia - Tokyo Central",
                    country = "Japan",
                    countryCode = "JP",
                    serverAddress = "tokyo1.vpn.network",
                    port = 51820,
                    protocol = "WireGuard",
                    pingMs = 110,
                    configData = "[Interface]\nPrivateKey = SAMPLE_KEY_3\nAddress = 10.0.0.4/32\n\n[Peer]\nPublicKey = SERVER_PUB_KEY\nEndpoint = tokyo1.vpn.network:51820"
                ),
                ServerEntity(
                    name = "UK - London",
                    country = "United Kingdom",
                    countryCode = "GB",
                    serverAddress = "lon1.vpn.network",
                    port = 500,
                    protocol = "IKEv2",
                    pingMs = 72,
                    configData = "conn uk-london\n  keyexchange=ikev2\n  right=lon1.vpn.network"
                )
            )
            serverDao.insertAll(sampleServers)
        }
    }
}
