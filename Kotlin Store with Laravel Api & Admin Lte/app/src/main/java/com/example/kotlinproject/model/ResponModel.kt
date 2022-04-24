package com.inyongtisto.tokoonline.model

import com.example.kotlinproject.model.Produk

class ResponModel {
    var success = 0
    lateinit var message: String
    var user = User()
    var produks: ArrayList<Produk> = ArrayList()
    var transaksis: ArrayList<Transaksi> = ArrayList()

    var rajaongkir= ModelAlamat()
    var transaksi = Transaksi()

    //var provinsi: ArrayList<ModelAlamat> = ArrayList()
    //var city: ArrayList<ModelAlamat> = ArrayList()
    var districts: ArrayList<ModelAlamat> = ArrayList()
}