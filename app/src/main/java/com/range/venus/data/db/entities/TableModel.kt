package com.range.venus.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "lessons_table")
data class TableModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("auditoriya")
    val auditoriya: String,
    @SerializedName("dars_nomi")
    val darsNomi: String,
    @SerializedName("dars_turi")
    val darsTuri: String,
    @SerializedName("dars_vaqti")
    val darsVaqti: String,
    @SerializedName("hafta_id")
    val haftaId: String,
    @SerializedName("mash_turi")
    val mashTuri: String,
    @SerializedName("oqituvchi")
    val oqituvchi: String,
    @SerializedName("para_id")
    val paraId: String
)