package com.example.audition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import java.util.Collections.rotate





class NoticeActivity : AppCompatActivity() {

    companion object {
        const val ACTIVE = true
        const val INACTIVE = false
    }

    private lateinit var giraffeImageView: ImageView
    private var noticeStatus = ACTIVE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (noticeStatus == ACTIVE)
            setContentView(R.layout.activity_notice_active)
        else
            setContentView(R.layout.activity_notice_inactive)

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

    @Suppress("UNUSED_PARAMETER")
    fun switchLayout(view : View) {
        if (noticeStatus == ACTIVE) {
            noticeStatus = INACTIVE
            setContentView(R.layout.activity_notice_inactive)
        }
        else {
            noticeStatus = ACTIVE
            setContentView(R.layout.activity_notice_active)
        }
        animate()
    }

    fun openChat(view : View) {
        val intent = Intent(
            this@NoticeActivity,
            ChatActivity::class.java
        )
        startActivity(intent)
    }

}
