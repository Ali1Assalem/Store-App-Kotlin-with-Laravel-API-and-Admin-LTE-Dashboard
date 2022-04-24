package com.example.kotlinproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.DeliveryActivity
import com.example.kotlinproject.activity.LoginActivity
import com.example.kotlinproject.adapter.AdapterCart
import com.example.kotlinproject.helper.SharedPref
import com.example.kotlinproject.model.Produk
import com.inyongtisto.tokoonline.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class KernajangFragment : Fragment() {

    lateinit var myDb: MyDatabase
    lateinit var s: SharedPref



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_kernajang, container, false)
        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharedPref(requireActivity())

        mainButton()
        return view
    }

    lateinit var btnDelete: ImageView
    lateinit var rvProduct: RecyclerView
    lateinit var tvTotal: TextView
    lateinit var btnBuy: TextView
    lateinit var cbAll: CheckBox

    private fun init(view: View) {
        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduct = view.findViewById(R.id.rv_product)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBuy = view.findViewById(R.id.btn_bayar)
        cbAll = view.findViewById(R.id.cb_all)
    }



    lateinit var adapter: AdapterCart
    var listProduct= ArrayList<Produk>()
    private fun displayProduk() {
        listProduct = myDb.daoCart().getAll() as ArrayList
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterCart(requireActivity(), listProduct, object : AdapterCart.Listeners {
            override fun onUpdate() {
                countTotal()
            }

            override fun onDelete(position: Int) {
                listProduct.removeAt(position)
                adapter.notifyDataSetChanged()
                countTotal()
            }
        })
        rvProduct.adapter = adapter
        rvProduct.layoutManager = layoutManager
    }

    var totalPrice = 0
    fun countTotal() {
        val listProduct = myDb.daoCart().getAll() as ArrayList
        totalPrice = 0
        var isSelectedAll = true
        for (product in listProduct) {
            if (product.selected) {
                val price = Integer.valueOf(product.price)
                totalPrice += (price * product.qty)
            } else {
                isSelectedAll = false
            }
        }

        cbAll.isChecked = isSelectedAll
        tvTotal.text =com.example.kotlinproject.helper.Helper().changeRupiah(totalPrice)
    }

    private fun mainButton() {
        btnDelete.setOnClickListener {
            val listDelete = ArrayList<Produk>()
            for (p in listProduct) {
                if (p.selected) listDelete.add(p)
            }

            delete(listDelete)
        }

        btnBuy.setOnClickListener {

            if (s.getStatusLogin()) {
                var isThereProduk = false
                for (p in listProduct) {
                    if (p.selected) isThereProduk = true
                }

                if (isThereProduk) {
                    val intent = Intent(requireActivity(), DeliveryActivity::class.java)
                    intent.putExtra("extra", "" + totalPrice)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "No product selected", Toast.LENGTH_SHORT).show()
                }
            } else {
                requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }

        cbAll.setOnClickListener {
            for (i in listProduct.indices) {
                val product = listProduct[i]
                product.selected = cbAll.isChecked
                listProduct[i] = product
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun delete(data: ArrayList<Produk>) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoCart().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listProduct.clear()
                listProduct.addAll(myDb.daoCart().getAll() as ArrayList)
                adapter.notifyDataSetChanged()
                tvTotal.setText("")
            })
    }


    override fun onResume() {
        displayProduk()
        countTotal()
        super.onResume()
    }

}