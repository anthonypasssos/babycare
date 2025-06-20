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

class MainActivity : BaseActivity() {
    private lateinit var addSleep: ImageButton
    private lateinit var addFedding: ImageButton

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = this.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val temUserId = prefs.contains("user_id")

        if (!temUserId) {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        setContentView(R.layout.activity_main)

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
    }
}
