package com.example.kotlinproject.activity

import android.app.ProgressDialog
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

class RegisterActivity : AppCompatActivity() {

    lateinit var layoutEmail: TextInputLayout
    lateinit var layoutPassword: TextInputLayout
    lateinit var layoutPhone: TextInputLayout
    lateinit var layoutName: TextInputLayout
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPassword: TextInputEditText
    lateinit var txtPhone: TextInputEditText
    lateinit var txtName: TextInputEditText
    lateinit var txtSignIn: TextView
    lateinit var btnSignUp: Button

    lateinit var fcm: String
    lateinit var s: SharedPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        s = SharedPref(this)
        init()

        getFcm()

        btnSignUp.setOnClickListener {
            register()
        }


    }
    private fun init() {
        layoutPassword = findViewById(R.id.txtLayoutPasswordSignUp)
        layoutPassword = findViewById(R.id.txtLayoutPasswordSignUp)
        layoutEmail = findViewById(R.id.txtLayoutEmailSignUp)
        layoutName = findViewById(R.id.txtLayoutNameSignUp)
        layoutPhone = findViewById(R.id.txtLayoutPhoneSignUp)
        txtPassword = findViewById(R.id.txtPasswordSignUp)
        txtPhone = findViewById(R.id.txtPhoneSignUp)
        txtSignIn = findViewById(R.id.txtSignIn)
        txtEmail = findViewById(R.id.txtEmailSignUp)
        txtName = findViewById(R.id.txtNameSignUp)
        btnSignUp = findViewById(R.id.btnSignUp)

        txtSignIn.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }
        btnSignUp.setOnClickListener { v: View? ->
            //validate fields first
            if (validate()) {
                register()
                //  Toast.makeText(getApplicationContext(),Common.currentUser.getApi_token().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        txtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!txtEmail.text.toString().isEmpty()) {
                    layoutEmail.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!txtName.text.toString().isEmpty()) {
                    layoutName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txtPassword.text.toString().length > 7) {
                    layoutPassword.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txtPhone.text.toString() == txtPassword.text.toString()) {
                    layoutPhone.isErrorEnabled = false
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
        if (txtName.text.toString().isEmpty()) {
            layoutName.isErrorEnabled = true
            layoutName.error = "Name is Required"
            return false
        }
        if (txtPhone.text.toString().isEmpty()) {
            layoutPhone.isErrorEnabled = true
            layoutPhone.error = "Phone is Required"
            return false
        }
        if (txtPassword.text.toString().length < 8) {
            layoutPassword.isErrorEnabled = true
            layoutPassword.error = "Required at least 8 characters"
            return false
        }

        return true
    }

    fun register() {

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()
        ApiConfig.instanceRetrofit.register(txtName.text.toString(), txtEmail.text.toString(), txtPhone.text.toString(), txtPassword.text.toString(),fcm)
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
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "Welcome " + respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    loading.dismiss()
                    error(respon.message.toString())
                }
            }
        })


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

    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }
}