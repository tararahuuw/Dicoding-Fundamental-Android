package com.example.fundamental_android.view_model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamental_android.api_service.ApiConfig
import com.example.fundamental_android.api_service.ResponseApi.ResponseFollowing
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _userData = MutableLiveData<ArrayList<ResponseFollowing>>()
    val userData: LiveData<ArrayList<ResponseFollowing>> = _userData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadDataUser(dataUser: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(dataUser)
        client.enqueue(object : Callback<ArrayList<ResponseFollowing>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseFollowing>>,
                response: Response<ArrayList<ResponseFollowing>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userData.value = responseBody!!
                    }
                } else {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseFollowing>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}
