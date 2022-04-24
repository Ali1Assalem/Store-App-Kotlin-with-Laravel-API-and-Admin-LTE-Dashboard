package com.example.kotlinproject.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.helper.Helper
import com.example.kotlinproject.model.Produk
import com.inyongtisto.tokoonline.room.MyDatabase
import com.inyongtisto.tokoonline.util.Config
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AdapterCart(var activity: Activity, var data: ArrayList<Produk>, var listener: Listeners) : RecyclerView.Adapter<AdapterCart.Holder>()  {

    class Holder (view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_name)
        val tvHarga = view.findViewById<TextView>(R.id.tv_price)
        val imgProduk = view.findViewById<ImageView>(R.id.img_product)
        val layout = view.findViewById<CardView>(R.id.layout)

        val btnPlus = view.findViewById<ImageView>(R.id.btn_plus)
        val btnMinus = view.findViewById<ImageView>(R.id.btn_minus)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvQty = view.findViewById<TextView>(R.id.tv_qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val produk = data[position]
        val harga = Integer.valueOf(produk.price)

        holder.tvNama.text = produk.name
        holder.tvHarga.text = Helper().changeRupiah(harga * produk.qty)

        var QTY = data[position].qty
        holder.tvQty.text = QTY.toString()

        holder.checkBox.isChecked = produk.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            produk.selected = isChecked
            update(produk)
        }

        val image = Config.productUrl + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .into(holder.imgProduk)


        holder.btnPlus.setOnClickListener {
            QTY++
            produk.qty = QTY
            update(produk)

            holder.tvQty.text = QTY.toString()
            holder.tvHarga.text = Helper().changeRupiah(harga * QTY)
        }

        holder.btnMinus.setOnClickListener {
            if (QTY <= 1) return@setOnClickListener
            QTY--

            produk.qty= QTY
            update(produk)

            holder.tvQty.text = QTY.toString()
            holder.tvHarga.text = Helper().changeRupiah(harga * QTY)
        }

        holder.btnDelete.setOnClickListener {
            delete(produk)
            listener.onDelete(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoCart().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    private fun delete(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoCart().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            })
    }
}