package com.example.fundamental_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class DetailUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val Name: TextView = findViewById(R.id.name_detail)
        val Username:TextView = findViewById(R.id.username_detail)
        val Photo:ImageView = findViewById(R.id.photo_detail)
        val Location:TextView = findViewById(R.id.location_detail)
        val Repository:TextView = findViewById(R.id.repository_detail)
        val Company:TextView = findViewById(R.id.company_detail)
        val Followers:TextView = findViewById(R.id.followers_detail)
        val Following:TextView = findViewById(R.id.following_detail)

        val data = intent.getParcelableExtra<Hero>("DATA")
        Name.text = "Nama : ${data?.name.toString()}"
        Username.text = "Username : ${data?.username.toString()}"
        data?.let { Photo.setImageResource(it.photo) }
        Location.text = "Location : ${data?.location.toString()}"
        Repository.text = "Repository : ${data?.repository.toString()}"
        Company.text = "Company : ${data?.company.toString()}"
        Followers.text = "Followers : ${data?.followers.toString()}"
        Following.text = "Following : ${data?.following.toString()}"
    }
}