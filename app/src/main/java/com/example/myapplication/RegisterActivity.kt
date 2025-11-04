package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val textViewError = findViewById<TextView>(R.id.textViewError)

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                textViewError.text = "Vui lòng điền đầy đủ thông tin"
                textViewError.visibility = View.VISIBLE
            } else if (password != confirmPassword) {
                textViewError.text = "Mật khẩu không khớp"
                textViewError.visibility = View.VISIBLE
            } else {
                textViewError.visibility = View.GONE
                saveUserCredentials(email, password)
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                finish() // Quay lại màn hình đăng nhập
            }
        }
    }

    private fun saveUserCredentials(email: String, password: String) {
        val file = File(filesDir, "user_credentials.txt")
        try {
            FileOutputStream(file).use {
                it.write("$email,$password".toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show()
        }
    }
}