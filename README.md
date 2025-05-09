# 🎟️ RifaApp — Aplicación de Rifas Android

Una aplicación Android desarrollada con **Jetpack Compose** y **SQLite** para gestionar rifas: creación, inscripción y visualización de boletos disponibles.

---

## 🧱 Arquitectura

- **Jetpack Compose** para la UI declarativa.
- **SQLite** local como base de datos persistente.
- Arquitectura basada en componentes simples (`Activity` + `Composable`).
- Separación lógica de UI y persistencia.
- **Sin DataStore** ni ViewModel por simplicidad (pero fácilmente integrable).

---

## 🗂️ Estructura del Proyecto

📁 app/
┣ 📄 BD.kt # Base de datos SQLite y funciones CRUD
┣ 📄 MainActivity.kt # Entrada principal de la app
┣ 📄 Principal.kt # Lista de rifas + búsqueda + navegación
┣ 📄 RifaDetalle.kt # Pantalla de edición de rifa (boletos)
┣ 📄 RifaNew.kt # Crear nueva rifa
┣ 📁 res/ # Recursos (strings, colores, etc.)
┣ 📄 AndroidManifest.xml # Declaración de actividades


---

## 🚀 Funcionalidades

- 🔍 Buscar rifas por nombre.
- ➕ Crear nuevas rifas con nombre y fecha.
- 🧾 Editar rifas y tachar números comprados.
- 🗑️ Eliminar rifas completas.
- ✅ Guardado persistente con SQLite.
- 🎨 UI moderna y responsiva usando Compose.
- 🔁 Actualización automática de la pantalla principal al volver de otras vistas.

---

## 🧪 Pruebas

### Unitarias (`BDTest.kt`)

- Pruebas de:
  - Inserción y lectura de rifas
  - Filtro por nombre
  - Modificación de boletos comprados
  - Eliminación completa

> Se encuentran en `src/test/java/com.example.rifadb/BDTest.kt`

---

## 🛠️ Requisitos

- Android Studio Hedgehog o superior
- JDK 17
- Gradle 8.2+
- API 24+

---

## 📦 Dependencias Clave

- `androidx.compose.*`
- `androidx.lifecycle:lifecycle-runtime-compose`
- `androidx.sqlite`
- `androidx.activity:activity-compose`

---


## 📥 Clonar el repositorio

```bash
git clone https://github.com/cristiandavella/rifa-aplicacion-movil.git
cd rifa-aplicacion-movil

