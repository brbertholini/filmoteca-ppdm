package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmoteca.databinding.LayoutActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val email = binding.editEmail2.text.toString()
            val password = binding.editPassword2.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "As senhas devem ser iguais", Toast.LENGTH_SHORT)
                        .show()
                }
            } else{
                Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.textViewNewAccess.setOnClickListener {
            val signinIntent = Intent(this, SignInActivity::class.java)
            startActivity(signinIntent)
        }
    }
}