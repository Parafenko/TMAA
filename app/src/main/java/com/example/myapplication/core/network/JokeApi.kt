package com.example.myapplication.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class JokeResponse(
    val setup: String,
    val punchline: String
)

interface JokeApi {
    @GET("random_joke")
    suspend fun getRandomJoke(): JokeResponse
}

object NetworkClient {
    val jokeApi: JokeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JokeApi::class.java)
    }
}
