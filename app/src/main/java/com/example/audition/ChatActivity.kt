package com.example.audition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var myDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        Utility.hideSystemUI(window)

        myDatabase = FirebaseDatabase.getInstance().getReference("Message")

        val myText = findViewById(R.id.textView) as TextView

        val chatListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue()
                myText.setText(post.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                myText.setText("CANCELLED")
            }

        }

        myDatabase.addValueEventListener(chatListener)
    }

    fun sendMessage(view: View) {
        var myEditText = findViewById(R.id.editText) as EditText

        val id = myDatabase.push().key.toString()
        val message = myEditText.getText().toString()

        myDatabase.child(id).setValue(message)

        myEditText.setText("")
    }


}