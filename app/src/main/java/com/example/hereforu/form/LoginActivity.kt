package com.example.hereforu.form

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.example.hereforu.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity() {
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences("SavedValues", Context.MODE_PRIVATE)

        val editor = prefs.edit()
        editor.remove("selectedPhotoUri")
        editor.commit()

        auth = FirebaseAuth.getInstance()

        RegisterforMedico.setOnClickListener {
            val activityIntent = Intent(this, RegistrazioneMedico::class.java)
            startActivity(activityIntent)
            finish()
        }
        RegisterforCittadino.setOnClickListener {
            val activityIntent = Intent(this, RegistrazioneCittadino::class.java)
            startActivity(activityIntent)
            finish()
        }

        loginButton.setOnClickListener {
            performLogin()
        }


        loginRecoveryPassword.setOnClickListener {
            val email = loginEmailEditText.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Inserire la mail nell'apposito campo", Toast.LENGTH_LONG)
                    .show()
            } else {
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Email di recupero inviata, se non lo trovi verifica il suo spam !", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener{
                            e -> Toast.makeText(this,"Failed To send due to " +e.message,Toast.LENGTH_LONG).show()
                    }
            }

        }
    }
    private fun performLogin() {
        val email = loginEmailEditText.text.toString()
        val password = loginPasswordEditText.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Per favore, devi riempire tutti i campi", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                val currentUser=FirebaseAuth.getInstance().currentUser
                if(currentUser!!.isEmailVerified()){
                  // send the user to the home page
                Toast.makeText(this,"sei loggato con successo",Toast.LENGTH_LONG).show()
                }
                else{
                    currentUser!!.sendEmailVerification()
                        .addOnSuccessListener{
                            Toast.makeText(this ,"check your Email , an Email of Verification has been sent to you  ",Toast.LENGTH_LONG).show()
                            }
                        .addOnFailureListener{
                            e -> Toast.makeText(this,"Failed To send due to " +e.message,Toast.LENGTH_LONG).show()
                        }

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

}

