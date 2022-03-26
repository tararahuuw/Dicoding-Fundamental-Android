package com.example.fundamental_android.activity

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.fundamental_android.Database.Favorites
import com.example.fundamental_android.Database.FavoritesDB
import com.example.fundamental_android.R
import com.example.fundamental_android.Settings.SettingPreferences
import com.example.fundamental_android.adapter.TabpagerAdapter
import com.example.fundamental_android.api_service.ResponseApi.ResponseDetailUser
import com.example.fundamental_android.model.Hero
import com.example.fundamental_android.view_model.DetailViewModel
import com.example.fundamental_android.view_model.SettingsViewModel
import com.example.fundamental_android.view_model.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailUser : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var Name : TextView
    private lateinit var Username : TextView
    private lateinit var Photo : CircleImageView
    private lateinit var Location : TextView
    private lateinit var Repository : TextView
    private lateinit var Company : TextView
    private lateinit var Followers : TextView
    private lateinit var Following : TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var buttonFavorites : FloatingActionButton
    private lateinit var UrlAvatar : String
    private lateinit var usernameData : String
    private var available : Boolean = false
    private val db by lazy { FavoritesDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        Name = findViewById(R.id.name_detail)
        Username = findViewById(R.id.username_detail)
        Photo = findViewById(R.id.photo_detail)
        Location = findViewById(R.id.location_detail)
        Repository = findViewById(R.id.repository_detail)
        Company = findViewById(R.id.company_detail)
        Followers = findViewById(R.id.followers_detail)
        Following = findViewById(R.id.following_detail)
        progressBar = findViewById(R.id.progressBarDetail)
        buttonFavorites = findViewById(R.id.button_favorite)
        val pref = SettingPreferences.getInstance(dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingsViewModel::class.java
        )
        settingsViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    Name.setTextColor("#ffffff".toColor())
                    Username.setTextColor("#ffffff".toColor())
                    Location.setTextColor("#ffffff".toColor())
                    Repository.setTextColor("#ffffff".toColor())
                    Company.setTextColor("#ffffff".toColor())
                    Followers.setTextColor("#ffffff".toColor())
                    Following.setTextColor("#ffffff".toColor())
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.userData.observe(this, { userData ->
            setListUserData(userData)
        })
        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        val data = intent.getParcelableExtra<Hero>("DATA")
        usernameData = data?.login.toString()
        val tabpagerAdapter = TabpagerAdapter(this)
        tabpagerAdapter.username = usernameData
        val viewPager : ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = tabpagerAdapter
        val tabs : TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        detailViewModel.loadDataUser(usernameData)
        CoroutineScope(Dispatchers.IO).launch {
            var listUser = db.favoritesDao().findUserWithName(usernameData)
            if (listUser.size > 0) {
                available = true
                buttonFavorites.setImageResource(R.drawable.favorite)
            }
            else {
                available = false
                buttonFavorites.setImageResource(R.drawable.favorite_border)
            }
        }
        FavoritesButton()
    }

    private fun FavoritesButton() {
        buttonFavorites.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (available == true) {
                    db.favoritesDao().deleteByUsername(usernameData)
                    buttonFavorites.setImageResource(R.drawable.favorite_border)
                    available = false
                }
                else {
                    db.favoritesDao().addFavorites(
                        Favorites(
                            0,
                            usernameData,
                            UrlAvatar,
                        )
                    )
                    buttonFavorites.setImageResource(R.drawable.favorite)
                    available = true
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListUserData(listUser: ResponseDetailUser?) {
        Name.text = "Nama : " + listUser?.name.toString()
        Username.text = "Username : " + listUser?.login.toString()
        Location.text = "Location : " + listUser?.location.toString()
        Repository.text = "Repository : " + listUser?.publicRepos.toString()
        Company.text = "Company : " + listUser?.company.toString()
        Followers.text = "Follower : " + listUser?.followers.toString()
        Following.text = "Following : " + listUser?.following.toString()
        Picasso.get().load(listUser?.avatarUrl.toString()).into(Photo);
        UrlAvatar = listUser?.avatarUrl.toString()
    }
}

fun String.toColor(): Int = Color.parseColor(this)
