package com.inyongtisto.tokoonline.model

class Transaksi {
    var id = 0
    var bank = ""
    var jasa_pengiriaman = ""
    var courier = ""
    var name = ""
    var shipping = ""
    var phone = ""
    var total_price = ""
    var total_item = ""
    var total_transfer = ""
    var location_details = ""
    var user_id = ""
    var code_payment = ""
    var code_trx = ""
    var code_unique = 0
    var status = ""
    var expired_at = ""
    var updated_at = ""
    var created_at = ""
    val details = ArrayList<DetailTransaksi>()
}