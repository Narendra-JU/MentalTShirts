package com.example.mentalapp.utility.firebase.util

import org.json.JSONArray

operator fun <T> JSONArray.iterator(): Iterator<T> =
    (0 until this.length()).asSequence().map { this.get(it) as T }.iterator()