package com.example.mentalapp.utility.firebase

import com.example.mentalapp.utility.firebase.RemoteConfigConstants.KEY_ENABLED
import org.json.JSONObject

class MetalUtils {
    companion object {
        fun isRemoteConfigKeyEnabled(remoteConfigKey: String) : Boolean{
            return isRemoteConfigKeyEnabled(remoteConfigKey,KEY_ENABLED)
        }

        fun isRemoteConfigKeyEnabled(remoteConfigKey: String,key:String) : Boolean{
            try{
                val jsonObject = JSONObject(MetalRemoteConfig.getStringConfig(remoteConfigKey))
                return jsonObject.optBoolean(key,false)
            }catch (e:Exception){
                //silent catch
            }
            return false
        }
    }
}