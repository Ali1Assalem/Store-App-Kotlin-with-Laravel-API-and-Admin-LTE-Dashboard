package com.example.kotlinproject.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.DetailProductActivity
import com.example.kotlinproject.helper.Helper
import com.example.kotlinproject.model.Favorite
import com.example.kotlinproject.model.Produk
import com.google.gson.Gson
import com.inyongtisto.tokoonline.util.Config
import com.squareup.picasso.Picasso

class AdapterFavorite(var activity: Activity, var data: ArrayList<Favorite>, var listener: AdapterFavorite.Listeners) : RecyclerView.Adapter<AdapterFavorite.Holder>()  {

    class Holder (view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_name)
        val tvHarga = view.findViewById<TextView>(R.id.tv_price)
        val imgProduk = view.findViewById<ImageView>(R.id.img_product)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return AdapterFavorite.Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val produk = data[position]

        holder.tvNama.text = produk.name
        holder.tvHarga.text = Helper().changeRupiah(produk.price)

        val image = Config.productUrl + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .into(holder.imgProduk)
        holder.layout.setOnClickListener {
            val activiti = Intent(activity, DetailProductActivity::class.java)
            val str = Gson().toJson(data[position], Favorite::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

}