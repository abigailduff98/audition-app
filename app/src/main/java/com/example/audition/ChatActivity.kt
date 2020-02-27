package com.example.audition

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatDatabase : DatabaseReference
    private lateinit var chatText : TextView
    private lateinit var username: String
    private lateinit var userColor: Integer
    private lateinit var newUserColor: Integer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Utility.hideSystemUI(window)

        chatDatabase = FirebaseDatabase.getInstance().getReference("/log")

        chatText = findViewById(R.id.chatbox)

        username = "Anonymous"

        userColor = Color.RED as Integer
        newUserColor = Color.RED as Integer

        chatText.movementMethod = ScrollingMovementMethod()

        val chatListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //clear chat
                chatText.setText("")
                userColor = newUserColor
                var completeSpannable = SpannableStringBuilder()

                for (snapshot in dataSnapshot.children) {
                    val msg = snapshot.getValue(ChatMessage::class.java)
                    val oldText = completeSpannable.toString()

                    var userText =
                        when (oldText == "") {
                            true ->msg!!.readUser()
                            false -> "\n" + msg!!.readUser()
                        }

                    val msgText = ": " + msg!!.readMessage()

                    completeSpannable.append(userText,ForegroundColorSpan(userColor.toInt()), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    completeSpannable.append(msgText, ForegroundColorSpan(ResourcesCompat.getColor(getResources(), R.color.colorTextPrimary, null)), Spanned.SPAN_INCLUSIVE_INCLUSIVE)

                }

                chatText.setText(completeSpannable, TextView.BufferType.SPANNABLE)

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

    fun openColorMenu(view: View) {

        newUserColor = Color.YELLOW as Integer

        /*val popupMenu = PopupMenu(this, view)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_color_karen -> {
                    userColor = Color.RED as Integer
                    true
                }
                R.id.menu_color_kaoruko -> {
                    userColor = Color.MAGENTA as Integer
                    true
                }
                R.id.menu_color_hikari -> {
                    userColor = Color.BLUE as Integer
                    true
                }
                R.id.menu_color_mahiru -> {
                    userColor = Color.GREEN as Integer
                    true
                }
                R.id.menu_color_maya -> {
                    userColor = Color.LTGRAY as Integer
                    true
                }
                R.id.menu_color_claudine -> {
                    userColor = Color.DKGRAY as Integer
                    true
                }
                R.id.menu_color_junna -> {
                    userColor = Color.CYAN as Integer
                    true
                }
                R.id.menu_color_nana -> {
                    userColor = Color.YELLOW as Integer
                    true
                }
                R.id.menu_color_futaba -> {
                    userColor = Color.MAGENTA as Integer
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.menu_main)
        popupMenu.show()*/
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