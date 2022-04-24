package com.example.kotlinproject.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinproject.MainActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.helper.Helper
import com.google.gson.Gson
import com.inyongtisto.tokoonline.model.Bank
import com.inyongtisto.tokoonline.model.Chekout
import com.inyongtisto.tokoonline.model.Transaksi
import com.inyongtisto.tokoonline.room.MyDatabase
import kotlinx.android.synthetic.main.activity_success.*
import kotlinx.android.synthetic.main.toolbar_custom.*

class SuccessActivity : AppCompatActivity() {
    var nominal = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        Helper().setToolbar(this, toolbar, "Bank transfer")

        setValues()
        mainButton()
    }

    fun mainButton() {
        btn_copyNoRek.setOnClickListener {
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener {
            copyText(nominal.toString())
        }

        btn_cekStatus.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    fun copyText(text: String) {
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)

        Toast.makeText(this, "Text berhasil di Kopi", Toast.LENGTH_LONG).show()
    }

    fun setValues() {
        val jsBank = intent.getStringExtra("bank")
        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("chekout")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val chekout = Gson().fromJson(jsCheckout, Chekout::class.java)

        // delete cart
        val myDb = MyDatabase.getInstance(this)!!
        for (produk in chekout.produks){
            myDb.daoCart().deleteById(produk.id)
        }

        tv_nomorRekening.text = bank.account
        tv_namaPenerima.text = bank.receiver
        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_transfer) + transaksi.code_unique
        tv_nominal.text = Helper().changeRupiah(nominal)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}