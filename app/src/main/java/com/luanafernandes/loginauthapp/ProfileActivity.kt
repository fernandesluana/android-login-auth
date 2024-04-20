package com.luanafernandes.loginauthapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.luanafernandes.loginauthapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var userReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")
        userReference = databaseReference?.child(auth.currentUser?.uid ?: "")

        loadProfile()

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile() {
        val currentUser = auth.currentUser
        binding.textEmail.text = "Email: ${currentUser?.email ?: ""}"

        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val firstname = snapshot.child("firstname").value?.toString() ?: ""
                val lastname = snapshot.child("lastname").value?.toString() ?: ""
                binding.textFirstName.text = "Firstname: $firstname"
                binding.textLastName.text = "Lastname: $lastname"
            }

            override fun onCancelled(error: DatabaseError) {
                val errorMessage = "Database Error"
                Log.e("ProfileActivity", errorMessage)
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        userReference?.removeEventListener(valueEventListener)
    }


}