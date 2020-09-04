package com.range.venus.data.pravider

import android.content.Context
import android.net.ConnectivityManager
import java.net.InetAddress
import java.net.UnknownHostException

class UnitProviderImpl(private val context: Context) : UnitProvider, PreferenceProvider(context) {

    private val USER_ID = "userID"
    private val PASSWORD = "password"

    override suspend fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected) {
            try {
                !InetAddress.getByName("google.com").equals("")
            }catch (e: UnknownHostException){
                false
            }
        }else false
    }

    override fun saveUserID(id: String) {
        preferences.edit().putString(USER_ID, id).apply()
    }

    override fun getUserID(): String {
        return preferences.getString(USER_ID, "")!!
    }

    override fun savePassword(pass: String) {
        preferences.edit().putString(PASSWORD, pass).apply()
    }

    override fun getPassword(): String {
        return preferences.getString(PASSWORD, "")!!
    }

}