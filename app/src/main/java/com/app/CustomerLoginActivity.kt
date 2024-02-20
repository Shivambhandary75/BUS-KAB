package com.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CustomerLoginActivity : AppCompatActivity() {

    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mLogin: Button
    private lateinit var mRegisteration: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)

        mAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = FirebaseAuth.AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if(user != null){
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }

        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password)

        mLogin = findViewById(R.id.login)
        mRegisteration = findViewById(R.id.registeration)

        mRegisteration.setOnClickListener {
            val email = mEmail.text.toString()
            val password = mPassword.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
                if(!task.isSuccessful){
                    Toast.makeText(this@CustomerLoginActivity, "sign up error", Toast.LENGTH_SHORT).show()
                } else{
                    val user_id = mAuth.currentUser?.uid
                    val current_user_db = FirebaseDatabase.getInstance().getReference("Users/Customers/$user_id")
                    current_user_db.setValue(true)
                }
            }
        }

        mLogin.setOnClickListener {
            val email = mEmail.text.toString()
            val password = mPassword.text.toString()
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                if(!task.isSuccessful){
                    Toast.makeText(this@CustomerLoginActivity, "sign in error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(firebaseAuthListener)
    }
}