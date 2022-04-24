package com.example.kotlinproject.helper;

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun changeRupiah(string: String): String {
        return NumberFormat.getCurrencyInstance(Locale("uk", "ID")).format(Integer.valueOf(string))
    }

    fun changeRupiah(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("en", "ID")).format(value)
    }

    fun changeRupiah(value: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("en", "ID")).format(value)
    }

    fun setToolbar(activity: Activity, toolbar: Toolbar, title: String) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun convertTanggal(tgl: String, formatBaru: String, fromatLama: String = "yyyy-MM-dd kk:mm:ss") :String{
        val dateFormat = SimpleDateFormat(fromatLama)
        val confert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        return dateFormat.format(confert)
    }
}