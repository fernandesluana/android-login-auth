package com.luanafernandes.loginauthapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luanafernandes.loginauthapp.databinding.ActivityProfileBinding
import com.luanafernandes.loginauthapp.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        binding.btnRegister.setOnClickListener{
            register()
        }
    }

    private fun register() {

            if (!validateInputs()) {
                return
            }

            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val firstName = binding.editFirstName.text.toString()
            val lastName = binding.editLastName.text.toString()

            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserToDatabase(firstName, lastName)
                    } else {
                        Toast.makeText(
                            this@RegistrationActivity,
                            "Registration failed, please try again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

    }

    private fun validateInputs(): Boolean {
        val inputs = listOf(
            binding.editFirstName to "First name",
            binding.editLastName to "Last name",
            binding.editUsername to "Username",
            binding.editPassword to "Password"
        )

        for ((inputField, fieldName) in inputs) {
            if (inputField.text.toString().isEmpty()) {
                inputField.error = "Please enter $fieldName"
                return false
            }
        }
        return true
    }

    private fun saveUserToDatabase(firstName: String, lastName: String) {
        val currentUser = auth.currentUser
        val currentUserDb = databaseReference?.child(currentUser?.uid!!)
        currentUserDb?.child("firstName")?.setValue(firstName)
        currentUserDb?.child("lastName")?.setValue(lastName)

        Toast.makeText(this@RegistrationActivity, "Registration Success.", Toast.LENGTH_LONG).show()
        finish()
    }


}