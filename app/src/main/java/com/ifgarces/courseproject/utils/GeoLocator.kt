package com.ifgarces.courseproject.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat


class GeoLocator : LocationListener {
    private lateinit var locationManager: LocationManager
    lateinit var application: Application
    private val locationPermissionCode = 2
    var curLat   :Double = -1.0
    var curLong  :Double = -1.0

    // Gets the current user location
    public fun getLocation(activity: Activity) {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                application.baseContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: consider overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission.
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
                return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    // Once the current location is returned by requestLocationUpdates, we set curLat and curLong
    override fun onLocationChanged(location: Location) {
        curLat = location.latitude
        curLong = location.longitude
    }

    override fun onProviderDisabled(provider :String) {
        super.onProviderDisabled(provider)
    }

    override fun onProviderEnabled(provider :String) {
        super.onProviderEnabled(provider)
    }

    override fun onStatusChanged(provider :String?, status :Int, extras :Bundle?) {
        //super.onStatusChanged(provider, status, extras)
    }

    /*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(application.baseContext, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(application.baseContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}