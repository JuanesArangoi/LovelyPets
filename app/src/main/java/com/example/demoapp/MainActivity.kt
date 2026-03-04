package com.example.demoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.demoapp.features.home.HomeScreen
import com.example.demoapp.features.login.ChangePasswordScreen

//import com.example.demoapp.features.Home.HomeScreen
import com.example.demoapp.features.login.LoginScreen
import com.example.demoapp.features.login.Loginmejorado
import com.example.demoapp.features.login.VerifyCodeScreen
import com.example.demoapp.features.login.SendCodeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VerifyCodeScreen()
        }
    }
}

