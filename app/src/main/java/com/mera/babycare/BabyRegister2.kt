package com.mera.babycare

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.UUID

class BabyRegister2 : BaseActivity() {
    private lateinit var girlBtn: MaterialButton
    private lateinit var boyBtn: MaterialButton

    private var isGirlSelected = false
    private var isBoySelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_register2)
        setupListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        girlBtn = findViewById(R.id.girl_btn)
        boyBtn = findViewById(R.id.boy_btn)
        val nextBtn = findViewById<ImageButton>(R.id.next_btn)

        girlBtn.setOnTouchListener { v, event ->
            handleButtonTouch(v as MaterialButton, event, "girl")
            true
        }

        boyBtn.setOnTouchListener { v, event ->
            handleButtonTouch(v as MaterialButton, event, "boy")
            true
        }

        nextBtn.setOnClickListener {
            registerBaby()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun registerBaby() {
        val babyId = sendBabyToDB()

        val prefs = this.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        prefs.edit { putString("baby_id", babyId) }
    }

    private fun sendBabyToDB(): String {
        val dbHelper = Database(this)
        val db = dbHelper.writableDatabase
        val dbManager = DataBaseManager(this)

        val babyId = UUID.randomUUID().toString()

        val prefs = this.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val loggedUserId = prefs.getString("user_id", null)

        dbManager.addBaby(
            db = db,
            id = babyId,
            userId = loggedUserId.toString(),
            name = intent.getStringExtra("baby_name").toString(),
            birthDate = intent.getLongExtra("baby_birth", 0L),
            sex = babySex!!,
            createdAt = System.currentTimeMillis()
        )

        return babyId
    }

    private var babySex: String? = null

    private fun handleButtonTouch(button: MaterialButton, event: MotionEvent, type: String) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (type == "girl" && !isGirlSelected || type == "boy" && !isBoySelected) {
                    button.translationZ = 8f
                }
            }
            MotionEvent.ACTION_UP -> {
                if (type == "girl") {
                    if (!isGirlSelected) {
                        isGirlSelected = true
                        isBoySelected = false
                        babySex = "girl"
                    } else {
                        isGirlSelected = false
                        babySex = null
                    }
                } else if (type == "boy") {
                    if (!isBoySelected) {
                        isBoySelected = true
                        isGirlSelected = false
                        babySex = "boy"
                    } else {
                        isBoySelected = false
                        babySex = null
                    }
                }

                updateButtonStates()
            }
        }
    }

    private fun updateButtonStates() {
        val nextBtn = findViewById<ImageButton>(R.id.next_btn)
        girlBtn.translationZ = if (isGirlSelected) 4f else 0f
        boyBtn.translationZ = if (isBoySelected) 4f else 0f
        nextBtn.visibility = if (babySex != null) View.VISIBLE else View.GONE
    }
}