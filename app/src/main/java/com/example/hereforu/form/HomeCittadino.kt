package com.example.hereforu.form


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.hereforu.R
import com.example.hereforu.ui.ProfiloFragment
import com.example.hereforu.ui.fragment_home_cittadino
import com.example.hereforu.ui.fragment_notifiche
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_cittadino.*



class HomeCittadino : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_cittadino)
        // val toolbar: Toolbar = findViewById(R.id.toolbar)
        // setSupportActionBar(toolbar)

        loadFragment(fragment_home_cittadino())
        val x = findViewById(R.id.bottomNav) as BottomNavigationView
        x.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(fragment_home_cittadino())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.profilo -> {
                    loadFragment(ProfiloFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.notifiche -> {
                    loadFragment(fragment_notifiche())
                    return@setOnNavigationItemReselectedListener
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_cittadino, menu)
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
                val activityIntent = Intent(this, PopUpListAnnouncementActivity::class.java)
                startActivity(activityIntent)
                finish()

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