package com.example.rifadb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BD(context: Context) : SQLiteOpenHelper(context, "Rifas.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        val crearTablaRifas = """
            CREATE TABLE rifas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                fecha TEXT
            )
        """

        val crearTablaNumeros = """
            CREATE TABLE numeros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                rifa_id INTEGER,
                numero INTEGER,
                comprado INTEGER,
                FOREIGN KEY (rifa_id) REFERENCES rifas(id) ON DELETE CASCADE
            )
        """

        db.execSQL(crearTablaRifas)
        db.execSQL(crearTablaNumeros)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS numeros")
        db.execSQL("DROP TABLE IF EXISTS rifas")
        onCreate(db)
    }

    fun insertarRifa(nombre: String, fecha: String) {
        val db = writableDatabase
        val insertRifaSQL = "INSERT INTO rifas (nombre, fecha) VALUES (?, ?)"
        val stmt = db.compileStatement(insertRifaSQL)
        stmt.bindString(1, nombre)
        stmt.bindString(2, fecha)
        val rifaId = stmt.executeInsert().toInt()

        val insertNumeroSQL = "INSERT INTO numeros (rifa_id, numero, comprado) VALUES (?, ?, 0)"
        val numeroStmt = db.compileStatement(insertNumeroSQL)
        for (i in 0 until 100) {
            numeroStmt.bindLong(1, rifaId.toLong())
            numeroStmt.bindLong(2, i.toLong())
            numeroStmt.executeInsert()
        }
        db.close()
    }

    fun obtenerRifas(filtro: String = ""): List<Rifa> {
        val lista = mutableListOf<Rifa>()
        val db = readableDatabase
        val query = if (filtro.isEmpty()) {
            "SELECT r.*, COUNT(n.id) as inscritos FROM rifas r LEFT JOIN numeros n ON r.id = n.rifa_id AND n.comprado = 1 GROUP BY r.id"
        } else {
            "SELECT r.*, COUNT(n.id) as inscritos FROM rifas r LEFT JOIN numeros n ON r.id = n.rifa_id AND n.comprado = 1 WHERE r.nombre LIKE ? GROUP BY r.id"
        }
        val cursor = if (filtro.isEmpty()) db.rawQuery(query, null)
        else db.rawQuery(query, arrayOf("%$filtro%"))

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val inscritos = cursor.getInt(cursor.getColumnIndexOrThrow("inscritos"))
                lista.add(Rifa(nombre, inscritos, fecha))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun obtenerNumeros(rifaNombre: String): List<Pair<Int, Boolean>> {
        val lista = mutableListOf<Pair<Int, Boolean>>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT n.numero, n.comprado FROM numeros n JOIN rifas r ON n.rifa_id = r.id WHERE r.nombre = ?",
            arrayOf(rifaNombre)
        )
        if (cursor.moveToFirst()) {
            do {
                val numero = cursor.getInt(0)
                val comprado = cursor.getInt(1) == 1
                lista.add(Pair(numero, comprado))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarNumeros(rifaNombre: String, comprados: Set<Int>) {
        val db = writableDatabase
        val rifaIdCursor = db.rawQuery("SELECT id FROM rifas WHERE nombre = ?", arrayOf(rifaNombre))
        if (rifaIdCursor.moveToFirst()) {
            val rifaId = rifaIdCursor.getInt(0)
            val resetSQL = "UPDATE numeros SET comprado = 0 WHERE rifa_id = ?"
            val updateSQL = "UPDATE numeros SET comprado = 1 WHERE rifa_id = ? AND numero = ?"
            db.compileStatement(resetSQL).apply {
                bindLong(1, rifaId.toLong())
                executeUpdateDelete()
            }
            val stmt = db.compileStatement(updateSQL)
            for (num in comprados) {
                stmt.bindLong(1, rifaId.toLong())
                stmt.bindLong(2, num.toLong())
                stmt.executeUpdateDelete()
            }
        }
        rifaIdCursor.close()
        db.close()
    }

    fun eliminarRifa(nombre: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM rifas WHERE nombre = ?", arrayOf(nombre))
        db.close()
    }
}

data class Rifa(val nombre: String, val inscritos: Int, val fecha: String)