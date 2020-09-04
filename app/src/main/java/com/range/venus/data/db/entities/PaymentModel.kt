package com.range.venus.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "payments_table")
data class PaymentModel(
    @SerializedName("doc_nomer")
    val docNomer: String,
    @SerializedName("gazna")
    val gazna: String,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("Id")
    val id: String,
    @SerializedName("izox")
    val izox: String,
    @SerializedName("sana")
    val sana: String,
    @SerializedName("summa")
    val summa: String,
    @SerializedName("tip_doc")
    val tipDoc: String,
    @SerializedName("tolovchi")
    val tolovchi: String,
    @SerializedName("turi")
    val turi: String,
    @SerializedName("user_id")
    val userId: String
)