package com.example.kotlinproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kotlinproject.MainActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.helper.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.inyongtisto.tokoonline.app.ApiConfig
import com.inyongtisto.tokoonline.model.ResponModel
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var s: SharedPref
    lateinit var fcm: String
    lateinit var layoutEmail: TextInputLayout
    lateinit var layoutPassword:TextInputLayout
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPassword:TextInputEditText
    lateinit var btnSignIn: Button
    lateinit var txtSignUp:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)
        init()
        getFcm()

        btnSignIn.setOnClickListener(View.OnClickListener {
            login()
        })
    }

    private fun init() {
        layoutPassword = findViewById(R.id.txtLayoutPasswordSignIn)
        layoutEmail = findViewById(R.id.txtLayoutEmailSignIn)
        txtPassword = findViewById(R.id.txtPasswordSignIn)
        txtSignUp = findViewById(R.id.txtSignUp)
        txtEmail = findViewById(R.id.txtEmailSignIn)
        btnSignIn = findViewById(R.id.btnSignIn)

        txtSignUp.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
            finish()
        })
        btnSignIn.setOnClickListener(View.OnClickListener { v: View? ->
            //validate fields first
            if (validate()) {
                login()
            }
        })
        txtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!txtEmail.getText().toString().isEmpty()) {
                    layoutEmail.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txtPassword.getText().toString().length > 7) {
                    layoutPassword.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


    private fun validate(): Boolean {
        if (txtEmail.text.toString().isEmpty()) {
            layoutEmail.isErrorEnabled = true
            layoutEmail.error = "Email is Required"
            return false
        }
        if (txtPassword.text.toString().length < 8) {
            layoutPassword.isErrorEnabled = true
            layoutPassword.error = "Required at least 8 characters"
            return false
        }
        return true
    }

    private fun getFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Respon", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            fcm = token.toString()
            // Log and toast
            Log.d("respon fcm:", token.toString())
        })
    }

    fun login() {

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()

        ApiConfig.instanceRetrofit.login(txtEmail.text.toString(), txtPassword.text.toString(),fcm)
            .enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val respon = response.body()!!
                if (respon.success == 1) {
                    s.setStatusLogin(true)
                    s.setUser(respon.user)


                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@LoginActivity, "Welcome " + respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    loading.dismiss()
                    error(respon.message.toString())
                }
            }
        })
    }

    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }
}