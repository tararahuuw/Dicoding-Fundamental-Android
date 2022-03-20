package com.example.fundamental_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamental_android.R
import com.example.fundamental_android.activity.DetailUser
import com.example.fundamental_android.adapter.AdapterHero
import com.example.fundamental_android.api_service.ResponseApi.ResponseFollowing
import com.example.fundamental_android.model.Hero
import com.example.fundamental_android.view_model.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var rvFollowing : RecyclerView
    private var listHero = ArrayList<Hero>()
    private lateinit var progressBar: ProgressBar
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(FollowerFragment.ARG_NAME)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)

        rvFollowing = view.findViewById(R.id.recycleViewFollowing)
        progressBar = view.findViewById(R.id.progressBarFollowing)

        followingViewModel.userData.observe(viewLifecycleOwner, { userData ->
            setListUserData(userData)
        })

        followingViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        followingViewModel.loadDataUser(name.toString())
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListUserData(userData: ArrayList<ResponseFollowing>) {
        userData?.forEach { i ->
            val hero = Hero(i?.login, i?.avatarUrl)
            listHero.add(hero)
        }
        showRecycleView()
    }

//    private fun getDataUsername(usernameUser: String?) {
//        progressBar.visibility = View.VISIBLE
//        val client = ApiConfig.getApiService().getFollowing(usernameUser.toString())
//        client.enqueue(object : Callback<ArrayList<ResponseFollowing>> {
//            override fun onResponse(
//                call: Call<ArrayList<ResponseFollowing>>,
//                response: Response<ArrayList<ResponseFollowing>>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//
//                        responseBody.forEach { i ->
//                            val hero = Hero(i?.login, i?.avatarUrl)
//                            listHero.add(hero)
//                        }
//                        showRecycleView()
//                    }
//                } else {
//                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<ArrayList<ResponseFollowing>>, t: Throwable) {
//                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
//            }
//        })
//    }
//
    private fun showRecycleView() {
        rvFollowing.layoutManager = LinearLayoutManager(context)
        val listHeroAdapter = AdapterHero(listHero)
        rvFollowing.adapter = listHeroAdapter

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