package com.example.fundamental_android.view_model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamental_android.api_service.ApiConfig
import com.example.fundamental_android.api_service.ResponseApi.ResponseFollowers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {

    private val _userData = MutableLiveData<ArrayList<ResponseFollowers>>()
    val userData : LiveData<ArrayList<ResponseFollowers>> = _userData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadDataUser(dataUser : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(dataUser)
        client.enqueue(object : Callback<ArrayList<ResponseFollowers>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseFollowers>>,
                response: Response<ArrayList<ResponseFollowers>>
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
            override fun onFailure(call: Call<ArrayList<ResponseFollowers>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}