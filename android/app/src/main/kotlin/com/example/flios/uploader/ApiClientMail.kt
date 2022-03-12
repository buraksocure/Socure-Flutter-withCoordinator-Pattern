package com.socure.idplus.uploader

import com.example.flios.BuildConfig
import com.socure.idplus.Constants
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientMail {

    fun getRetrofitSandbox(): Retrofit {
        val certPinner = CertificatePinner.Builder()
            .add(
                Constants.CERT_PIN_DOMAIN,
                Constants.CERT_PIN_SOCURE
            )
            .add(
                Constants.CERT_PIN_DOMAIN,
                Constants.CERT_PIN_AMAZON_INTERMEDIATE
            )
            .add(
                Constants.CERT_PIN_DOMAIN,
                Constants.CERT_PIN_AMAZON_ROOT
            )
            .build()
        val client = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(loggingInterceptor)
        client.connectTimeout(50, TimeUnit.SECONDS)
        client.readTimeout(50, TimeUnit.SECONDS)
        client.writeTimeout(50, TimeUnit.SECONDS)
        client.certificatePinner(certPinner)
        return Retrofit.Builder()
            .baseUrl("https://service.socure.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
}