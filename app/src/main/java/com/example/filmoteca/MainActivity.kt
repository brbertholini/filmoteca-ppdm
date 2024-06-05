package com.example.filmoteca

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_welcome)

        var btnNavigateToSignIn = findViewById<Button>(R.id.btnNavigateSignIn)
        var btnNavigateToSignUp = findViewById<Button>(R.id.btnNavigateSignUp)


    }
}