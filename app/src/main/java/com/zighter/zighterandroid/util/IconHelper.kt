@file:JvmName("IconHelper")

package com.zighter.zighterandroid.util

import android.content.Context

import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.zighter.zighterandroid.R


fun getIcon(context: Context, type: IconHelperType): Icon {
    val drawableRes: Int = when (type) {
        IconHelperType.SIGHT -> R.drawable.ic_navigation_black
        IconHelperType.ENDPOINT -> R.drawable.ic_sight_black
        IconHelperType.CHECKED_SIGHT -> R.drawable.ic_checked_sight
        IconHelperType.CURRENT_LOCATION -> R.mipmap.ic_accessibility
        IconHelperType.CURRENT_LOCATION_OFFLINE -> R.mipmap.ic_accessibility_offline
        else -> throw IllegalStateException("Unknown icon type")
    }

    return IconFactory.getInstance(context).fromResource(drawableRes)
}

enum class IconHelperType {
    ENDPOINT,
    SIGHT,
    CHECKED_SIGHT,
    CURRENT_LOCATION,
    CURRENT_LOCATION_OFFLINE
}

