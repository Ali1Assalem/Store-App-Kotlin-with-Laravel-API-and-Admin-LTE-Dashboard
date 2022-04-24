package com.example.kotlinproject.room

import androidx.room.*
import com.inyongtisto.tokoonline.model.Address
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface DaoAddress {
    @Insert(onConflict = REPLACE)
    fun insert(data: Address)

    @Delete
    fun delete(data: Address)

    @Update
    fun update(data: Address): Int

    @Query("SELECT * from address ORDER BY id ASC")
    fun getAll(): List<Address>

    @Query("SELECT * FROM address WHERE id = :id LIMIT 1")
    fun getProduk(id: Int): Address

    @Query("SELECT * FROM address WHERE isSelected = :status LIMIT 1")
    fun getByStatus(status: Boolean): Address?

    @Query("DELETE FROM address")
    fun deleteAll(): Int
}