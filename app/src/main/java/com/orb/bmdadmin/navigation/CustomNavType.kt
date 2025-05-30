package com.orb.bmdadmin.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.data.ReservedFood
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val FoodType = object : NavType<Foods?>(
        isNullableAllowed = true
    ) {
        override fun get(
            bundle: Bundle,
            key: String
        ): Foods? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Foods? {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Foods?): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: Foods?
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}

object CustomNavType2 {
    val FoodType = object : NavType<ReservedFood>(
        isNullableAllowed = false
    ) {
        override fun get(
            bundle: Bundle,
            key: String
        ): ReservedFood {
            return Json.decodeFromString(bundle.getString(key) ?: return ReservedFood())
        }

        override fun parseValue(value: String): ReservedFood {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ReservedFood): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: ReservedFood
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}