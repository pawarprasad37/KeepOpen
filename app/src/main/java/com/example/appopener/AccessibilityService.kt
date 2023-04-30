package com.example.appopener

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AccessibilityService : android.accessibilityservice.AccessibilityService() {
    private val PACKAGE_TO_OPEN = "com.warden.cam"

    private var intent: Intent? = null;
    private var handler: Handler? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.i("AccessibilityService", "New event logged")
        if (event == null) {
            Log.i("AccessibilityService", "Event null. Returning.")
            return
        }
        if (PACKAGE_TO_OPEN.equals(event.packageName)) {
            Log.i("AccessibilityService", "App already open. Returning.")
            return
        }
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.i("AccessibilityService", "Window state change event. Opening the app.")
            queueAppOpening();
        }
    }

    private fun queueAppOpening() {
        handler?.removeCallbacksAndMessages(null)
        handler?.postDelayed(Runnable {
            if (intent != null) {
                Log.i("AccessibilityService", "Opening the app now.")
                startActivity(intent)
            } else {
                // App not found
            }
        }, 500)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        handler = Handler(Looper.getMainLooper())
        intent = packageManager.getLaunchIntentForPackage(PACKAGE_TO_OPEN)
        Log.i("AccessibilityService", "Intent generated")
    }

    override fun onInterrupt() {
        //IGNORE
    }


}