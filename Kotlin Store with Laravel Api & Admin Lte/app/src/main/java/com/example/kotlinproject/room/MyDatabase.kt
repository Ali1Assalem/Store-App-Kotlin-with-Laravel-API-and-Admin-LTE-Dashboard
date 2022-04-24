package com.inyongtisto.tokoonline.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlinproject.model.Favorite
import com.example.kotlinproject.model.Produk
import com.example.kotlinproject.room.DaoAddress
import com.example.kotlinproject.room.DaoFavorite
import com.inyongtisto.tokoonline.model.Address


@Database(entities = [Produk::class,Address::class,Favorite::class] /* List model Ex:NoteModel */, version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun daoCart() :DaoCart
    abstract fun daoAddress(): DaoAddress
    abstract fun daoFavorite():DaoFavorite

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            MyDatabase::class.java, "MyDatabase14"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}