package com.example.hereforu.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.hereforu.R
import com.google.firebase.auth.FirebaseAuth

class HomeMedico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_medico)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_medico, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when (id) {
            R.id.MENU_1 -> {
                Toast.makeText(this, "cliccato menu 1, logout", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut();
                val activityIntent = Intent(this, LoginActivity::class.java)
                startActivity(activityIntent)
                finish()

            }
            R.id.MENU_2 -> {
                Toast.makeText(this, "cliccato menu 2", Toast.LENGTH_LONG).show()

            }
            R.id.MENU_3 -> {
                Toast.makeText(this, "cliccato menu 3", Toast.LENGTH_LONG).show()

            }

            R.id.MENU_4 -> {
                Toast.makeText(this, "cliccato menu 4", Toast.LENGTH_LONG).show()

            }
            R.id.MENU_5 -> {
                Toast.makeText(this, "cliccato menu 5", Toast.LENGTH_LONG).show()

            }
        }
        return false
    }
}