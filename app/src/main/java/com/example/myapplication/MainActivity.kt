package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sharedPref = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val editTextEmail = findViewById<TextInputEditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<TextInputEditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonGoToRegister = findViewById<Button>(R.id.buttonGoToRegister)
        val textViewError = findViewById<TextView>(R.id.textViewError)
        
        buttonGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                textViewError.text = "Vui lòng nhập email"
                textViewError.visibility = View.VISIBLE
            } else if (isValidCredentials(email, password)) {
                textViewError.visibility = View.GONE
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", true)
                    apply()
                }

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                textViewError.text = "Email hoặc mật khẩu không chính xác"
                textViewError.visibility = View.VISIBLE
            }
        }
    }
    
    private fun isValidCredentials(email: String, password: String): Boolean {
        val file = File(filesDir, "user_credentials.txt")
        if (!file.exists()) {
            // No user has registered yet, so login is not possible
            return false
        }
        
        try {
            val credentials = file.readText().split(",")
            // Check if the file has the correct format (email,password)
            if (credentials.size == 2) {
                return email.trim() == credentials[0].trim() && password.trim() == credentials[1].trim()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return false
    }
}
