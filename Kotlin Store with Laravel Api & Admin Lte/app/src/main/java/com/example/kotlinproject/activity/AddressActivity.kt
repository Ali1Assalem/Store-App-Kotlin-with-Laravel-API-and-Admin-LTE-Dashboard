package com.example.kotlinproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.AdapterAddress
import com.example.kotlinproject.helper.Helper
import com.inyongtisto.tokoonline.model.Address
import com.inyongtisto.tokoonline.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.toolbar_custom.*

class AddressActivity : AppCompatActivity() {
    lateinit var myDb : MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        Helper().setToolbar(this, toolbar, "Address")
        myDb = MyDatabase.getInstance(this)!!

        mainButton()
        displayAddress()
    }

    private fun displayAddress() {
        val arrayList = myDb.daoAddress().getAll() as ArrayList

        if (arrayList.isEmpty()) div_kosong.visibility = View.VISIBLE
        else div_kosong.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_alamat.adapter = AdapterAddress(arrayList, object : AdapterAddress.Listeners {
            override fun onClicked(data: Address) {
                if (myDb.daoAddress().getByStatus(true) != null){
                    val addressActive = myDb.daoAddress().getByStatus(true)!!
                    addressActive.isSelected = false
                    updateActive(addressActive, data)
                }
            }
        })
        rv_alamat.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: Address, dataNonActive: Address) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAddress().update(dataActive) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateNonActive(dataNonActive)
            })
    }

    private fun updateNonActive(data: Address) {
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAddress().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onBackPressed()
            })
    }

    private fun mainButton() {
        btn_tambahAlamat.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
    }

    override fun onResume() {
        displayAddress()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}