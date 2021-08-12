package com.example.finedust.data

import com.example.finedust.BuildConfig
import com.example.finedust.data.model.airquality.MeasuredValue
import com.example.finedust.data.model.monitoringstation.MonitoringStation
import com.example.finedust.data.services.AirKoreaApiService
import com.example.finedust.data.services.KakaoLocalApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object Repository {

    suspend fun getNearbyMonitoringStation(latitude: Double, longtitude: Double) : MonitoringStation?{

        val tmCoordinates = kakaoLocalApiService
                .getTmCoordinates(longtitude, latitude)
                .body()
                ?.documents
                ?.firstOrNull() // 즉 첫번째 값을 가저오고 없으면 널

        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y
        return airKoreaApiService
            .getNearbyMonitoringStation(tmX!!, tmY!!)
            .body()
            ?.response
            ?.body
            ?.monitoringStations
            ?.minByOrNull { it.tm ?: Double.MAX_VALUE }


    }

    suspend fun getLatestAirQualityData(stationName : String): MeasuredValue? =
            airKoreaApiService
                    .getRealtimeAirQualities(stationName)
                    .body()
                    ?.response
                    ?.body
                    ?.measuredValues
                    ?.firstOrNull()
    private val kakaoLocalApiService: KakaoLocalApiService by lazy {

        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()

        //클라이언트를 넣는 이유가 logging 을 추가하려구

    }

    private val airKoreaApiService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.AIR_KOREA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()

        //클라이언트를 넣는 이유가 logging 을 추가하려구

    }


    private fun buildHttpClient():OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()


}


