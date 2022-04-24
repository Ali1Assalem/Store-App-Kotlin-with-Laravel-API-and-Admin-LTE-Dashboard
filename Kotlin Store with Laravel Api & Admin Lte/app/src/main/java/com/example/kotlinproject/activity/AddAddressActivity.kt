package com.example.kotlinproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.kotlinproject.R
import com.example.kotlinproject.helper.Helper
import com.inyongtisto.tokoonline.app.ApiConfigAddress
import com.inyongtisto.tokoonline.model.Address
import com.inyongtisto.tokoonline.model.ModelAlamat
import com.inyongtisto.tokoonline.model.ResponModel
import com.inyongtisto.tokoonline.room.MyDatabase
import com.inyongtisto.tokoonline.util.ApiKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressActivity : AppCompatActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var city = ModelAlamat.Provinsi()
    var districts = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        Helper().setToolbar(this, toolbar, "Add Address")

        mainButton()
        getProvinsi()
    }

    private fun mainButton() {
        btn_simpan.setOnClickListener {
            save()
        }
    }

    private fun getProvinsi() {
        ApiConfigAddress.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_province.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Select Province")

                    val listProvinsi = res.rajaongkir.results
                    for (prov in listProvinsi) {
                        arryString.add(prov.province)
                    }

                    val adapter = ArrayAdapter<Any>(this@AddAddressActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter
                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                provinsi = listProvinsi[position - 1]
                                val idProv = listProvinsi[position - 1].province_id
                                getKota(idProv)
                            }
                        }
                    }

                } else {
                    Toast.makeText(applicationContext,"failed to load data:" + response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getKota(id: String) {
        pb.visibility = View.VISIBLE
        ApiConfigAddress.instanceRetrofit.getKota(  ApiKey.key,id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.rajaongkir.results

                    val arryString = ArrayList<String>()
                    arryString.add("Select City")
                    for (kota in listArray) {
                        arryString.add(kota.city_name)
                    }

                    val adapter = ArrayAdapter<Any>(this@AddAddressActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter
                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                city = listArray[position - 1]
                                val idKota = listArray[position - 1].city_id
                                edt_kodePos.setText(idKota)
                                //getKecamatan(idKota)
                            }
                        }
                    }
                } else {
                    Toast.makeText(applicationContext,"failed to load data:" + response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getKecamatan(id: Int) {
        pb.visibility = View.VISIBLE
        ApiConfigAddress.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE
                    val res = response.body()!!
                    val listArray = res.districts

                    val arryString = ArrayList<String>()
                    arryString.add("Select District")
                    for (data in listArray) {
                        arryString.add(data.name)
                    }

                    val adapter = ArrayAdapter<Any>(this@AddAddressActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                districts = listArray[position - 1]
                            }
                        }
                    }
                } else {
                    Toast.makeText(applicationContext,"failed to load data:" + response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun save() {
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.province_id == "0") {
            toast("Please select a province")
            return
        }

        if (city.city_id == "0") {
            toast("Please select City")
            return
        }

//        if (districts.id == 0) {
//            toast("Please select District")
//            return
//        }

        val address = Address()
        address.name = edt_nama.text.toString()
        address.type = edt_type.text.toString()
        address.phone = edt_phone.text.toString()
        address.address = edt_alamat.text.toString()
        address.codepos = edt_kodePos.text.toString()

        address.id_provinsi = Integer.valueOf(provinsi.province_id)
        address.provinsi = provinsi.province
        address.id_city = Integer.valueOf(city.city_id)
        address.city = city.city_name
       // address.id_districts = districts.id
       // address.districts = districts.name

//
//        val address = Address()
//        address.name = edt_nama.text.toString()
//        address.type = edt_type.text.toString()
//        address.phone = edt_phone.text.toString()
//        address.address = edt_alamat.text.toString()
//        address.codepos = edt_kodePos.text.toString()
//
//        address.id_provinsi = 1
//        address.provinsi = "syria"
//        address.id_city = 2
//        address.city = "homs"
//        address.id_districts = 3
//        address.districts ="street-12-homs"

        insert(address)
    }

    private fun insert(data: Address) {
       val myDb = MyDatabase.getInstance(this)!!
       if (myDb.daoAddress().getByStatus(true) == null){
           data.isSelected = true
       }
       CompositeDisposable().add(Observable.fromCallable { myDb.daoAddress().insert(data) }
           .subscribeOn(Schedulers.computation())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe {
               toast("Insert data success")
               onBackPressed()
           })
   }


    fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText) {
        editText.error = "Columns cannot be empty"
        editText.requestFocus()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}