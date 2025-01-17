package com.hischool.hischool.kantin

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.FoodRecommendation
import com.hischool.hischool.data.entity.Kantin
import com.hischool.hischool.utils.ButtonHelper
import com.hischool.hischool.utils.KeyboardHelper
import com.hischool.hischool.utils.ShimmerHelper
import kotlinx.android.synthetic.main.activity_kantin.*

class KantinActivity : AppCompatActivity() {
    private val firestore = Firebase.firestore

    private val kantinAdapter = KantinAdapter(this)
    private val bannerAdapter = FoodBannerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kantin)

        ButtonHelper.setupBackButton(this, btnKantinBack)

        et_kantin_search.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                KeyboardHelper.hideKeyboard(this)

                Toast.makeText(this, "Searching ${et_kantin_search.text}", Toast.LENGTH_SHORT)
                    .show()

                return@OnEditorActionListener true
            }

            return@OnEditorActionListener false
        })

        rv_list_kantin_container.apply {
            layoutManager = LinearLayoutManager(this@KantinActivity)
            adapter = kantinAdapter
        }

        vp_food_banner_container.apply {
            adapter = bannerAdapter
            pageMargin = 20

            setPadding(50, 20, 50, 20)
        }

        loadBannerData()
        loadKantinData()
    }

    private fun loadKantinData() {
        firestore.collection("kantin").get()
            .addOnSuccessListener {
                val kantins: List<Kantin> = it.toObjects()

                ShimmerHelper.stopShimmer(shimmer_list_kantin)

                kantinAdapter.setKantin(kantins as ArrayList<Kantin>)
            }
    }

    private fun loadBannerData() {
        firestore.collection("recommendation").get()
            .addOnSuccessListener {
                val foodRecommendations: List<FoodRecommendation> = it.toObjects()

                ShimmerHelper.stopShimmer(shimmer_food_banner)

                bannerAdapter.setRecommendation(foodRecommendations as ArrayList<FoodRecommendation>)
            }
    }
}
