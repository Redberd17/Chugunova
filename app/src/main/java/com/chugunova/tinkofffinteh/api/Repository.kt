package com.chugunova.tinkofffinteh.api

import com.chugunova.tinkofffinteh.model.Result
import io.reactivex.Observable

class PostRepository(private val apiService: UserAPI) {
    fun searchUsers(section: String, page: Int): Observable<Result> {
        return apiService.getPost(section, page, true)
    }
}

object PostRepositoryProvider {
    fun providePostRepository(apiService: UserAPI): PostRepository {
        return PostRepository(apiService)
    }
}