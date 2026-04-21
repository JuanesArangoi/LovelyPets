# LovelyPets - App de Adopción y Cuidado de Mascotas

LovelyPets es una aplicación moderna de Android diseñada para conectar a mascotas en busca de un hogar con personas dispuestas a adoptarlas.  
La aplicación permite gestionar publicaciones, explorar diferentes categorías de animales y administrar perfiles de usuario de forma intuitiva.

---

## Tecnologías y Arquitectura

El proyecto está construido siguiendo las mejores prácticas de **Modern Android Development (MAD):**

- **Lenguaje:** Kotlin (100%)  
- **UI:** Jetpack Compose (arquitectura declarativa)  
- **Arquitectura:** MVVM con Clean Architecture  
- **Inyección de Dependencias:** Hilt (Dagger)  
- **Navegación:** Navigation Compose 2.8+ con soporte Type-Safety (Kotlin Serialization)  
- **Persistencia:** DataStore para preferencias de usuario  

---

## Estructura del Proyecto

```plaintext
com.example.demoapp/
├── core/                 # Configuración global (Navegación, Temas, DI Modules)
│   ├── navigation/       # Grafos de navegación (AppNavigation, UserNavigation)
│   └── theme/            # Sistema de diseño (Color, Type, Shape)
├── data/                 # Repositorios y fuentes de datos (API/Local)
├── features/             # Módulos por funcionalidad
│   ├── auth/             # Login, Registro, Recuperación de contraseña
│   ├── dashboard/        # Pantalla principal y navegación interna
│   ├── pets/             # Gestión de mascotas (Listado, Detalle, Edición)
│   └── profile/          # Gestión de perfil de usuario
└── MainActivity.kt       # Punto de entrada de la aplicación

```

## Características Principales

-  **Autenticación completa:** Login, registro y recuperación de cuenta con códigos de verificación  
-  **Exploración por categorías:** Filtro dinámico de mascotas (Perros, Gatos, etc.)  
-  **Gestión de publicaciones:** Crear, editar y eliminar anuncios de mascotas  
-  **Detalle de mascota:** Información expandida, contacto con el dueño y galería de fotos  
-  **Panel de moderación:** Funciones exclusivas para administradores *(en desarrollo)*  
-  **Modo oscuro/claro:** Soporte completo mediante `DemoAppTheme`  

---


## Configuración para Desarrolladores

Para ejecutar este proyecto localmente:

**1. Clonar el repositorio:**
 ```bash
git clone https://github.com/tu-usuario/LovelyPets.git
 ```

**2. Sincronizar Gradle:**  
El proyecto utiliza Kotlin Serialization y Hilt, por lo que requiere una sincronización inicial para generar las clases necesarias.

**3. Ejecutar:**
Conecta un dispositivo físico o emulador con API 24+.

---

**Notas de Integración Reciente (Merge)**

Se implementó el sistema de navegación Type-Safe, eliminando el uso de Strings para rutas.

Se integró UserNavigation dentro del Dashboard para separar la lógica de autenticación de la navegación interna de la app.
