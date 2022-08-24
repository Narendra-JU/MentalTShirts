package com.example.mentalapp.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.mentalapp.ui.theme.MentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentalApp() {
    MentalAppTheme {
        val navController = rememberNavController()
    }
}