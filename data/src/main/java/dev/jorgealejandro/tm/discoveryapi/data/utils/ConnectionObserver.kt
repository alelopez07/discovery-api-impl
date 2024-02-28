package dev.jorgealejandro.tm.discoveryapi.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun connectionExists(context: Context): Boolean {
    // Open connection manager instance.
    val manager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
    if (capabilities != null) {
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    return false
}
