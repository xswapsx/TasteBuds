package com.swapy.tastebuds.di

import com.swapy.tastebuds.api.RecipeWebService
import com.swapy.tastebuds.util.CommonUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(3600L, TimeUnit.SECONDS)
            .writeTimeout(3600L, TimeUnit.SECONDS).readTimeout(3600L, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CommonUtil.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideLoginWebService(retrofit: Retrofit): RecipeWebService {
        return retrofit.create(RecipeWebService::class.java)
    }

}