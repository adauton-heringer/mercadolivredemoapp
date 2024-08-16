package com.adauton.mercadolivredemo.di

import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.services.SearchServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    private const val BASE_URL = "https://api.mercadolibre.com/sites/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApi =
        retrofit.create(SearchApi::class.java)

    @Singleton
    @Provides
    fun proviceSearchService(searchApi: SearchApi) = SearchServiceImpl(searchApi)
}