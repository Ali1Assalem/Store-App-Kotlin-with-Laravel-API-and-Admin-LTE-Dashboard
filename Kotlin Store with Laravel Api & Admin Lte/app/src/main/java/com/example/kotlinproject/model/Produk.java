package com.example.kotlinproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cart")
public class Produk implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String name;
    public String price;
    public String description;
    public int category_id;
    public String image;
    public String created_at;
    public String updated_at;

    public int discount = 0;
    public int qty = 1;
    public boolean selected = true;
}
