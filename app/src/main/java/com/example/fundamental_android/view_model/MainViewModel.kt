package com.example.fundamental_android.view_model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamental_android.api_service.ApiConfig
import com.example.fundamental_android.api_service.ResponseApi.ResponseUsersSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<ResponseUsersSearch>()
    val listUser : LiveData<ResponseUsersSearch> = _listUser

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUser(dataUser : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserSearch(dataUser)
        client.enqueue(object : Callback<ResponseUsersSearch> {
            override fun onResponse(
                call: Call<ResponseUsersSearch>,
                response: Response<ResponseUsersSearch>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody!!
                    }
                } else {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ResponseUsersSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}