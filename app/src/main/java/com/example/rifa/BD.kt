package com.example.rifadb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BD(context: Context) : SQLiteOpenHelper(context, "Rifas.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE rifas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                inscritos INTEGER,
                fecha TEXT
            )
        """
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS rifas")
        onCreate(db)
    }

    fun insertarRifa(nombre: String, inscritos: Int, fecha: String) {
        val db = writableDatabase
        val sql = "INSERT INTO rifas (nombre, inscritos, fecha) VALUES (?, ?, ?)"
        val statement = db.compileStatement(sql)
        statement.bindString(1, nombre)
        statement.bindLong(2, inscritos.toLong())
        statement.bindString(3, fecha)
        statement.executeInsert()
        db.close()
    }

    fun obtenerRifas(filtro: String = ""): List<Rifa> {
        val lista = mutableListOf<Rifa>()
        val db = readableDatabase
        val cursor = if (filtro.isEmpty()) {
            db.rawQuery("SELECT * FROM rifas", null)
        } else {
            db.rawQuery("SELECT * FROM rifas WHERE nombre LIKE ?", arrayOf("%$filtro%"))
        }

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val inscritos = cursor.getInt(cursor.getColumnIndexOrThrow("inscritos"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                lista.add(Rifa(nombre, inscritos, fecha))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}

data class Rifa(val nombre: String, val inscritos: Int, val fecha: String)