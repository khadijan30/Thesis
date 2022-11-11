package com.example.hereforu.form

import Announcement
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.hereforu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_annuncio_medico2.*
import java.lang.Exception


class AnnuncioMedico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annuncio_medico2)

        //val mainAc : MainActivity = activity as MainActivity

      /*  val departmentSpinner: Spinner = root.findViewById(R.id.CampoMedico)
        val departmentAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.campo_array, android.R.layout.simple_spinner_item)
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        departmentSpinner.adapter = departmentAdapter


        val insertButton: Button = root.findViewById(R.id.createInsertButton) */

       /* try {
            name = mainAc.profile!!.getName()
        }catch (e: Exception){
            val nameText: TextView = mainAc.headerNameTextView
            name = nameText.text.toString().trim()
        } */

        createInsertButton.setOnClickListener {
           publish()
        }

        return
    }

    private fun publish() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/announcements/$uid/")
        val nome = Nomestudio.text.trim().toString()
        val campo = CampoMedico.selectedItem.toString()
        val data = orario.text.toString()
        val news = Indicazioni.text.trim().toString()
        val description = createDescriptionEditText.text.trim().toString()

        if(nome.isEmpty() || campo.isEmpty() || data.isEmpty() || news.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Per favore, riempire tutti i campi", Toast.LENGTH_LONG).show()
            return
        }

        val announcementID = ref.push().key
        val announcement = Announcement(uid, announcementID!!, nome, campo, data, news,description, ServerValue.TIMESTAMP, 0, 0f)

        //  val c : MutableMap<String, String> = ServerValue.TIMESTAMP
        ref.child(announcementID.toString()).setValue(announcement)
            .addOnSuccessListener {
                Toast.makeText(this, "Annuncio pubblicato", Toast.LENGTH_LONG).show()
               // mainAc.onNavigationItemSelected(mainAc.navView.menu.findItem(R.id.nav_showcase))
                val activityIntent = Intent(this, HomeMedico::class.java)
                startActivity(activityIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed announcement on db", Toast.LENGTH_LONG).show()
            }
    }
}