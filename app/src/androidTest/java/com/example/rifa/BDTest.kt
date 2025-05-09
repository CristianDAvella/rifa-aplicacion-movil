package com.example.rifa

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.rifadb.BD
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BDTest {

    private lateinit var db: BD
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = BD(context)
        db.writableDatabase.execSQL("DELETE FROM rifas")
        db.writableDatabase.execSQL("DELETE FROM numeros")
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertarRifa_creaRifaCon100Numeros() {
        db.insertarRifa("Rifa Test", "2025-12-25")
        val rifas = db.obtenerRifas()
        assertEquals(1, rifas.size)

        val numeros = db.obtenerNumeros("Rifa Test")
        assertEquals(100, numeros.size)
        assertTrue(numeros.all { !it.second }) // todos sin comprar
    }

    @Test
    fun actualizarNumeros_marcaNumerosComprados() {
        db.insertarRifa("Rifa Compras", "2025-06-01")
        val seleccionados = setOf(1, 2, 3, 10, 99)
        db.actualizarNumeros("Rifa Compras", seleccionados)

        val numeros = db.obtenerNumeros("Rifa Compras")
        val marcados = numeros.filter { it.second }.map { it.first }
        assertEquals(seleccionados.sorted(), marcados.sorted())
    }


    @Test
    fun eliminarRifa_eliminaNumerosYLaRifa() {
        db.insertarRifa("Rifa Borrar", "2025-01-01")
        db.eliminarRifa("Rifa Borrar")

        val rifas = db.obtenerRifas()
        val numeros = db.obtenerNumeros("Rifa Borrar")

        assertTrue(rifas.none { it.nombre == "Rifa Borrar" })
        assertTrue(numeros.isEmpty())
    }

    @Test
    fun obtenerRifas_filtraPorNombreCorrectamente() {
        db.insertarRifa("Sorteo", "2025-08-01")
        db.insertarRifa("Lotería", "2025-09-01")
        val resultado = db.obtenerRifas("Sorteo")
        assertEquals(1, resultado.size)
        assertEquals("Sorteo", resultado[0].nombre)
    }
}
