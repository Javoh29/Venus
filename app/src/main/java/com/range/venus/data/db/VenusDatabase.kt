package com.range.venus.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.range.venus.data.db.entities.*

@Database(
    entities = [UserModel::class, PaymentModel::class, DebitModel::class, TableModel::class, AttenModel::class],
    version = 7,
    exportSchema = false
)
abstract class VenusDatabase: RoomDatabase() {

    abstract fun venusDao(): VenusDao

    companion object {
        @Volatile
        private var instance: VenusDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                VenusDatabase::class.java, "venus.db"
            ).fallbackToDestructiveMigration().build()
    }
}