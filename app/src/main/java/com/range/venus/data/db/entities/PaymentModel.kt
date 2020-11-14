package com.range.venus.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "payments_table")
data class PaymentModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("Id")
    val id: String,
    @SerializedName("sana")
    val sana: String,
    @SerializedName("summa")
    val summa: String,
    @SerializedName("turi")
    val turi: String,
    @SerializedName("user_id")
    val userId: String
)