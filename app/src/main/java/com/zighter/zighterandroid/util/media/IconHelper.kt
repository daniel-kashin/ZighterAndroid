@file:JvmName("IconHelper")

package com.zighter.zighterandroid.util.media

import android.content.Context

import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.zighter.zighterandroid.R


fun getIcon(context: Context, type: IconHelperType): Icon {
    val drawableRes: Int = when (type) {
        IconHelperType.SIGHT -> R.drawable.ic_navigation_black
        IconHelperType.ENDPOINT -> R.drawable.ic_sight_black
        IconHelperType.CHECKED_SIGHT -> R.drawable.ic_checked_sight
        IconHelperType.CURRENT_LOCATION_ACTIVE -> R.mipmap.ic_accessibility
        IconHelperType.CURRENT_LOCATION_OUTDATED -> R.mipmap.ic_accessibility_outdated
        else -> throw IllegalStateException("Unknown icon type")
    }

    return IconFactory.getInstance(context).fromResource(drawableRes)
}

enum class IconHelperType {
    ENDPOINT,
    SIGHT,
    CHECKED_SIGHT,
    CURRENT_LOCATION_ACTIVE,
    CURRENT_LOCATION_OUTDATED
}

