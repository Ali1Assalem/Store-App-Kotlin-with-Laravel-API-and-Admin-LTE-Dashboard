package com.inyongtisto.tokoonline.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ali on 1/20/2021.
 */

@Entity(tableName = "address")
class Address {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    var idTb = 0

    var id = 0
    var name = ""
    var phone = ""
    var type = ""
    var address = ""

    var id_provinsi = 0
    var id_city = 0
    var id_districts = 0
    var provinsi = ""
    var city = ""
    var districts = ""
    var codepos = ""
    var isSelected = false
}