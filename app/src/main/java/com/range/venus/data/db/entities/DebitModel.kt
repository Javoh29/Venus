package com.range.venus.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "debit_table")
data class DebitModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @SerializedName("summa")
    val summa: String
)