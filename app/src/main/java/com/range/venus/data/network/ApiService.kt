package com.range.venus.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.range.venus.data.model.DebitResponse
import com.range.venus.data.model.PaymentsResponse
import com.range.venus.data.model.TableResponse
import com.range.venus.data.model.UserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("gt_reg.php")
    suspend fun checkLogin(@FieldMap params: Map<String, String>): Response<UserResponse>

    @FormUrlEncoded
    @POST("get_tulovlar.php")
    suspend fun getPayments(@FieldMap params: Map<String, String>): Response<PaymentsResponse>

    @FormUrlEncoded
    @POST("get_sum.php")
    suspend fun getDebit(@FieldMap params: Map<String, String>): Response<DebitResponse>

    @FormUrlEncoded
    @POST("get_dars_jadvali.php")
    suspend fun getTable(@FieldMap params: Map<String, String>): Response<TableResponse>

    companion object {
        operator fun invoke(): ApiService {
            val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://www.balans-corp.com/kontrakt_hisobi/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }
}