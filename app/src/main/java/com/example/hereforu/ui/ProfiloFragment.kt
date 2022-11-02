package com.example.hereforu.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hereforu.R
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
    var fullname:String="";
    var description:String="";
    var srcImageProfile:String="";
    var cognome:String=""
    var email:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                srcImageProfile=snapshot.child("profileImagePath").getValue<String>().toString()
                Picasso.with(this@ProfiloFragment.context).load(srcImageProfile).into(imageProfile)
                description=snapshot.child("identificativo").getValue<String>().toString();
                if(description=="2"){
                    WHO.setText("User")
                }
                else{
                    WHO.setText("DOCTOR")
                }
                email= snapshot.child("email").getValue<String>().toString()
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
        // Not Working
        //Update.setOnClickListener{
            //if(email!=myEmail.text.toString()){
                //uidRef.child("email").setValue(myEmail.text.toString())
            //}
        //}
    }
    private fun update(){
        if(email!=myEmail.text.toString()){
            uidRef.child("email").setValue(myEmail.text.toString())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profilo, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfiloFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfiloFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}