package com.orb.bmdadmin.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.orb.bmdadmin.data.Foods
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