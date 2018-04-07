@file:JvmName("IconHelper")

package com.zighter.zighterandroid.view.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.zighter.zighterandroid.R


fun getIcon(context: Context, type: IconHelperType): Icon {
    var resizePercent = 100
    val drawableRes: Int = when (type) {
        IconHelperType.SIGHT -> R.mipmap.ic_sight
        IconHelperType.CHECKED_SIGHT -> R.mipmap.ic_checked_sight
        IconHelperType.ENDPOINT_START -> {
            resizePercent = 40
            R.mipmap.ic_endpoint_start
        }
        IconHelperType.ENDPOINT_END -> {
            resizePercent = 40
            R.mipmap.ic_endpoint_end
        }
        IconHelperType.CURRENT_LOCATION_ACTIVE -> {
            resizePercent = 50
            R.mipmap.ic_location_active
        }
        IconHelperType.CURRENT_LOCATION_OUTDATED -> {
            resizePercent = 50
            R.mipmap.ic_location_outdated
        }
        else -> throw IllegalStateException("Unknown icon type")
    }

    var bitmap = BitmapFactory.decodeResource(context.resources, drawableRes)
    if (resizePercent != 100) {
        bitmap = Bitmap.createScaledBitmap(bitmap,
                bitmap.width * resizePercent / 100,
                bitmap.height * resizePercent / 100,
                false)
    }
    return IconFactory.getInstance(context).fromBitmap(bitmap)
}

enum class IconHelperType {
    ENDPOINT_START,
    ENDPOINT_END,
    SIGHT,
    CHECKED_SIGHT,
    CURRENT_LOCATION_ACTIVE,
    CURRENT_LOCATION_OUTDATED
}

