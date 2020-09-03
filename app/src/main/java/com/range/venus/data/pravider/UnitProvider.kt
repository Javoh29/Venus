package com.range.venus.data.pravider

interface UnitProvider {

    suspend fun isOnline(): Boolean

    fun saveUserID(id: String)

    fun getUserID(): String
}