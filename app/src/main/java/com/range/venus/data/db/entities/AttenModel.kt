package com.range.venus.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "atten_table")
data class AttenModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("kelgan_vaqti")
    val kelganVaqti: String,
    @SerializedName("ketgan_vaqti")
    val ketganVaqti: String
)