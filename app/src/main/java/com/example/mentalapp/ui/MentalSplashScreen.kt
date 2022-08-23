package com.example.mentalapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mentalapp.R
import com.example.mentalapp.ui.MentalSplashScreen.Companion.KEY_SEQUENCE
import com.example.mentalapp.ui.theme.MentalAppTheme
import com.example.mentalapp.util.iterator
import com.example.mentalapp.utility.firebase.MetalRemoteConfig
import com.example.mentalapp.utility.firebase.RemoteConfigConstants
import org.json.JSONObject

class MentalSplashScreen : ComponentActivity() {

    private var isLoading= true

    companion object {
        const val HERO_BANNER = "heroBanner"
        const val GRID_LISTING = "gridListing"
        const val KEY_SEQUENCE = "sequence"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{
            isLoading
        }
        super.onCreate(savedInstanceState)
        var homeScreenList = ArrayList<String>()
        setContent {


            MentalAppTheme {


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var startRender by remember {
                        mutableStateOf(false)
                    }

                    val metalRemoteConfig = MetalRemoteConfig(3000)
                    metalRemoteConfig.setOnFailureListener{

                    }
                    metalRemoteConfig.setOnCompleteListener{
                        if (it.isSuccessful){
                            homeScreenList = initializeHomeSection()
                            isLoading =false
                            startRender = true
                        }
                    }
                    MainScreen(startRender,homeScreenList)



                }

                createNotificationChannel()
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = getString(R.string.notification_channel_id)
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name , importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun MainScreen(
    startRender:Boolean,
    homeScreenList:ArrayList<String>
){
    if (startRender){
        Column {
            for (i in 0 until homeScreenList.size){
                when(homeScreenList[i]){
                    MentalSplashScreen.HERO_BANNER -> {
                        Greeting("Hero Banner")
                    }
                    MentalSplashScreen.GRID_LISTING -> {
                        Greeting("Grid Listing")
                    }
                    else -> {
                        Greeting("Error has occoured")
                    }

                }
            }
        }

    }
}

fun initializeHomeSection():ArrayList<String>{
    val sectionArray= initializeWithDefault()
    return getHomeScreenRemoteConfig(sectionArray)
}

fun getHomeScreenRemoteConfig(homeSections:ArrayList<String>):ArrayList<String>{
    try{
        val jsonObject= JSONObject(MetalRemoteConfig.getStringConfig(RemoteConfigConstants.KEY_DYNAMIC_HOME_SECTION))
        val isEnabled = jsonObject.optBoolean(RemoteConfigConstants.KEY_ENABLED,false)
        val finalArray = arrayListOf<String>()
        if (isEnabled){
            val arraySections = jsonObject.optJSONArray(KEY_SEQUENCE)
            if (arraySections != null && arraySections.length()>0){
                val tempArray = arraySections.iterator<String>()
                for (i in tempArray){
                    finalArray.add(i)
                }
            }
        }
        return finalArray
    }catch (e:Exception){
        homeSections.clear()
        return initializeWithDefault()
    }
}







fun initializeWithDefault():ArrayList<String> {
    val homeSection = ArrayList<String>()
    homeSection.add(MentalSplashScreen.HERO_BANNER)
    homeSection.add(MentalSplashScreen.GRID_LISTING)
    homeSection.add(MentalSplashScreen.HERO_BANNER)
    return homeSection
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MentalAppTheme {
        Greeting("Android")
    }
}