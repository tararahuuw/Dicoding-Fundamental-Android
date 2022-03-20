package com.example.fundamental_android.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamental_android.fragment.FollowerFragment
import com.example.fundamental_android.R
import com.example.fundamental_android.adapter.AdapterHero
import com.example.fundamental_android.api_service.ResponseApi.ResponseUsersSearch
import com.example.fundamental_android.model.Hero
import com.example.fundamental_android.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var rvHeroes: RecyclerView
    private var listHero = ArrayList<Hero>()
    private lateinit var progressBar:ProgressBar
    private lateinit var mainViewModel : MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvHeroes = findViewById(R.id.rv_heroes)
        progressBar = findViewById(R.id.progressBar)
        rvHeroes.setHasFixedSize(true)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.listUser.observe(this, { listUser ->
            setListUserData(listUser)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }

    private fun setListUserData(listUser: ResponseUsersSearch?) {
        if (listUser?.items?.size == 0) {
            Toast.makeText(this, "Username not found", Toast.LENGTH_LONG).show()
        }
        listUser?.items?.forEach { i ->
            val hero = Hero(i?.login, i?.avatarUrl)
            listHero.add(hero)
        }
        showRecyclerList()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                listHero = ArrayList<Hero>()
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showRecyclerList() {
        rvHeroes.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = AdapterHero(listHero)
        rvHeroes.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : AdapterHero.OnItemClickCallback {
            override fun onItemClicked(data: Hero) {
                val bundle = Bundle()
                bundle.putString(
                    "usernameBundle",
                    data.login.toString()
                )

                val frag2 = FollowerFragment()
                frag2.arguments = bundle

                val moveWithObjectIntent = Intent(this@MainActivity, DetailUser::class.java)
                moveWithObjectIntent.putExtra("DATA", data)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}
