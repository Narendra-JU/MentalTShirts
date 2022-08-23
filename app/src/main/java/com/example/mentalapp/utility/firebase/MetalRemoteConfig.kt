package com.example.mentalapp.utility.firebase

import android.util.Log
import com.example.mentalapp.BuildConfig
import com.example.mentalapp.utility.firebase.RemoteConfigConstants.KEY_APIS
import com.example.mentalapp.utility.firebase.RemoteConfigConstants.KEY_API_PARAMS_APPENDER
import com.example.mentalapp.utility.firebase.RemoteConfigConstants.KEY_DYNAMIC_HOME_SECTION
import com.example.mentalapp.utility.firebase.RemoteConfigConstants.KEY_ENABLED
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap


class MetalRemoteConfig @JvmOverloads constructor(timeinMilliSec: Long = CACHE_EXPIRY_TIME) {
    private var cacheExpiryTime: Long = 0;
    private var onCompleteListener: OnCompleteListener<Boolean>? = null
    private var onFailureListener: OnFailureListener? = null
    init {
        initializeDefValues()
        Log.d(TAG, ": Metal_REMOTE CONFIG ENABLED")
        try {
            cacheExpiryTime = timeinMilliSec
            setupRemoteConfig()
        } catch (e: Exception) {
            Log.i(TAG, "Exception:$e ")
        }
    }

    private fun setupRemoteConfig() {
        val mFirebaseRemoteConfig = instance
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(cacheExpiry).build()
        mFirebaseRemoteConfig?.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig?.setDefaultsAsync(firebaseDefHashMap)
        if (mFirebaseRemoteConfig != null) {
            fetchConfiguration(mFirebaseRemoteConfig)
        }

    }

    private fun fetchConfiguration(mFirebaseRemoteConfig: FirebaseRemoteConfig) {
        Log.i(TAG, "fetchConfiguration: Fetch Started")
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "fetchConfiguration: Fetch Successful")
                mFirebaseRemoteConfig.activate()
                onCompleteListener?.onComplete(task)
                prepareFirebaseApiParamsIfApplicable()

            } else {
                onFailureListener?.onFailure(Exception())
            }


        }
    }

    private val cacheExpiry: Long
        get() = if (BuildConfig.DEBUG) {
            CACHE_EXPIRY_IMMEDIATE_FETCH
        } else {
            cacheExpiryTime
        }

    fun setOnCompleteListener(onCompleteListener: OnCompleteListener<Boolean>?) {
        this.onCompleteListener = onCompleteListener
    }

    fun setOnFailureListener(onFailureListener: OnFailureListener?) {
        onFailureListener?.let {
            this.onFailureListener = it
        }
    }

    companion object {
        const val TAG = "MetalRemoteConfig"
        const val CACHE_EXPIRY_IMMEDIATE_FETCH: Long = 0
        const val CACHE_EXPIRY_TIME: Long = 1200
        var firebaseDefHashMap: HashMap<String, Any> = hashMapOf()
        var apiParamAppenderHashmap: ConcurrentHashMap<String, HashMap<String, String>> =
            ConcurrentHashMap()

        fun getDefValue(key: String): Any? {
            return firebaseDefHashMap.let {
                firebaseDefHashMap[key]
            }
        }

        private val instance: FirebaseRemoteConfig?
            get() = try {
                FirebaseRemoteConfig.getInstance()
            } catch (e: Exception) {
                null
            }

        fun getStringConfig(key: String): String {
            return instance?.getString(key) ?: getDefValue(key).toString()
        }


    }








    private fun initializeDefValues() {
        firebaseDefHashMap[KEY_DYNAMIC_HOME_SECTION] = "{\n" +
                "\"enabled\":false,\n" +
                "\"sequence\":[\n" +
                "\"heroBanner\",\n" +
                "\"gridListing\"\n" +
                "]" +
                "}"
    }


    fun prepareFirebaseApiParamsIfApplicable() {
        try {
            apiParamAppenderHashmap.clear()
            val firebaseApiJsonString = getStringConfig(KEY_API_PARAMS_APPENDER)
            val jsonObject = JSONObject(firebaseApiJsonString)
            if (jsonObject.optBoolean(KEY_ENABLED, false)) {
                val optJSONObject = jsonObject.optJSONObject(KEY_APIS)
                optJSONObject?.let {
                    val keys: Iterator<*> = optJSONObject.keys()
                    while (keys.hasNext()) {
                        val key = keys.next() as String
                        if (optJSONObject[key] is JSONObject) {
                            val apiObject = optJSONObject[key] as JSONObject
                            Log.i(
                                TAG,
                                "prepareFirebaseApiParamsIfApplicable: $key -----> $apiObject"
                            )
                            val innerKeys: Iterator<*> = apiObject.keys()
                            val hashMap: HashMap<String, String> = hashMapOf()
                            while (innerKeys.hasNext()) {
                                val innerKey = innerKeys.next() as String
                                if (apiObject[innerKey] is String) {
                                    Log.i(
                                        TAG,
                                        "prepareFirebaseApiParamsIfApplicable: KeyValue $innerKey -----> ${
                                            apiObject.optString(
                                                innerKey,
                                                ""
                                            )
                                        }"
                                    )
                                    if (innerKey.isEmpty()) {
                                        hashMap[innerKey] = apiObject.optString(innerKey, "")
                                    }
                                } else if (apiObject[innerKey] is JSONObject) {
                                    Log.i(
                                        TAG,
                                        "prepareFirebaseApiParamsIfApplicable: KEY-VALUE $innerKey -----> Json Object ${
                                            apiObject.optJSONObject(innerKey)
                                        }"
                                    )
                                    if (innerKey.isEmpty()) {
                                        hashMap[innerKey] =
                                            apiObject.optJSONObject(innerKey).toString()
                                    }
                                }
                                apiParamAppenderHashmap[key] = hashMap
                            }
                        }

                    }

                }
            } else {
                Log.i(
                    TAG,
                    "prepareFirebaseApiParamsIfApplicable: api params appender false $firebaseApiJsonString"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "prepareFirebaseApiParamsIfApplicable: Exception $e")
        }
    }

}