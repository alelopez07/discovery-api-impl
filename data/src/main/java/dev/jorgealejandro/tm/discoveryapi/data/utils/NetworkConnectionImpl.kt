package dev.jorgealejandro.tm.discoveryapi.data.utils

import android.content.Context
import java.io.IOException

class NetworkConnectionImpl(
    private val ctx: Context
) : NetworkConnection {
    override fun networkAccess(): Boolean {
        val ping = "/system/bin/ping -c 1 8.8.8.8"

        // Validate if connection exists.
        if (connectionExists(ctx)) {
            // If connection exists: Create a runtime variable.
            val runtime = Runtime.getRuntime()
            // Do a google ping. If true, this method return true.
            // This means we have network access and connectivity.
            try {
                val exitValue = runtime.exec(ping).waitFor()
                return exitValue == 0
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return false
    }
}