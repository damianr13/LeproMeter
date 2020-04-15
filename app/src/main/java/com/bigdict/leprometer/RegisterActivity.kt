package com.bigdict.leprometer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener(){
            registerUser()
        }

        gotoLoginButton.setOnClickListener(){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(){
        if(nameEditText.text.toString().isEmpty()){
            nameEditText.error = "Please enter a name"
            nameEditText.requestFocus()
            return
        }

        if(mailEditText.text.toString().isEmpty()){
            mailEditText.error = "Please enter an email address"
            mailEditText.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mailEditText.text.toString()).matches()){
            mailEditText.error = "Please enter a valid email address"
            mailEditText.requestFocus()
            return
        }

        if(passwordEditText.text.toString().isEmpty()){
            passwordEditText.error = "Please enter a password"
            passwordEditText.requestFocus()
            return
        }

        if(passwordEditText.text.toString()!= password2EditText.text.toString()){
            password2EditText.error = "Passwords dont match. Cacat prost ce esti! "
            password2EditText.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(mailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->

                if(task.isSuccessful){
                    Toast.makeText(baseContext, "Sign up Suceeded",
                        Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                } else{
                    Toast.makeText(baseContext, "Sign up Failed",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }

}
