package com.range.venus.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class UserModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("fakulteti")
    val fakulteti: String,
    @SerializedName("fio")
    val fio: String,
    @SerializedName("guruhi")
    val guruhi: String,
    @SerializedName("Id")
    val id: String,
    @SerializedName("kursi")
    val kursi: String,
    @SerializedName("parol")
    val parol: String,
    @SerializedName("universiteti")
    val universiteti: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("yonalishi")
    val yonalishi: String
)