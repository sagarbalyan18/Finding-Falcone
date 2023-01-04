package com.example.findingfalcone.application.injection.modules

import com.example.findingfalcone.BuildConfig
import com.example.findingfalcone.application.LocalProperties.Companion.BASE_URL
import com.example.findingfalcone.data.Service
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        fun buildRetrofit(httpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // return RX compatible responses
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()
    }

    // region Http Clients
    @Provides
    @Singleton
    fun provideOkHttp(logger: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    // endregion

    @Provides
    @Singleton
    fun provideApiService(client: OkHttpClient): Service =
        buildRetrofit(client)
            .create(Service::class.java)
}