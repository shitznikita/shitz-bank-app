package com.example.shitzbank.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Определяет текущее состояние сетевого соединения.
 */
sealed class ConnectionStatus {
    /**
     * Сетевое соединение доступно.
     */
    data object Available : ConnectionStatus()

    /**
     * Сетевое соединение недоступно.
     */
    data object Unavailable : ConnectionStatus()
}

/**
 * Отслеживает состояние сетевого соединения устройства.
 * Предоставляет [Flow] для наблюдения за изменениями доступности сети.
 *
 * @param context Контекст приложения, необходимый для доступа к [ConnectivityManager].
 */
class NetworkMonitor(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * [Flow], который предоставляет текущее состояние сетевого соединения.
     * Эмитирует [ConnectionStatus.Available] при наличии подключения к интернету
     * и [ConnectionStatus.Unavailable] при его отсутствии.
     *
     * Использует [ConnectivityManager.NetworkCallback] для получения обновлений состояния сети
     * и [distinctUntilChanged] для эмиссии только при изменении состояния.
     */
    val connectionStatus: Flow<ConnectionStatus> =
        callbackFlow {
            val callback =
                object : ConnectivityManager.NetworkCallback() {
                    /**
                     * Вызывается при появлении нового сетевого соединения.
                     *
                     * @param network Объект [Network], представляющий доступную сеть.
                     */
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        trySend(ConnectionStatus.Available)
                    }

                    /**
                     * Вызывается при потере сетевого соединения.
                     *
                     * @param network Объект [Network], представляющий потерянную сеть.
                     */
                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(ConnectionStatus.Unavailable)
                    }

                    /**
                     * Вызывается при изменении возможностей сетевого соединения.
                     * Проверяет наличие возможности [NetworkCapabilities.NET_CAPABILITY_INTERNET].
                     *
                     * @param network Объект [Network], возможности которого изменились.
                     * @param networkCapabilities Новые возможности сети.
                     */
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities,
                    ) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            trySend(ConnectionStatus.Available)
                        } else {
                            trySend(ConnectionStatus.Unavailable)
                        }
                    }
                }

            val networkRequest =
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
            connectivityManager.registerNetworkCallback(networkRequest, callback)

            // Отправляем начальное состояние подключения при подписке на Flow
            if (isCurrentlyConnected()) {
                trySend(ConnectionStatus.Available)
            } else {
                trySend(ConnectionStatus.Unavailable)
            }

            /**
             * Блок, который выполняется при отмене Flow (например, когда сборщик Flow отменяется).
             * Отменяет регистрацию [NetworkCallback], чтобы избежать утечек памяти.
             */
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()

    /**
     * Проверяет, активно ли сетевое соединение с возможностью выхода в интернет в данный момент.
     *
     * @return `true`, если устройство имеет активное подключение к интернету, иначе `false`.
     */
    private fun isCurrentlyConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
