package com.chugunova.tinkofffinteh.api

import com.chugunova.tinkofffinteh.model.Result
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserAPI {

    @GET("/{section}/{page}")
    fun getPost(
        @Path("section") section: String,
        @Path("page") page: Int,
        @Query("json") json: Boolean
    ): Observable<Result>

    companion object Factory {
        fun create(): UserAPI {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://developerslife.ru")
                .build()
            return retrofit.create(UserAPI::class.java);
        }
    }
}