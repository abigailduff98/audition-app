package com.example.audition

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class NoticeActivity : AppCompatActivity() {

    companion object {
        const val ACTIVE = true
        const val INACTIVE = false
    }

    private lateinit var giraffeImageView: ImageView
    private var noticeStatus = INACTIVE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noticeStatus = intent.getBooleanExtra("ACTIVE", false)

        when (noticeStatus == ACTIVE) {
            true -> setContentView(R.layout.activity_notice_active)
            false -> setContentView(R.layout.activity_notice_inactive)
        }

        Utility.hideSystemUI(window)
    }

    override fun onStart() {
        super.onStart()
        Utility.hideSystemUI(window)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        animate()
    }

    private fun animate() {
        giraffeImageView = findViewById(R.id.giraffeImageView)
        fadeIn.run()
        if (noticeStatus == ACTIVE)
            rotate()
    }

    private val fadeIn = Runnable {
        giraffeImageView.animate().setDuration(500)
            .setInterpolator(LinearInterpolator()).alpha(1.0f)
    }

    private fun rotate() {
        val rotate = RotateAnimation(
            360f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotate.duration = 20000
        rotate.repeatCount = Animation.INFINITE
        rotate.interpolator = LinearInterpolator()
        giraffeImageView.startAnimation(rotate)
    }

    fun switchLayout(@Suppress("UNUSED_PARAMETER") view: View) {
        if (noticeStatus == ACTIVE) {
            noticeStatus = INACTIVE
            setContentView(R.layout.activity_notice_inactive)
        } else {
            noticeStatus = ACTIVE
            setContentView(R.layout.activity_notice_active)
        }
        animate()
    }

    fun openChat(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(
            this@NoticeActivity,
            ChatActivity::class.java
        )
        startActivity(intent)
    }

    fun setAlarm(@Suppress("UNUSED_PARAMETER") view: View) {

        // Get the AlarmManager Service
        val mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an Intent to broadcast to the AuditionReceiver
        val mAuditionReceiverIntent = Intent(
            this@NoticeActivity,
            AuditionReceiver::class.java
        )

        // Create an PendingIntent that holds the auditionReceiverIntent
        val mAuditionReceiverPendingIntent = PendingIntent.getBroadcast(
            this@NoticeActivity, 0, mAuditionReceiverIntent, 0
        )

        // Set single alarm
        mAlarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            mAuditionReceiverPendingIntent
        )

        // Show Toast message
        Toast.makeText(
            applicationContext, "Alarm Set",
            Toast.LENGTH_LONG
        ).show()

    }

}
