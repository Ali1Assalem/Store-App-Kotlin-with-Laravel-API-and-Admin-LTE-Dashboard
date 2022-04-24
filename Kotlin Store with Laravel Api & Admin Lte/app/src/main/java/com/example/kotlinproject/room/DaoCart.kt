package com.inyongtisto.tokoonline.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.kotlinproject.model.Produk

@Dao
interface DaoCart {

    @Insert(onConflict = REPLACE)
    fun insert(data: Produk)

    @Delete
    fun delete(data: Produk)

    @Delete
    fun delete(data: List<Produk>)

    @Update
    fun update(data: Produk): Int

    @Query("SELECT * from cart ORDER BY id ASC")
    fun getAll(): List<Produk>

    @Query("SELECT * FROM cart WHERE id = :id LIMIT 1")
    fun getProduk(id: Int): Produk

    @Query("DELETE FROM cart WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM cart")
    fun deleteAll(): Int
}