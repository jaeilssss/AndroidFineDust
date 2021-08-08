package com.example.finedust.data.services

import com.example.finedust.BuildConfig
import com.example.finedust.data.model.monitoringstation.MonitoringStation
import com.example.finedust.data.model.monitoringstation.MonitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApiService  {


    @GET("B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}"+
    "&returnType=json")
    suspend fun getNearbyMonitoringStation(
        @Query("tmX") tmX : Double,
        @Query("tmY") tmY : Double
    ):Response<MonitoringStationsResponse>
}