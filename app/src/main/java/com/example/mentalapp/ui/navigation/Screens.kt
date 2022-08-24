package com.example.mentalapp.ui.navigation

import androidx.annotation.DrawableRes
import com.example.mentalapp.R

sealed class Screens(
    val route:String,
    @DrawableRes val icon:Int,
    @DrawableRes val selectedIcon: Int
) {
    object PLP: Screens(
        route = "PLP",
        icon = R.drawable.ic_baseline_recycling_24,
        selectedIcon = R.drawable.ic_baseline_recycling_24
    )

    object PDP:Screens(
        route = "PDP",
        icon = R.drawable.ic_baseline_recycling_24,
        selectedIcon = R.drawable.ic_baseline_recycling_24
    )

    fun withArgs(vararg args:Any):String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }


}