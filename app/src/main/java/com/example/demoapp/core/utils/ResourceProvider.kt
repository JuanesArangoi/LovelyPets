package com.example.demoapp.core.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Interfaz que provee acceso a los recursos de strings.
 * Permite que los ViewModels accedan a strings sin depender del contexto de la UI.
 */
interface ResourceProvider {
    fun getString(id: Int): String
    fun getString(id: Int, vararg args: Any): String
}

/**
 * Implementación de ResourceProvider que usa el contexto de la aplicación.
 * Se inyecta vía Hilt en todos los ViewModels que necesiten strings.
 */
class ResourceProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ResourceProvider {
    override fun getString(id: Int): String = context.getString(id)
    override fun getString(id: Int, vararg args: Any): String = context.getString(id, *args)
}
