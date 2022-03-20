package com.example.fundamental_android.view_model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamental_android.api_service.ApiConfig
import com.example.fundamental_android.api_service.ResponseApi.ResponseDetailUser
import com.example.fundamental_android.api_service.ResponseApi.ResponseUsersSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<ResponseUsersSearch>()
    val listUser : LiveData<ResponseUsersSearch> = _listUser

    private val _userData = MutableLiveData<ResponseDetailUser>()
    val userData : LiveData<ResponseDetailUser> = _userData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUser(searchInput : String) {
        _isLoading.value = true
        var inputSearch = searchInput.replace(" ", "")
        val client = ApiConfig.getApiService().getUserSearch(inputSearch)
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

    fun loadDataUser(dataUser : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(dataUser)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
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
            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

}
