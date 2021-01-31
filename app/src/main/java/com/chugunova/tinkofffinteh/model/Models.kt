package com.chugunova.tinkofffinteh.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("votes") val votes: Int,
    @SerializedName("author") val author: String,
    @SerializedName("date") val date: String,
    @SerializedName("gifURL") val gifURL: String,
    @SerializedName("gifSize") val gifSizeIn: Int,
    @SerializedName("previewURL") val previewURL: String,
    @SerializedName("videoURL") val videoURL: String,
    @SerializedName("videoPath") val videoPath: String,
    @SerializedName("videoSize") val videoSize: Int,
    @SerializedName("type") val type: String,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String,
    @SerializedName("commentsCount") val commentsCount: Int,
    @SerializedName("fileSize") val fileSize: Int,
    @SerializedName("canVote") val canVote: Boolean
)

data class Result(val result: List<Post>, val totalCount: Int)

data class SavedPosts(val url: String, val description: String)