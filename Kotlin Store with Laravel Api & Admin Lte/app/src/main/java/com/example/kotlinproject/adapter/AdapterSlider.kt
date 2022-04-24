package com.example.kotlinproject.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.example.kotlinproject.R

class AdapterSlider(var data :ArrayList<Int>,var context:Activity?) :PagerAdapter() {
    lateinit var layoutInflater:LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_slider,container,false)

        //init
        val imageView:ImageView
        imageView=view.findViewById(R.id.image)
        imageView.setImageResource(data[position])
        container.addView(view,0)

        return view

    }
    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as View)
    }
}