package com.mera.babycare

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.security.MessageDigest

class DataBaseManager(context: Context) {
    fun addUser(
        db: SQLiteDatabase,
        id: String, //Use UUID.randomUUID().toString() para gerar o id
        name: String,
        photoUrl: String?,
        email: String,
        passwordHash: String?,
        googleIdToken: String?,
        createdAt: Long = System.currentTimeMillis()
    ) {
        val values = ContentValues().apply {
            put("id", id)
            put("name", name)
            put("photo_url", photoUrl)
            put("email", email)
            put("password_hash", passwordHash)
            put("google_id_token", googleIdToken)
            put("created_at", createdAt)
        }
        db.insert("users", null, values)
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun isEmailRegistered(db: SQLiteDatabase, email: String): Boolean {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM users WHERE email = ?", arrayOf(email))
        var exists = false
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0
        }
        cursor.close()
        return exists
    }

    fun addBaby(
        db: SQLiteDatabase,
        id: String, //Use UUID.randomUUID().toString() para gerar o id
        userId: String,
        name: String,
        birthDate: Long,
        sex: String,
        createdAt: Long = System.currentTimeMillis()
    ) {
        val values = ContentValues().apply {
            put("id", id)
            put("user_id", userId)
            put("name", name)
            put("birth_date", birthDate)
            put("sex", sex)
            put("created_at", createdAt)
        }
        db.insert("babies", null, values)
    }

    data class Baby(
        val id: String,
        val userId: String,
        val name: String,
        val birthDate: Long,
        val sex: String,
        val createdAt: Long
    )

    fun getBabyById(db: SQLiteDatabase, babyId: String): Baby? {
        val cursor = db.rawQuery(
            "SELECT id, user_id, name, birth_date, sex, created_at FROM babies WHERE id = ?",
            arrayOf(babyId)
        )

        var baby: Baby? = null

        if (cursor.moveToFirst()) {
            baby = Baby(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                birthDate = cursor.getLong(cursor.getColumnIndexOrThrow("birth_date")),
                sex = cursor.getString(cursor.getColumnIndexOrThrow("sex")),
                createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"))
            )
        }

        cursor.close()
        return baby
    }


    fun addFeeding(
        db: SQLiteDatabase,
        id: String, //Use UUID.randomUUID().toString() para gerar o id
        babyId: String,
        type: String, // "amamentação" ou "fórmula"
        startTime: Long,
        endTime: Long,
        volumeMl: Int?, // apenas se fórmula
        notes: String?,
        createdAt: Long = System.currentTimeMillis()
    ) {
        val values = ContentValues().apply {
            put("id", id)
            put("baby_id", babyId)
            put("type", type)
            put("start_time", startTime)
            put("end_time", endTime)
            put("volume_ml", volumeMl)
            put("notes", notes)
            put("created_at", createdAt)
        }
        db.insert("feedings", null, values)
    }

    fun addSleepSession(
        db: SQLiteDatabase,
        id: String, //Use UUID.randomUUID().toString() para gerar o id
        babyId: String,
        startTime: Long,
        endTime: Long,
        isNap: Boolean,
        notes: String?,
        createdAt: Long = System.currentTimeMillis()
    ) {
        val values = ContentValues().apply {
            put("id", id)
            put("baby_id", babyId)
            put("start_time", startTime)
            put("end_time", endTime)
            put("is_nap", if (isNap) 1 else 0)
            put("notes", notes)
            put("created_at", createdAt)
        }
        db.insert("sleep_sessions", null, values)
    }

}