package com.example.finedust

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.finedust.data.Repository
import com.example.finedust.data.model.airquality.Grade
import com.example.finedust.data.model.airquality.MeasuredValue
import com.example.finedust.data.model.monitoringstation.MonitoringStation
import com.example.finedust.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception


class MainActivity : AppCompatActivity() {


    private var cancellationTokenSource : CancellationTokenSource ? =null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val scope = MainScope()


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindViews()
        initVariables()
        requestLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()

        cancellationTokenSource?.cancel()
        scope.cancel()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!locationPermissionGranted) {
                finish()
            } else {
                val backgroundLocationPermissionGranted =
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                val shouldShowBackgroundPermissionRationale =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                if (!backgroundLocationPermissionGranted && shouldShowBackgroundPermissionRationale) {
                    showBackgroundLocationPermissionRationaleDialog()
                } else {
                    fetchAirQualityData()
                }
            }
        } else {
            if (!locationPermissionGranted) {
                finish()
            } else {
                fetchAirQualityData()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun showBackgroundLocationPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setMessage("??? ????????? ??????????????? ?????? ?????? ????????? ${packageManager.backgroundPermissionOptionLabel} ???????????? ?????????.")
            .setPositiveButton("????????????") { dialog, _ ->
                requestBackgroundLocationPermissions()
                dialog.dismiss()
            }
            .setNegativeButton("????????????") { dialog, _ ->
                fetchAirQualityData()
                dialog.dismiss()
            }
            .show()
    }
    private fun initVariables(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestBackgroundLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }
    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData(){
        cancellationTokenSource = CancellationTokenSource()


        fusedLocationProviderClient.getCurrentLocation(
            //high accuracy ??? ???????????? ????????? ???????????? ?????? ??????
            // ???????????? ????????? ????????? ?????? ????????? ?????? ???
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token

        ).addOnSuccessListener { location ->
            scope.launch {
                binding.errorDescriptionTextView.visibility = View.GONE
                try{
                    val monitoringStation =
                            Repository.getNearbyMonitoringStation(location.latitude,location.longitude)



                    val measuredValue =
                            Repository.getLatestAirQualityData(monitoringStation!!.stationName!!)

                    displayAirQualityData(monitoringStation,measuredValue!!)
                }catch (exception : Exception){
                    binding.errorDescriptionTextView.visibility = View.VISIBLE
                    binding.contentsLayout.alpha =0F
                }finally {
                    binding.progressBar.visibility = View.GONE

                    binding.refresh.isRefreshing = false



                }



            }
        }
    }


    private fun bindViews(){
        binding.refresh.setOnRefreshListener {
            fetchAirQualityData()
        }
    }
    @SuppressLint("SetTextI18n")
    fun displayAirQualityData(monitoringStation: MonitoringStation, measuredValue: MeasuredValue) {

        binding.contentsLayout.animate()
                .alpha(1F)
                .start()
        binding.measuringStationNameTextView.text = monitoringStation.stationName
        binding.measuringStationAddress.text = "????????? ??????: ${monitoringStation.addr}"

        (measuredValue.khaiGrade ?: Grade.UNKNOWN).let { grade ->
            binding.root.setBackgroundResource(grade.colorResId)
            binding.totalGraddeLabelTextView.text = grade.label
            binding.totalGradeEmojiTextView.text = grade.emoji
        }

        with(measuredValue) {
            binding.fineDustInformationTextView.text =
                    "????????????: $pm10Value ???/??? ${(pm10Grade ?: Grade.UNKNOWN).emoji}"
            binding.ultraFineDuistInformationTextView.text =
                    "???????????????: $pm25Value ???/??? ${(pm25Grade ?: Grade.UNKNOWN).emoji}"

            with(binding.so2Item) {
                labelTextView.text = "???????????????"
                gradeTextView.text = (so2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$so2Value ppm"
            }

            with(binding.coItem) {
                labelTextView.text = "???????????????"
                gradeTextView.text = (coGrade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$coValue ppm"
            }

            with(binding.o3Item) {
                labelTextView.text = "??????"
                gradeTextView.text = (o3Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$o3Value ppm"
            }

            with(binding.no2Item) {
                labelTextView.text = "???????????????"
                gradeTextView.text = (no2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$no2Value ppm"
            }
        }
    }
    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101

    }
}