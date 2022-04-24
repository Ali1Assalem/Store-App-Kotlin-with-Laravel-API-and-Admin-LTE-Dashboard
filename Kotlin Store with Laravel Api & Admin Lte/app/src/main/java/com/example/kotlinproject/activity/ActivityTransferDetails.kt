package com.example.kotlinproject.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.AdapterProductTransaction
import com.example.kotlinproject.helper.Helper
import com.google.gson.Gson
import com.inyongtisto.tokoonline.app.ApiConfig
import com.inyongtisto.tokoonline.model.DetailTransaksi
import com.inyongtisto.tokoonline.model.ResponModel
import com.inyongtisto.tokoonline.model.Transaksi
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_transfer_details.*
import kotlinx.android.synthetic.main.toolbar_custom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityTransferDetails : AppCompatActivity() {
    var transaksi = Transaksi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_details)
        Helper().setToolbar(this, toolbar, "Transaction Details")

        val json = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson(json, Transaksi::class.java)

        setData(transaksi)
        displayProduct(transaksi.details)
        mainButton()
    }
    private fun mainButton() {
        btn_batal.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("The transaction will be canceled and cannot be returned!")
                .setConfirmText("Yes, cancel")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    batalTransaksi()
                }
                .setCancelText("Close")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }
    }


    fun batalTransaksi() {
        val loading = SweetAlertDialog(this@ActivityTransferDetails, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()
        ApiConfig.instanceRetrofit.batalChekout(transaksi.id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1) {
                    SweetAlertDialog(this@ActivityTransferDetails, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success...")
                        .setContentText("Transaction successfully canceled")
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            onBackPressed()
                        }
                        .show()

//                    Toast.makeText(this@DetailTransaksiActivity, "Transaksi berhasil di batalkan", Toast.LENGTH_SHORT).show()
//                    onBackPressed()
//                    displayRiwayat(res.transaksis)
                } else {
                    error(res.message)
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

    @SuppressLint("NewApi")
    fun setData(t: Transaksi) {
        tv_status.text = t.status

        val formatBaru = "dd MMMM yyyy, kk:mm:ss"
        tv_tgl.text = Helper().convertTanggal(t.created_at, formatBaru)

        tv_penerima.text = t.name + " - " + t.phone
        tv_alamat.text = t.location_details
        tv_kodeUnik.text = Helper().changeRupiah(t.code_unique)
        tv_totalBelanja.text = Helper().changeRupiah(t.total_price)
        tv_ongkir.text = Helper().changeRupiah(t.shipping)
        tv_total.text = Helper().changeRupiah(t.total_transfer)

        if (t.status != "WAITING") div_footer.visibility = View.GONE

        var color = getColor(R.color.menungu)
        if (t.status == "DONE") color = getColor(R.color.selesai)
        else if (t.status == "CANCELLED") color = getColor(R.color.batal)

        tv_status.setTextColor(color)
    }

    fun displayProduct(transaksis: ArrayList<DetailTransaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_produk.adapter = AdapterProductTransaction(transaksis)
        rv_produk.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}