package com.example.kotlinproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.AdapterFavorite
import com.example.kotlinproject.model.Favorite
import com.inyongtisto.tokoonline.room.MyDatabase
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment() {

    lateinit var myDb: MyDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view :View= inflater.inflate(R.layout.fragment_favorite, container, false)

        init(view)

        myDb = MyDatabase.getInstance(requireActivity())!!
        return view

    }

    lateinit var btnDelete: ImageView
    lateinit var rvProduct: RecyclerView


    private fun init(view: View) {
        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduct = view.findViewById(R.id.rv_product)
    }



    lateinit var adapter: AdapterFavorite
    var listFavorites= ArrayList<Favorite>()
    private fun displayProduct() {
        listFavorites = myDb.daoFavorite().getAll() as ArrayList
        Collections.reverse(listFavorites);
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterFavorite(requireActivity(), listFavorites, object : AdapterFavorite.Listeners {
            override fun onUpdate() {
                //countTotal()
            }

            override fun onDelete(position: Int) {
                listFavorites.removeAt(position)
                adapter.notifyDataSetChanged()
                //countTotal()
            }
        })
        rvProduct.adapter = adapter
        rvProduct.layoutManager = layoutManager
    }

    override fun onResume() {
        displayProduct()
        super.onResume()
    }
}