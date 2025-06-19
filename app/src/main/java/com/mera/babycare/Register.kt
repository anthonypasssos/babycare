package com.mera.babycare

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.UUID
import androidx.core.content.edit

class Register : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupListeners()
    }

    private fun setupListeners() {
        val btnSend = findViewById<MaterialButton>(R.id.register_btn)

        btnSend.setOnClickListener {
            val inputsData = getInputData()

            if (validadeInputs(inputsData)) {
                registerUser(inputsData)
                val intent = Intent(this, BabyRegister1::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        val btnLogin = findViewById<TextView>(R.id.login_btn)
        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun registerUser(inputsData: RegisterInput) {
        val userId = sendUserToDB(inputsData)

        val prefs = this.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        prefs.edit { putString("user_id", userId) }
    }

    private fun sendUserToDB(inputsData: RegisterInput): String {
        val dbHelper = Database(this)
        val db = dbHelper.writableDatabase
        val dbManager = DataBaseManager(this)

        val userId = UUID.randomUUID().toString()
        dbManager.addUser(
            db = db,
            id = userId,
            name = inputsData.name,
            photoUrl = null,
            email = inputsData.email,
            passwordHash = dbManager.hashPassword(inputsData.password),
            googleIdToken = null
        )

        db.close()

        return userId
    }

    data class RegisterInput(val name: String, val email: String, val password: String)

    private fun getInputData(): RegisterInput {
        val name = findViewById<TextInputEditText>(R.id.name_input).text.toString()
        val email = findViewById<TextInputEditText>(R.id.email_input).text.toString()
        val password = findViewById<TextInputEditText>(R.id.password_input).text.toString()

        return RegisterInput(name, email, password)
    }

    private fun validadeInputs(inputsData: RegisterInput): Boolean {
        return validateEmail(inputsData.email) && validatePassword(inputsData.password) && validateName(inputsData.name)
    }

    private fun validateEmail(emailText: String): Boolean {
        val emailLayout = findViewById<TextInputLayout>(R.id.layout_email)
        val dbHelper = Database(this)
        val db = dbHelper.readableDatabase
        val dbManager = DataBaseManager(this)

        val isEmailFormatValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()

        if (!isEmailFormatValid) {
            emailLayout.error = "E-mail inválido"
            db.close()
            return false
        }

        val isEmailAlreadyRegistered = dbManager.isEmailRegistered(db, emailText)
        db.close()

        return if (isEmailAlreadyRegistered) {
            emailLayout.error = "E-mail já cadastrado"
            false
        } else {
            emailLayout.error = null
            true
        }
    }

    private fun validatePassword(passwordText: String): Boolean {
        val passwordLayout = findViewById<TextInputLayout>(R.id.layout_password)

        return if (passwordText.length >= 6) {
            passwordLayout.error = null  // Senha válida
            true
        } else {
            passwordLayout.error = "A senha deve ter pelo menos 6 caracteres"
            false
        }
    }

    private fun validateName(nameText: String): Boolean {
        val nameLayout = findViewById<TextInputLayout>(R.id.layout_name)

        return if (nameText.isNotEmpty()) {
            nameLayout.error = null
            true
        } else {
            nameLayout.error = "Digite o seu nome"
            false
        }
    }

}