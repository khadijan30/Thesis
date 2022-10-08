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
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity() {
    // add comment
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
        setupGoogleLogin()

        loginButton.setOnClickListener {
            performLogin()
        }

        RegisterforMedico.setOnClickListener{
            val activityIntent = Intent(this, RegistrazioneMedico::class.java)
            //Toast.makeText(this,"FUNZIONA",Toast.LENGTH_LONG).show()
            startActivity(activityIntent)
            finish()
        }
        RegisterforCittadino.setOnClickListener{
            val activityIntent = Intent(this, RegistrazioneCittadino::class.java)
            //Toast.makeText(this,"FUNZIONA",Toast.LENGTH_LONG).show()
            startActivity(activityIntent)
            finish()
        }

        loginGoogleButton.setOnClickListener{
            loginWithGoogle()
        }
        loginButton.setOnClickListener{

        }

        loginRecoveryPassword.setOnClickListener {
            val email = loginEmailEditText.text.toString()
            if(email.isEmpty())
                Toast.makeText(this, "Inserire la mail nell'apposito campo", Toast.LENGTH_LONG).show()
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Log.d(TAG, "Email sent.")
                            Toast.makeText(this, "Email di recupero inviata!", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun loginWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    googleFirebaseAuth(account)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun googleFirebaseAuth(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                checkFirstAccess()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }
        }
    }

    private fun checkFirstAccess() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/")

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.child(uid).exists()){
                    //startAppGoogle(true)
                    FirebaseMessaging.getInstance().subscribeToTopic("users_topic$uid")
                        .addOnCompleteListener {
                            Log.d("LoginActivity", "Registrato al topic")
                        }


                    Toast.makeText(this@LoginActivity, "Google sign in SUCCESSFUL", Toast.LENGTH_LONG).show()
                    val editor = prefs.edit()
                    editor.putBoolean("hasProfile", true)
                    editor.commit()

                    //startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }else{
                    //startAppGoogle(false)
                    startActivity(Intent(this@LoginActivity, RegistrationGoogleActivity::class.java))
                    finish()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Errore: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }



    private fun setupGoogleLogin() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
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
                //intent = checkMail()
                //startActivity(intent)
                //finish()
                //startApp()
                Toast.makeText(this,"sei loggato con successo",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun checkMail(): Intent {
        val intent: Intent
        val user = FirebaseAuth.getInstance().currentUser

        if(user!!.isEmailVerified){
            FirebaseMessaging.getInstance().subscribeToTopic("users_topic${user.uid}")
                .addOnCompleteListener {
                    Log.d("LoginActivity", "Registrato al topic")
                }

            val editor = prefs.edit()
            editor.putBoolean("hasProfile", true)
            editor.commit()
            intent = Intent(this, VerifyEmailActivity::class.java)
        }
        else {
            intent = Intent(this, VerifyEmailActivity::class.java)
        }
        return intent
    }}

