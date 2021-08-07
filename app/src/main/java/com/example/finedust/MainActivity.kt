package com.example.finedust

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.finedust.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource


class MainActivity : AppCompatActivity() {


    private var cancellationTokenSource : CancellationTokenSource ? =null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initVariables()
        requestLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()

        cancellationTokenSource?.cancel()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS &&
                    grantResults[0]==PackageManager.PERMISSION_GRANTED

        if(!locationPermissionGranted){
            finish()
        }else{
            // fetchData

                cancellationTokenSource = CancellationTokenSource()


            fusedLocationProviderClient.getCurrentLocation(
                //high accuracy 는 정확도는 높지만 배터리를 많이 먹음
                // 여기서는 어차피 한번만 요청 하므로 그냥 씀
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token

                ).addOnSuccessListener { location ->
                    binding.textview.text = "${location.latitude} , ${location.longitude}"
            }

        }
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


    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
    }
}