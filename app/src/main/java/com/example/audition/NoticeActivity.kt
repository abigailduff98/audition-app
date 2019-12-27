package com.example.audition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView

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
        val rotation = AnimationUtils.loadAnimation(this@NoticeActivity, R.anim.rotate)
        rotation.fillAfter = true
        giraffeImageView.startAnimation(rotation)
    }

    fun switchLayout(view: View) {
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

}
