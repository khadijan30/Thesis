package com.example.hereforu.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hereforu.R
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfile : AppCompatActivity() {

    //private var currentUser=FirebaseAuth.getInstance().currentUser
    private lateinit var reference:DatabaseReference;
    private lateinit var auth:FirebaseAuth;
    private lateinit var UserId:String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        var fullname:String=""

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference
        val uidRef = db.child("users").child(uid.toString())
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullname =
                    snapshot.child("identificativo").getValue<String>().toString()
                    NameProfile.setText(fullname)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.getMessage())
            }
        }
        uidRef.addListenerForSingleValueEvent(valueEventListener)
    }



    }
