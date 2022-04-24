package com.example.kotlinproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinproject.R
import com.example.kotlinproject.helper.Helper
import com.google.gson.Gson
import com.inyongtisto.tokoonline.adapter.AdapterBank
import com.inyongtisto.tokoonline.app.ApiConfig
import com.inyongtisto.tokoonline.model.Bank
import com.inyongtisto.tokoonline.model.Chekout
import com.inyongtisto.tokoonline.model.ResponModel
import com.inyongtisto.tokoonline.model.Transaksi
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Helper().setToolbar(this, toolbar, "Payment")

        displayBank()
    }

    fun displayBank() {
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA", "092093871237", "Ali Assalem", R.drawable.logo_bca))
        arrBank.add(Bank("BRI", "86721349128", "Ali Assalem", R.drawable.logo_bri))
        arrBank.add(Bank("Mandiri", "02394870329", " Ali Assalem", R.drawable.logo_madiri))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_data.layoutManager = layoutManager
        rv_data.adapter = AdapterBank(arrBank, object : AdapterBank.Listeners {
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })
    }

    fun bayar(bank: Bank) {
        val json = intent.getStringExtra("extra")!!.toString()
        val chekout = Gson().fromJson(json, Chekout::class.java)
        chekout.bank = bank.name

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()

        ApiConfig.instanceRetrofit.chekout(chekout).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
                Toast.makeText(this@PaymentActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                if (!response.isSuccessful) {
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1) {

                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsChekout = Gson().toJson(chekout, Chekout::class.java)

                    val intent = Intent(this@PaymentActivity, SuccessActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("chekout", jsChekout)
                    startActivity(intent)

                } else {
                    error(respon.message)
                    Toast.makeText(this@PaymentActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}