package com.inyongtisto.tokoonline.model

class Chekout {
    lateinit var user_id: String
    lateinit var total_item: String
    lateinit var total_price: String
    lateinit var name: String
    lateinit var phone: String
    lateinit var courier: String
    lateinit var location_details: String
    lateinit var Delivery_Service: String
    lateinit var shipping: String
    lateinit var total_transfer: String
    lateinit var bank: String


    lateinit var receipt: String
    lateinit var methode: String
    lateinit var description: String

    var produks = ArrayList<Item>()

    class Item {
        lateinit var id: String
        lateinit var total_item: String
        lateinit var total_price: String
        lateinit var notes: String
    }
}