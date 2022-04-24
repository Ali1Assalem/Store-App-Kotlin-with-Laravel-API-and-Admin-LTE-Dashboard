package com.example.kotlinproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinproject.R
import com.example.kotlinproject.helper.Helper
import com.example.kotlinproject.helper.SharedPref
import com.google.gson.Gson
import com.inyongtisto.tokoonline.adapter.AdapterKurir
import com.inyongtisto.tokoonline.app.ApiConfigAddress
import com.inyongtisto.tokoonline.model.Chekout
import com.inyongtisto.tokoonline.model.rajaongkir.Costs
import com.inyongtisto.tokoonline.model.rajaongkir.ResponOngkir
import com.inyongtisto.tokoonline.room.MyDatabase
import com.inyongtisto.tokoonline.util.ApiKey
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase
    var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)
        Helper().setToolbar(this, toolbar, "Delivery")
        myDb = MyDatabase.getInstance(this)!!

        totalHarga = Integer.valueOf(intent.getStringExtra("extra")!!)
        tv_totalBelanja.text = Helper().changeRupiah(totalHarga)
        mainButton()
        setSepiner()
        checkAddress()
    }
    private fun mainButton() {
        btn_Address.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        btn_buy.setOnClickListener {
            bayar()
        }
    }

    fun setSepiner() {
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    getShipping(spn_kurir.selectedItem.toString())
                }
            }
        }
    }

    private fun bayar() {
        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAddress().getByStatus(true)!!

        val listProduk = myDb.daoCart().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
        val produks = ArrayList<Chekout.Item>()
        for (p in listProduk) {
            if (p.selected) {
                totalItem += p.qty
                totalHarga += (p.qty * Integer.valueOf(p.price))

                val produk = Chekout.Item()
                produk.id = "" + p.id
                produk.total_item = "" + p.qty
                produk.total_price = "" + (p.qty * Integer.valueOf(p.price))
                produk.notes = "new record"
                produks.add(produk)
            }
        }

        val chekout = Chekout()
        chekout.user_id = "" + user.id
        chekout.total_item = "" + totalItem
        chekout.total_price = "" + totalHarga
        chekout.name = a.name
        chekout.phone = a.phone
        chekout.Delivery_Service = jasaKirim
        chekout.shipping = ongkir
        chekout.courier = kurir
        chekout.location_details = tv_alamat.text.toString()
        chekout.total_transfer = "" + (totalHarga + Integer.valueOf(ongkir))
        chekout.produks = produks

        chekout.receipt = "recpt"
        chekout.methode = "methode"
        chekout.description = "please quickly"




        val json = Gson().toJson(chekout, Chekout::class.java)
        Log.d("Respon:", "jseon:" + json)
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("extra", json)
        startActivity(intent)
    }


    private fun getShipping(kurir: String) {

        val alamat = myDb.daoAddress().getByStatus(true)

        val origin = "501"
        val destination = "" + alamat!!.id_city.toString()
        val berat = 1000

        ApiConfigAddress.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object : Callback<ResponOngkir> {
            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {
                if (response.isSuccessful) {

                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()) {
                        displayShipping(result[0].code.toUpperCase(), result[0].costs)
                    }


                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("Error", "gagal memuat data:" + t.message)
            }

        })
    }


    var ongkir = "0"
    var kurir = ""
    var jasaKirim = ""
    private fun displayShipping(_kurir: String, arrayList: ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices) {
            val ongkir = arrayList[i]
            if (i == 0) {
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }
        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        kurir = _kurir
        jasaKirim = arrayOngkir[0].service

      val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir, _kurir, object : AdapterKurir.Listeners {
            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for (ongkir in arrayOngkir) {
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                kurir = _kurir
                jasaKirim = data.service
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }


    fun checkAddress() {

        if (myDb.daoAddress().getByStatus(true) != null) {
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAddress().getByStatus(true)!!
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = a.address + ", " + a.city + ", " + a.codepos + ", (" + a.type + ")"
            btn_Address.text = "Change Address"

            getShipping("JNE")
        } else {
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_Address.text = "Add Address"
        }
    }
    fun setTotal(shipping: String) {
        tv_ongkir.text = Helper().changeRupiah(shipping)
        tv_total.text = Helper().changeRupiah(Integer.valueOf(shipping) + totalHarga)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkAddress()
        super.onResume()
    }
}