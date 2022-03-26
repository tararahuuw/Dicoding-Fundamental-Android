package com.example.fundamental_android.api_service
import com.example.fundamental_android.api_service.ResponseApi.ResponseDetailUser
import com.example.fundamental_android.api_service.ResponseApi.ResponseFollowers
import com.example.fundamental_android.api_service.ResponseApi.ResponseFollowing
import com.example.fundamental_android.api_service.ResponseApi.ResponseUsersSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUserSearch(
        @Query("q") username : String
    ): Call<ResponseUsersSearch>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<ResponseFollowers>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<ResponseFollowing>>
}