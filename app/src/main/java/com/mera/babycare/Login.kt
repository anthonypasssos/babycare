package com.mera.babycare

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Login : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val googleSignInButton = findViewById<ImageButton>(R.id.google_btn)
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        findViewById<TextView>(R.id.register).setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInWithGoogle() {
        CoroutineScope(Dispatchers.Main).launch {
            val signInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(serverClientId = getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val credentialManager = CredentialManager.create(this@Login)

            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@Login
                )

                // **** AQUI ESTÁ A MUDANÇA CRUCIAL ****
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Tente criar o GoogleIdTokenCredential a partir dos dados do CustomCredential
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken)
                    } else {
                        Log.e("GoogleSignIn", "ID Token é nulo, mesmo após criar GoogleIdTokenCredential do CustomCredential.")
                    }
                } else {
                    Log.e("GoogleSignIn", "Credencial não é do tipo Google ID Token. Tipo: ${credential.javaClass.simpleName}, Sub-tipo: ${if (credential is CustomCredential) credential.type else "N/A"}")
                }

            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Erro ao fazer login com Google: ${e.message}", e)
                e.printStackTrace() // Adicione isso para depuração completa
            }
        }
    }

    private val auth = FirebaseAuth.getInstance()

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}