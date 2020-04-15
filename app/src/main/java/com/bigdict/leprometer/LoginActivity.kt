package com.bigdict.leprometer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = FirebaseAuth.getInstance()

        gotoRegisterButton.setOnClickListener() {
            startActivity((Intent(this, RegisterActivity::class.java)))
            finish()
        }

        signButton.setOnClickListener() {
            doLogin()
        }
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(baseContext, "Login Failed",
                Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    private fun doLogin(){

        if(nameLoginEditText.text.toString().isEmpty()){
            nameLoginEditText.error = "Please enter a valid email address"
            nameLoginEditText.requestFocus()
            return
        }

        if(passwordLoginEditText.text.toString().isEmpty()){
            passwordLoginEditText.error = "Please enter a password"
            passwordLoginEditText.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(nameLoginEditText.text.toString(), passwordLoginEditText.text.toString())
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    updateUI(user)
                }   else{

                    Toast.makeText(baseContext, "Login Failed",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
}


