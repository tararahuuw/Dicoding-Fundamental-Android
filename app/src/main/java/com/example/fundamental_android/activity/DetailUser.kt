package com.example.fundamental_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.fundamental_android.R
import com.example.fundamental_android.adapter.TabpagerAdapter
import com.example.fundamental_android.api_service.ResponseApi.ResponseDetailUser
import com.example.fundamental_android.model.Hero
import com.example.fundamental_android.view_model.DetailViewModel
import com.example.fundamental_android.view_model.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

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

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.userData.observe(this, { userData ->
            setListUserData(userData)
        })

        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        val data = intent.getParcelableExtra<Hero>("DATA")
        var usernameData = data?.login.toString()

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
    }


}