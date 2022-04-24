package com.example.kotlinproject.room

import androidx.room.*
import com.example.kotlinproject.model.Favorite
import com.example.kotlinproject.model.Produk
import com.inyongtisto.tokoonline.model.Address

@Dao
interface DaoFavorite {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Favorite)

    @Delete
    fun delete(data: Favorite)

    @Update
    fun update(data: Favorite): Int

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE id = :id LIMIT 1")
    fun getFavorite(id: Int): Favorite

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM favorite")
    fun deleteAll(): Int
}