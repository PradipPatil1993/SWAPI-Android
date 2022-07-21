package com.example.startwars.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object AppUtils {
    @SuppressLint("NewApi")
    fun hasInternetConnection(application: Context): Boolean {
      val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.let { it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) }?: false
    }
}