package com.range.venus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.range.venus.data.db.entities.UserModel

@Dao
interface VenusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(model: UserModel)

    @Query("select * from user_table")
    fun getUser(): UserModel

    @Query("DELETE FROM user_table")
    fun deleteUser()
}