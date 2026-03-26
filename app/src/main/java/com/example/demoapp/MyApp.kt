package com.example.demoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase principal de la aplicación, anotada con @HiltAndroidApp
 * para inicializar Hilt y habilitar la inyección de dependencias.
 */
@HiltAndroidApp
class MyApp : Application()
