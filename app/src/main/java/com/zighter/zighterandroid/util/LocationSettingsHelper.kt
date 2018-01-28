@file:JvmName("LocationSettingsHelper")

package com.zighter.zighterandroid.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.*
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import android.support.v7.app.AlertDialog
import com.zighter.zighterandroid.R


const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val LOCATION_PERMISSION_REQUEST_CODE = 1223
const val LOCATION_PROVIDER_REQUEST_CODE = 1224

fun isLocationPermissionGranted(context: Context): Boolean {
    val locationPermission = ContextCompat.checkSelfPermission(context.applicationContext, LOCATION_PERMISSION)
    return locationPermission == PackageManager.PERMISSION_GRANTED
}

fun requestOpenPermissionSettings(activity: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", activity.packageName, null)
    val packageManager = activity.packageManager
    if (packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0) {
        AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.open_permission_settings_request_title))
                .setMessage(activity.getString(R.string.open_permission_settings_request_message))
                .setCancelable(true)
                .setPositiveButton(activity.getText(R.string.yes), { dialog, which ->
                    activity.startActivityForResult(intent, LOCATION_PERMISSION_REQUEST_CODE)
                })
                .setNegativeButton(activity.getString(R.string.no), { dialog, which ->
                    // do nothing
                })
                .show()
    } else {
        AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.open_permission_settings_request_message))
                .setCancelable(true)
                .show();
    }
}

fun requestLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(activity,
            arrayOf(LOCATION_PERMISSION),
            LOCATION_PERMISSION_REQUEST_CODE)
}

fun requestOpenGpsSettings(activity: Activity) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    val packageManager = activity.packageManager
    if (packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0) {
        AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.open_gps_settings_request_title))
                .setMessage(activity.getString(R.string.open_gps_settings_request_message))
                .setCancelable(true)
                .setPositiveButton(activity.getText(R.string.yes), { dialog, which ->
                    activity.startActivityForResult(intent, LOCATION_PROVIDER_REQUEST_CODE)
                })
                .setNegativeButton(activity.getString(R.string.no), { dialog, which ->
                    // do nothing
                })
                .show()
    } else {
        AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.open_gps_settings_request_message))
                .setCancelable(true)
                .show();
    }
}

@Throws(SecurityException::class)
fun getLastKnownLocation(locationManager: LocationManager, defaultLocation: Location?): Location? {
    val location: Location? = locationManager.getLastKnownLocation(GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(NETWORK_PROVIDER)
            ?: locationManager.getLastKnownLocation(PASSIVE_PROVIDER)
            ?: defaultLocation

    return location
}

fun shouldHandleLocation(locationManager: LocationManager, provider: String): Boolean {
    var result = false
    when (provider) {
        GPS_PROVIDER -> result = true
        NETWORK_PROVIDER -> if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            result = true
        }
    }
    return result
}

fun isGpsLocationEnabled(locationManager: LocationManager): Boolean {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun isNetworkLocationEnabled(locationManager: LocationManager): Boolean {
    return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun ensureGpsEnabled(activity: Activity) {
    if (!isGpsLocationEnabled(activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager)) {
        // TODO
        requestOpenGpsSettings(activity)
    }
}
