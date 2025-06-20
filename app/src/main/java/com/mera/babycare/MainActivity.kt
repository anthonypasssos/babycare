package com.mera.babycare

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    private lateinit var addSleep: ImageButton
    private lateinit var addFedding: ImageButton

    private lateinit var baby: DataBaseManager.Baby

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = this.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val temUserId = prefs.contains("user_id")

        if (!temUserId) {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        setContentView(R.layout.activity_main)

        getData()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        val navbar = findViewById<MaterialCardView>(R.id.navbar)

        ViewCompat.setOnApplyWindowInsetsListener(navbar) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = systemBarsInsets.bottom
            view.layoutParams = params

            insets
        }

        val babyCard = findViewById<LinearLayout>(R.id.baby_card)

        ViewCompat.setOnApplyWindowInsetsListener(babyCard) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemBarsInsets.top
            view.layoutParams = params

            insets
        }

        findViewById<TextView>(R.id.baby_name).text = baby.name
        findViewById<TextView>(R.id.title_sleep).text = "Diário de sono de ${baby.name}"
        findViewById<TextView>(R.id.title_fedding).text = "Como ${baby.name} se alimentou"
        findViewById<TextView>(R.id.life_time).text = getMonthsAndDaysSince(baby.birthDate)
    }

    private fun setupListeners() {
        addSleep = findViewById(R.id.add_sleep)

        addSleep.setOnClickListener {
            val intent = Intent(this, SleepRegisterActivity::class.java)
            startActivity(intent)
        }

        addFedding = findViewById(R.id.add_fedding)

        addFedding.setOnClickListener {
            val intent = Intent(this, FoodRegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.report_btn).setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        val dbHelper = Database(this)
        val db = dbHelper.writableDatabase
        val dbManager = DataBaseManager(this)

        val prefs = this.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val babyId = prefs.getString("baby_id", null)

        baby = dbManager.getBabyById(db, babyId!!)!!
    }

    private fun getMonthsAndDaysSince(timestamp: Long): String {
        val startCalendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        val endCalendar = Calendar.getInstance() // Hoje

        var months = 0
        var days = 0

        // Calcular meses
        while (startCalendar.before(endCalendar)) {
            startCalendar.add(Calendar.MONTH, 1)
            if (startCalendar.after(endCalendar)) {
                startCalendar.add(Calendar.MONTH, -1) // volta 1 mês
                break
            }
            months++
        }

        // Calcular dias restantes
        val diffInMillis = endCalendar.timeInMillis - startCalendar.timeInMillis
        days = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

        // Montar o texto
        val mesesTexto = if (months == 1) "1 mês" else "$months meses"
        val diasTexto = if (days == 1) "1 dia" else "$days dias"

        return if (months > 0 && days > 0) {
            "$mesesTexto e $diasTexto"
        } else if (months > 0) {
            mesesTexto
        } else {
            diasTexto
        }
    }
}
