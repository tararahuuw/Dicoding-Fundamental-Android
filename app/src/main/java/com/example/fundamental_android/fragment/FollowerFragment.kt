package com.example.fundamental_android.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamental_android.R
import com.example.fundamental_android.activity.DetailUser
import com.example.fundamental_android.adapter.AdapterHero
import com.example.fundamental_android.api_service.ApiConfig
import com.example.fundamental_android.api_service.ResponseApi.ResponseFollowers
import com.example.fundamental_android.model.Hero
import com.example.fundamental_android.view_model.FollowerViewModel
import com.example.fundamental_android.view_model.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {

    private lateinit var rvFollowers : RecyclerView
    private var listHero = ArrayList<Hero>()
    private lateinit var progressBar: ProgressBar
    private lateinit var followerViewModel : FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(ARG_NAME)

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)

        rvFollowers = view.findViewById(R.id.recycleViewFollowers)
        progressBar = view.findViewById(R.id.progressBarFollowers)

        followerViewModel.userData.observe(viewLifecycleOwner, { userData ->
            setListUserData(userData)
        })

        followerViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        followerViewModel.loadDataUser(name.toString())

    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListUserData(userData: ArrayList<ResponseFollowers>?) {
        userData?.forEach { i ->
            val hero = Hero(i?.login, i?.avatarUrl)
            listHero.add(hero)
        }
        showRecycleView()
    }

//    private fun getDataUsername(usernameUser: String?) {
//        val client = ApiConfig.getApiService().getFollowers(usernameUser.toString())
//        client.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
//            override fun onResponse(
//                call: Call<ArrayList<ResponseFollowers>>,
//                response: Response<ArrayList<ResponseFollowers>>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//
//                        responseBody.forEach { i ->
//                            val hero = Hero(i?.login, i?.avatarUrl)
//                            listHero.add(hero)
//                        }
//
////                        showRecycleView()
//                    }
//                } else {
////                    progressBar.visibility = View.GONE
//                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<ArrayList<ResponseFollowers>>, t: Throwable) {
////                progressBar.visibility = View.GONE
//                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    private fun showRecycleView() {
        rvFollowers.layoutManager = LinearLayoutManager(context)
        val listHeroAdapter = AdapterHero(listHero)
        rvFollowers.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : AdapterHero.OnItemClickCallback {
            override fun onItemClicked(data: Hero) {
                val bundle = Bundle()
                bundle.putString("Username", data.login)
                val fragFollowing = FollowingFragment()
                val fragFollowers = FollowerFragment()
                fragFollowing.setArguments(bundle)
                fragFollowers.setArguments(bundle)

                val moveWithObjectIntent = Intent(context, DetailUser::class.java)
                moveWithObjectIntent.putExtra("DATA", data)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    companion object {
        const val ARG_NAME = "username"
    }
}