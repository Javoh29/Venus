package com.range.venus.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.range.venus.data.db.entities.*

@Dao
interface VenusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(model: UserModel)

    @Query("select * from user_table")
    fun getUser(): UserModel?

    @Query("DELETE FROM user_table")
    fun deleteUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayments(model: PaymentModel)

    @Query("select * from payments_table")
    fun getAllPayments(): LiveData<List<PaymentModel>>

    @Query("select * from payments_table where sana like :date")
    fun getPayment(date: String): List<PaymentModel>?

    @Query("DELETE FROM payments_table")
    fun deletePayments()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDebit(model: DebitModel)

    @Query("select * from debit_table")
    fun getDebit(): DebitModel?

    @Query("DELETE FROM debit_table")
    fun deleteDebit()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLessons(model: TableModel)

    @Query("select * from lessons_table where haftaId = :weekID ORDER BY paraId, darsTuri ASC")
    fun getLessons(weekID: String): LiveData<List<TableModel>>

    @Query("DELETE FROM lessons_table")
    fun deleteLessons()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAtten(model: AttenModel)

    @Query("select * from atten_table where kelganVaqti >= :date1 and ketganVaqti <= :date2")
    fun getAtten(date1: String, date2: String): LiveData<List<AttenModel>>

    @Query("DELETE FROM atten_table")
    fun deleteAtten()

}