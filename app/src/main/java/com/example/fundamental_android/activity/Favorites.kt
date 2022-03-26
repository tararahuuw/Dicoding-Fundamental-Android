package com.example.fundamental_android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamental_android.Database.FavoritesDB
import com.example.fundamental_android.R
import com.example.fundamental_android.adapter.AdapterHero
import com.example.fundamental_android.fragment.FollowerFragment
import com.example.fundamental_android.model.Hero
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.*

class Favorites : AppCompatActivity() {

    private val db by lazy { FavoritesDB(this) }
    lateinit var heroAdapter: AdapterHero
    private lateinit var favoritesRV : RecyclerView
    private var listHero = ArrayList<Hero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        favoritesRV = findViewById(R.id.favoritesRV)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            var listUser = db.favoritesDao().getFavorites()
            listHero = ArrayList<Hero>()
            listUser?.forEach { i ->
                val hero = Hero(i?.username, i?.urlAvatar)
                listHero.add(hero)
            }
            heroAdapter.setData(listHero)
            withContext(Dispatchers.Main) {
                heroAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecyclerView () {
        heroAdapter = AdapterHero(
            arrayListOf())
        favoritesRV.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = heroAdapter
        }
        heroAdapter.setOnItemClickCallback(object : AdapterHero.OnItemClickCallback {
            override fun onItemClicked(data: Hero) {
                val bundle = Bundle()
                bundle.putString(
                    "usernameBundle",
                    data.login.toString()
                )
                val frag2 = FollowerFragment()
                frag2.arguments = bundle
                val moveWithObjectIntent = Intent(this@Favorites, DetailUser::class.java)
                moveWithObjectIntent.putExtra("DATA", data)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}