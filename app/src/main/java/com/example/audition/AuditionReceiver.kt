package com.example.audition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri


class AuditionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val openNoticeIntent = Intent(context, NoticeActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra("ACTIVE", true)

        context.startActivity(openNoticeIntent)

        val soundUri = Uri
            .parse(
                "android.resource://" + context.packageName + "/"
                        + R.raw.ki_ringtone
            )

        val r = RingtoneManager.getRingtone(context, soundUri)
        r.play()
    }

}