package com.example.kotlinproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinproject.R
import com.example.kotlinproject.model.Produk
import com.google.gson.Gson
import com.inyongtisto.tokoonline.util.Config
import com.squareup.picasso.Picasso
import android.view.View
import android.widget.*
import com.example.kotlinproject.helper.Helper
import com.example.kotlinproject.model.Favorite
import com.inyongtisto.tokoonline.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.toolbar_custom.*


class DetailProductActivity : AppCompatActivity() {

    lateinit var product: Produk
    val favorite=Favorite()

    lateinit var myDb: MyDatabase

    lateinit var tv_name: TextView
    lateinit var tv_price: TextView
    lateinit var tv_description: TextView
    lateinit var image: ImageView
    lateinit var btn_cart: RelativeLayout
    lateinit var btn_favorit: RelativeLayout
    lateinit var img_favorite:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        myDb = MyDatabase.getInstance(this)!! // call database

        init()
        checkCart()
        getInfo()
        mainButton()
        check_imgFav()

    }

    fun init(){
        tv_name=findViewById(R.id.tv_name)
        tv_price=findViewById(R.id.tv_price)
        tv_description=findViewById(R.id.tv_description)
        image=findViewById(R.id.image)
        btn_cart=findViewById(R.id.btn_cart)
        btn_favorit=findViewById(R.id.btn_favorit)
        img_favorite=findViewById(R.id.img_fav)

    }

    private fun check_imgFav(){
        val data = myDb.daoFavorite().getFavorite(product.id)
        if (data==null){
            img_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
        else{
            img_favorite.setImageResource(R.drawable.ic_baseline_favorite_blue)
        }
    }

    private fun mainButton() {
        btn_cart.setOnClickListener {
            val data = myDb.daoCart().getProduk(product.id)
            if (data == null) {
                insert()
            } else {
                data.qty += 1
                update(data)
            }
        }

        btn_favorit.setOnClickListener {
            val data = myDb.daoFavorite().getFavorite(product.id)
            if(data==null){
                insert_Favorite()
                img_favorite.setImageResource(R.drawable.ic_baseline_favorite_blue)
            }else{
                delete_item_favorite(data)
                img_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
    }


    private fun insert() {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoCart().insert(product) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkCart()
                Toast.makeText(this, "Added to cart successfully", Toast.LENGTH_SHORT).show()
            })
    }

    private fun insert_Favorite(){

        //current favorite
        favorite.id=product.id
        favorite.name=product.name
        favorite.category_id=product.category_id
        favorite.description=product.description
        favorite.price=product.price
        favorite.image=product.image
        favorite.selected=true
        favorite.created_at=product.created_at
        favorite.updated_at=product.updated_at
        favorite.discount=product.discount
        favorite.qty=product.qty
        favorite.selected=true

        CompositeDisposable().add(Observable.fromCallable{myDb.daoFavorite().insert(favorite)}
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Toast.makeText(this, "Added to favorite successfully", Toast.LENGTH_SHORT).show()
            })
    }

    private fun delete_item_favorite(favorite: Favorite){
        CompositeDisposable().add(Observable.fromCallable{myDb.daoFavorite().delete(favorite)}
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
            })
    }

    private fun update(data: Produk) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoCart().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkCart()
                Toast.makeText(this, "Added to cart successfully", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkCart() {
        val dataKranjang = myDb.daoCart().getAll()

        if (dataKranjang.isNotEmpty()) {
            div_angka.visibility = View.VISIBLE
            tv_angka.text = dataKranjang.size.toString()
        } else {
            div_angka.visibility = View.GONE
        }
    }


    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        product= Gson().fromJson<Produk>(data, Produk::class.java)




        // set Value
        tv_name.text = product.name
        tv_price.text = Helper().changeRupiah(product.price)
        tv_description.text = product.description

        val img = Config.productUrl + product.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .resize(400, 400)
            .into(image)

        // setToolbar
        Helper().setToolbar(this, toolbar, product.name)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}