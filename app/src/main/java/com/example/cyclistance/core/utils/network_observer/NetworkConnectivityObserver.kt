package com.example.cyclistance.core.utils.network_observer
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(
    val context: Context
): ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkConnectivityUtil = NetworkConnectivityUtil(context)
    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    //todo implement later
                    val status = if (networkConnectivityUtil.hasInternet()) ConnectivityObserver.Status.Available else ConnectivityObserver.Status.Lost
                    launch { send(status) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    val status = if (networkConnectivityUtil.hasInternet()) ConnectivityObserver.Status.Available else ConnectivityObserver.Status.Lost
                    launch { send(status) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    val status = if (networkConnectivityUtil.hasInternet()) ConnectivityObserver.Status.Available else ConnectivityObserver.Status.Unavailable
                    launch { send(status) }
                }
            }
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}