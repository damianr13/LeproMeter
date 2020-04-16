package com.bigdict.leprometer.groups

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bigdict.leprometer.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_group.*


class AddGroup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        //var groupName = groupnameEditText.text.toString()

        val db = FirebaseFirestore.getInstance()

        data class groupToAdd(
            val groupName: String? = null
        )


        addGroupButton.setOnClickListener(){

            val groupToAdd = hashMapOf(
                "groupName" to groupnameEditText.text.toString()
            )
/*
            db.collection("cities")
                .add(groupToAdd as Map<String, Any>)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(baseContext, "Group created succesfully",
                        Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(baseContext, "Group creation failed",
                        Toast.LENGTH_SHORT).show()
                }

*/

            db.collection("data").document("one")
                .set(groupToAdd as Map<String, Any>)
                .addOnSuccessListener { Toast.makeText(baseContext, "Group created succesfully",
                    Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(baseContext, "Group creation failed",
                    Toast.LENGTH_SHORT).show()}

        }

        backButton.setOnClickListener(){
            startActivity(Intent(this, Groups::class.java))
        }
    }
}
