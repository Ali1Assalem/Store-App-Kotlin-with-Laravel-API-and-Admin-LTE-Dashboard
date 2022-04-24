package com.example.kotlinproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.AdapterProduct
import com.example.kotlinproject.adapter.AdapterSlider
import com.example.kotlinproject.model.Produk
import com.inyongtisto.tokoonline.app.ApiConfig
import com.inyongtisto.tokoonline.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


class HomeFragment : Fragment() {

    lateinit var rvProduct: RecyclerView
    val imageList=ArrayList<SlideModel>()

    lateinit var imageSlider:ImageSlider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_home,container,false)

        imageSlider=view.findViewById(R.id.slider)


        init(view)
        getProduk()

        return view
    }

    private var listProduk: ArrayList<Produk> = ArrayList()
    fun getProduk() {
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduk = ArrayList<Produk>()
                    for (p in res.produks) {
                        //p.discount = 100000
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }
        })
    }

    fun displayProduk() {
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.a)
        arrSlider.add(R.drawable.c)
        arrSlider.add(R.drawable.d)


        val manager = GridLayoutManager(context, 2)
        rvProduct.setLayoutManager(manager)

        rvProduct.adapter = AdapterProduct(requireActivity(), listProduk)

    }

    fun init(view:View){
        imageList.add(SlideModel(R.drawable.a,""))
        imageList.add(SlideModel(R.drawable.c,""))
        imageList.add(SlideModel(R.drawable.d,""))

        imageSlider.setImageList(imageList,ScaleTypes.FIT)

        rvProduct = view.findViewById(R.id.rv_product)
    }

}