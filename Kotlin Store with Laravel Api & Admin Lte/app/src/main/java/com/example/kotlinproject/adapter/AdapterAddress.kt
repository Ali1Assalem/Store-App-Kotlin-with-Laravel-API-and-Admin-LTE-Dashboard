package com.example.kotlinproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.inyongtisto.tokoonline.model.Address

class AdapterAddress(var data: ArrayList<Address>, var listener: Listeners) : RecyclerView.Adapter<AdapterAddress.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvAddress = view.findViewById<TextView>(R.id.tv_address)
        val layout = view.findViewById<CardView>(R.id.layout)
        val rd = view.findViewById<RadioButton>(R.id.rd_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]

        holder.rd.isChecked = a.isSelected
        holder.tvName.text = a.name
        holder.tvPhone.text = a.phone
        holder.tvAddress.text = a.address + ", " + a.city + ", " + a.districts + ", " + a.codepos + ", (" + a.type + ")"

        holder.rd.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }

        holder.layout.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners {
        fun onClicked(data: Address)
    }

}