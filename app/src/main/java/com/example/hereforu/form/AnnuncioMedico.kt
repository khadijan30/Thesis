package com.example.hereforu.form

import Announcement
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.hereforu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_annuncio_medico2.*
import java.text.SimpleDateFormat
import java.util.*


class AnnuncioMedico : AppCompatActivity() {
    var time :Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annuncio_medico2)

        orario.setOnClickListener{
            val dialogView = View.inflate(this, R.layout.date_time_picker, null)
            val alertDialog = AlertDialog.Builder(this).create()

            dialogView.findViewById<View>(R.id.date_time_set).setOnClickListener {
                val datePicker = dialogView.findViewById<View>(R.id.date_picker) as DatePicker
                val timePicker = dialogView.findViewById<View>(R.id.time_picker) as TimePicker
                val calendar: Calendar = GregorianCalendar(
                    datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth,
                    timePicker.currentHour,
                    timePicker.currentMinute
                )
                time = calendar.timeInMillis
                val textView: TextView = findViewById(R.id.orario)
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val dateString = simpleDateFormat.format(time)
                textView.text = String.format("Date: %s", dateString)
                alertDialog.dismiss()
            }
            alertDialog.setView(dialogView)
            alertDialog.show()
        }

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
        val data = time
        val news = Indicazioni.text.trim().toString()
        val description = createDescriptionEditText.text.trim().toString()

        if(nome.isEmpty() || campo.isEmpty()  || news.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Per favore, riempire tutti i campi", Toast.LENGTH_LONG).show()
            return
        }

        val announcementID = ref.push().key
        val announcement = Announcement(uid, "announcementID!!", nome, campo, data, news,description, ServerValue.TIMESTAMP, "0", "0")

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