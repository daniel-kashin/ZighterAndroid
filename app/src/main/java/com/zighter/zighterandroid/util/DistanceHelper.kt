@file:JvmName("DistanceHelper")

package com.zighter.zighterandroid.util

private const val EARTH_RADIUS_IN_KM = 6371

fun distanceInMeters(latitude1: Double,
                     latitude2: Double,
                     longitude1: Double,
                     longitude2: Double): Double {
    val latDistance = Math.toRadians(latitude2 - latitude1)
    val lonDistance = Math.toRadians(longitude2 - longitude1)
    val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    var distance = EARTH_RADIUS_IN_KM.toDouble() * c * 1000.0 // convert to meters

    distance = Math.pow(distance, 2.0)

    return Math.sqrt(distance)
}
