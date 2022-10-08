package com.example.hereforu

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Handler
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.hereforu.form.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {
    private var handler:Handler=Handler()
    private var runnable:Runnable?=null
    private var delay: Long=5000

    private lateinit var prefs: SharedPreferences

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_intro)

        prefs = getSharedPreferences("SavedValues", Context.MODE_PRIVATE)

        val hasProfile = prefs.getBoolean("hasProfile", false)
        val user = FirebaseAuth.getInstance().currentUser

        if(!isNetworkAvailable(this)){
            Toast.makeText(this, "please check your network", Toast.LENGTH_LONG).show()
        }
        //this part when the user has a profile and is login in ...
        /*else if(user != null && hasProfile) {
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, delay)
        }*/
        else{
            handler.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, delay)
        }

        handler.postDelayed({
            finish()
        }, delay)
    }

    private fun isNetworkAvailable(con: Context): Boolean {
        try {
            val cm = con
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}