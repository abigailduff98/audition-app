package com.example.audition

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatDatabase: DatabaseReference
    private lateinit var chatText: TextView
    private lateinit var username: String
    private lateinit var userPreview: TextView
    private var userColor = Color.RED

    override fun onCreate(savedInstanceState: Bundle?) {

        // create the view
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // initialize variables
        username = "Anonymous"
        userColor = Color.RED
        chatDatabase = FirebaseDatabase.getInstance().getReference("/log")
        chatText = findViewById(R.id.chatTextView)
        userPreview = findViewById(R.id.previewTextView2)

        randomizeUserColor()
        updateUserPreview()

        // set up the chat to respond to data changes
        val chatListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //clear chat
                chatText.text = ""

                // begin building updated text to display
                val completeSpannable = SpannableStringBuilder()

                for (snapshot in dataSnapshot.children) {

                    // read the username and message
                    val msg = snapshot.getValue(ChatMessage::class.java)
                    val oldText = completeSpannable.toString()

                    val userText =
                        when (oldText == "") {
                            true -> msg!!.readUser()
                            false -> "\n" + msg!!.readUser()
                        }

                    val msgText = ": " + msg.readMessage()

                    // add the user
                    completeSpannable.append(
                        userText,
                        ForegroundColorSpan(msg.color),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )

                    // get default text color
                    val color = getResourceColor(R.color.colorTextPrimary)

                    // add the msg
                    completeSpannable.append(
                        msgText,
                        ForegroundColorSpan(color),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )

                }

                // set the new chat text
                chatText.setText(completeSpannable, TextView.BufferType.SPANNABLE)

                // make the chat scroll to the bottom
                val chatScrollView = findViewById<ScrollView>(R.id.chatScrollView)
                chatScrollView.post { chatScrollView.fullScroll(View.FOCUS_DOWN) }

            }

            override fun onCancelled(error: DatabaseError) {
                chatText.text = resources.getString(R.string.cancelled)
            }

        }

        // set the listener
        chatDatabase.addValueEventListener(chatListener)
    }

    private fun updateUserPreview() {

        val currentName = SpannableStringBuilder()
        currentName.append(
            username,
            ForegroundColorSpan(userColor),
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        userPreview.setText(currentName, TextView.BufferType.SPANNABLE)

    }

    private fun randomizeUserColor() {

        val colors = listOf(
            R.color.karen,
            R.color.kaoruko,
            R.color.claudine,
            R.color.nana,
            R.color.maya,
            R.color.mahiru,
            R.color.junna,
            R.color.hikari,
            R.color.futaba
        )

        userColor = getResourceColor(colors.shuffled().take(1)[0])

    }

    private fun getResourceColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    private fun changeColor(resourceColor: Int): Boolean {

        userColor = getResourceColor(resourceColor)
        updateUserPreview()
        return true

    }

    fun sendMessage(@Suppress("UNUSED_PARAMETER") view: View) {

        val textField = findViewById<EditText>(R.id.inputEditText)

        val msg = textField.text.toString()

        // don't send empty messages
        if (msg == "") return

        // add msg to database
        val id = chatDatabase.push().key.toString()
        val message = ChatMessage(id, username, userColor, msg)
        chatDatabase.child(id).setValue(message)

        // clear text
        textField.setText("")
    }

    // called on button press and shows menu for color selector
    fun openColorMenu(view: View) {

        val popupMenu = PopupMenu(this, view)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_color_karen -> changeColor(R.color.karen)
                R.id.menu_color_kaoruko -> changeColor(R.color.kaoruko)
                R.id.menu_color_claudine -> changeColor(R.color.claudine)
                R.id.menu_color_nana -> changeColor(R.color.nana)
                R.id.menu_color_maya -> changeColor(R.color.maya)
                R.id.menu_color_mahiru -> changeColor(R.color.mahiru)
                R.id.menu_color_junna -> changeColor(R.color.junna)
                R.id.menu_color_hikari -> changeColor(R.color.hikari)
                R.id.menu_color_futaba -> changeColor(R.color.futaba)
                else -> false
            }
        }

        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.menu_color, popupMenu.menu)

        // try to force the menu to show the crown icons
        try {

            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)

            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)

        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }
    }

    // called on button press and shows alert to change user
    fun changeUsername(@Suppress("UNUSED_PARAMETER") view: View) {

        val userField = EditText(this)

        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

            // add EditText view
            .setView(userField)

            // make it cancelable
            .setCancelable(false)

            // positive button text and action
            .setPositiveButton("Proceed") { _, _ ->
                username = userField.text.toString()
                updateUserPreview()
            }

            // negative button text and action
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        alert.setTitle("Change Username")

        alert.show()

        val color = getResourceColor(R.color.colorTextPrimary)
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color)

    }

}