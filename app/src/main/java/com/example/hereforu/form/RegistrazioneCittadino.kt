package com.example.hereforu.form

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.hereforu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registrazione_cittadino.*
import kotlinx.android.synthetic.main.activity_registrazione_medico.*
import java.util.*


class RegistrazioneCittadino : AppCompatActivity() {
    private lateinit var dbReference: DatabaseReference
    private var selectedPhotolocation: Uri? = null
    private val DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/here4you-f2172.appspot.com/o/photos%2Fpsicologo%20(1).jpg?alt=media&token=35fefadd-b69f-4567-a4d8-a723f2da9a80"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione_cittadino)

        dbReference = FirebaseDatabase.getInstance().getReference("users")
        //backtologin.setOnClickListener {
        //     var intent = Intent(this, LoginActivity::class.java)
        //     startActivity(intent)
        //     finish()


        RegButton2.setOnClickListener {
            handelRegistration()
        }

        selectphotoC.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/ *"
            startActivityForResult(intent, 0)
        }
    }



override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
super.onActivityResult(requestCode, resultCode, data)
if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
Log.d("Register Activity", "photo e stat selezionata ")
// location where the photo is stored
selectedPhotolocation = data.data
val nitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotolocation)
val bitmapDrwable = BitmapDrawable(nitmap)
selectphotoC.setBackgroundDrawable(bitmapDrwable)
}
}

private fun uploadPhotoToFirebase(){
    if(selectedPhotolocation==null) {
        SaverUserTodb(DEFAULT_IMAGE_URL.toString(),true)
        return
    }

val filename= UUID.randomUUID().toString()
val ref = FirebaseStorage.getInstance().getReference("photos/$filename")
ref.putFile(selectedPhotolocation!!)
.addOnSuccessListener {
Log.d("Register Activity","image uploaded ")
ref.downloadUrl.addOnSuccessListener {
// un log
Log.d("Register Activity ","File Location $it")
// save the user to db
SaverUserTodb(it.toString(),false)
}
}
.addOnFailureListener{
Toast.makeText(this,"something wrong", Toast.LENGTH_LONG).show()
}
}

private fun SaverUserTodb(profileImageUrl:String,defaultImage:Boolean){
// i will verify if the user iud is not null
val uid =FirebaseAuth.getInstance().uid?:""
val ref=FirebaseDatabase.getInstance().getReference("/users/$uid") // i create directory user and for each user has own directory $uid
// here i will create the users ... but i have to distinguere btw cittaddino and doctor
// for the moment i will create a users
    if(defaultImage){
        val userDB = FirebaseAuth.getInstance().currentUser
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(profileImageUrl))
            .build()
        userDB!!.updateProfile(profileUpdate)
    }
val citizenusers=Citizen2(uid,RegisterNameC.text.toString(),RegisterCognomeC.text.toString(),RegisterEmailEditTextC.text.toString(),profileImageUrl)
ref.setValue(citizenusers)
.addOnSuccessListener {
Log.d("Register Activity","userd Adde")
}
.addOnFailureListener{
Toast.makeText(this,"Impossible add the user ${it.message}",Toast.LENGTH_LONG).show()
}
}

private fun handelRegistration() {
var email=RegisterEmailEditTextC.text.toString()
var name=RegisterNameC.text.toString();
var cognome=RegisterCognomeC.text.toString();
var password=RegisterPasswordEditTextC.text.toString();
var confirmPassword=configPasswordEditTextC.text.toString();
if(email.isEmpty()|| name.isEmpty() || cognome.isEmpty() || password.isEmpty() ||confirmPassword.isEmpty()){
Toast.makeText(this,"all fields are mandatory ",Toast.LENGTH_LONG).show()
return
}
if(confirmPassword!=password){
Toast.makeText(this, "confirm password doesn't match to password ",Toast.LENGTH_LONG).show()
return
}
FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
.addOnCompleteListener{
if(!it.isSuccessful){
return@addOnCompleteListener
}
uploadPhotoToFirebase()
//SaverUserTodb()
Toast.makeText(this ,"sei registrato ",Toast.LENGTH_LONG).show()
}
.addOnFailureListener{
Toast.makeText(this ,"Failed to create user : ${it.message}",Toast.LENGTH_LONG).show()
}


}


}

class Citizen2(val uid:String,val name:String,val cognome:String,val email:String, val profileImagePath:String)
