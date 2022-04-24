package com.example.kotlinproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.HistoryActivity
import com.example.kotlinproject.activity.LoginActivity
import com.example.kotlinproject.helper.SharedPref

class AkunFragment : Fragment() {

    lateinit var s: SharedPref
    lateinit var btnLogout: TextView
    lateinit var tvNama: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView
    lateinit var btn_history: RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View= inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)
        s = SharedPref(requireActivity())

        setData()
        mainButton()
        return view
    }

    fun mainButton() {
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        btn_history.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }
    }

    fun setData() {

        if(s.getStatusLogin()) {
            val user = s.getUser()!!

            tvNama.text = user.name
            tvEmail.text = user.email
            tvPhone.text = user.phone
        }
    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        btn_history = view.findViewById(R.id.btn_history)
    }
}