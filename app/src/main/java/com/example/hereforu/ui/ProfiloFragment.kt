package com.example.hereforu.ui
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.UpdateAppearance
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hereforu.R
import com.example.hereforu.form.HomeMedico
import com.example.hereforu.form.RegistrazioneCittadino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.NameProfile
import kotlinx.android.synthetic.main.fragment_profilo.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profilo.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfiloFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfiloFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseDatabase.getInstance().reference
    val uidRef = db.child("users").child(uid.toString())
    var fullname: String = "";
    var description: String = "";
    var srcImageProfile: String = "";
    var cognome: String = ""
    var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        /* if(savedInstanceState==null){
            SupportFr.commit{

            } */


    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            srcImageProfile = snapshot.child("profileImagePath").getValue<String>().toString()
            Picasso.with(this@ProfiloFragment.context).load(srcImageProfile).into(imageProfile)
            description = snapshot.child("identificativo").getValue<String>().toString();
            if (description == "2") {
                WHO.setText("User")
            } else {
                WHO.setText("DOCTOR")
            }
            email = snapshot.child("email").getValue<String>().toString()
            myEmail.setText(email)
            fullname =
                snapshot.child("name").getValue<String>().toString()
            NameProfile.setText(fullname)
            myName.setText(fullname)
            cognome =
                snapshot.child("cognome").getValue<String>().toString()
            myCognome.setText(cognome)

        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("TAG", databaseError.getMessage())
        }
    }

    uidRef.addListenerForSingleValueEvent(valueEventListener)

}
    // handling the update button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         // here i'll manage the update profile on db
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profilo, container, false)
    }

}