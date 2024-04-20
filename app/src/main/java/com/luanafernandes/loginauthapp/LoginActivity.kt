package com.luanafernandes.loginauthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.luanafernandes.loginauthapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener{
            login()
        }

        binding.textRegister.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))

        }
    }

    private fun login(){
        if (!validateInputs()) {
            return
        }

        val username = binding.editUsername.text.toString()
        val password = binding.editPassword.text.toString()

        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateToProfile()
                } else {
                    showLoginFailedMessage()
                }
            }
    }

    private fun validateInputs(): Boolean {
        val inputs = listOf(
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

    private fun navigateToProfile() {
        startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
        finish()
    }

    private fun showLoginFailedMessage() {
        Toast.makeText(this@LoginActivity, "Login failed, please try again!", Toast.LENGTH_LONG).show()
    }
}