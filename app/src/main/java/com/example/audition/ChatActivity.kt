package com.example.audition

import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.text.method.ScrollingMovementMethod;

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var chatDatabase : DatabaseReference
    private lateinit var chatText : TextView
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Utility.hideSystemUI(window)

        chatDatabase = FirebaseDatabase.getInstance().getReference("/log")

        chatText = findViewById(R.id.chatbox)

        username = "Anonymous"

        chatText.movementMethod = ScrollingMovementMethod()

        val chatListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //clear chat
                chatText.setText("")

                for (snapshot in dataSnapshot.children) {
                    val msg = snapshot.getValue(ChatMessage::class.java)
                    val oldText = chatText.text.toString()

                    var newText =""
                    if (oldText == "") {
                        newText = msg!!.readUser() + ": " + msg!!.readMessage()
                    } else {
                        newText = "\n" + msg!!.readUser() + ": " + msg!!.readMessage()
                    }
                    chatText.setText(oldText + newText)
                }

                chatText.post {

                    val scrollAmount = chatText.getLayout().getLineTop(chatText.getLineCount()) - chatText.getHeight()
                    // if there is no need to scroll, scrollAmount will be <=0
                    if (scrollAmount > 0)
                        chatText.scrollTo(0, scrollAmount)
                    else
                        chatText.scrollTo(0, 0)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                chatText.setText("CANCELLED")
            }

        }

        chatDatabase.addValueEventListener(chatListener)
    }

    fun sendMessage(view: View) {
        var textField = findViewById(R.id.editText) as EditText

        val msg = textField.text.toString()

        if (msg == "") return

        val id = chatDatabase.push().key.toString()
        val message = ChatMessage(msg, username, id)

        chatDatabase.child(id).setValue(message)

        textField.setText("")
    }

    fun changeUsername(view: View) {

        val userField = EditText(this)

        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        // dialogBuilder.setMessage("Enter your new username")
            // add EditText view
            .setView(userField)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                    username = userField.text.toString()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Change Username")
        // show alert dialog
        alert.show()

    }

}