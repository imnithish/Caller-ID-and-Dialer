package com.imn.whocalling.callalert

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.imn.whocalling.R
import com.imn.whocalling.network.ApiService
import com.imn.whocalling.util.ResultWrapper
import com.imn.whocalling.util.fetchMockyAPI
import com.imn.whocalling.util.logd
import com.imn.whocalling.util.startUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncomingCallNotification {

    companion object {
        private const val WINDOW_WIDTH_RATIO = 0.8f

        private var windowManager: WindowManager? = null

        @SuppressLint("StaticFieldLeak")
        private var windowLayout: ViewGroup? = null
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private lateinit var numberTextView: TextView
    private lateinit var progressIndicator: ProgressBar
    private lateinit var nameTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var spamTextView: TextView
    private lateinit var cancelButton: Button
    private lateinit var dot: View

    private var params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        windowType,
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT,
    ).apply {
        gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        y = 600
        format = 1
    }

    private var x = 0f

    private var y = 0f

    private val windowType: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

    private val WindowManager.windowWidth: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            (WINDOW_WIDTH_RATIO * (windowMetrics.bounds.width() - insets.left - insets.right)).toInt()
        } else {
            DisplayMetrics().apply {
                defaultDisplay?.getMetrics(this)
            }.run {
                (WINDOW_WIDTH_RATIO * widthPixels).toInt()
            }
        }


    fun fetchAPI(apiService: ApiService, phone: String) {
        coroutineScope.launch {
            when (val res = apiService.fetchMockyAPI()) {
                is ResultWrapper.Error -> {
                    "${res.exception}".logd()
                    progressIndicator.visibility = View.GONE
                }

                is ResultWrapper.Success -> {
                    "${res.value.body()}".logd()
                    withContext(Dispatchers.Main) {
                        progressIndicator.visibility = View.GONE

                        val specificNumber = res.value.body()?.numbers?.find { it.number == phone }
                        specificNumber?.let {
                            nameTextView.text = it.name
                            nameTextView.visibility = View.VISIBLE
                            locationTextView.text = it.location
                            locationTextView.visibility = View.VISIBLE

                            if (it.isSpam) {
                                dot.visibility = View.VISIBLE
                                spamTextView.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    fun showWindow(context: Context, phone: String, apiService: ApiService) {
        if (windowLayout == null) {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager?.let { windowManager ->
                windowLayout =
                    View.inflate(context, R.layout.layout_incoming_call_banner, null) as ViewGroup?
                windowLayout?.let { windowLayout ->
                    params.width = windowManager.windowWidth

                    numberTextView = windowLayout.findViewById<TextView>(R.id.number)
                    progressIndicator = windowLayout.findViewById<ProgressBar>(R.id.progress)
                    nameTextView = windowLayout.findViewById<TextView>(R.id.name)
                    locationTextView = windowLayout.findViewById<TextView>(R.id.location)
                    spamTextView = windowLayout.findViewById<TextView>(R.id.spam)
                    dot = windowLayout.findViewById<TextView>(R.id.dot)

                    progressIndicator.visibility = View.VISIBLE
                    numberTextView.text = phone

                    cancelButton = windowLayout.findViewById<Button>(R.id.cancel)
                    cancelButton.setOnClickListener {
                        closeWindow()
                    }

                    windowLayout.setOnClickListener {
                        context.startUp()
                        closeWindow()
                    }

                    windowManager.addView(windowLayout, params)
                    setOnTouchListener()
                }
            }
        }
        fetchAPI(apiService, phone)
    }

    fun closeWindow() {
        if (windowLayout != null) {
            windowManager?.removeView(windowLayout)
            windowManager = null
            windowLayout = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener() {
        windowLayout?.setOnTouchListener { view: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX
                    y = event.rawY
                }

                MotionEvent.ACTION_MOVE -> updateWindowLayoutParams(event)
                MotionEvent.ACTION_UP -> view.performClick()
                else -> Unit
            }
            false
        }
    }

    private fun updateWindowLayoutParams(event: MotionEvent) {
        params.x -= (x - event.rawX).toInt()
        params.y -= (y - event.rawY).toInt()
        windowManager?.updateViewLayout(windowLayout, params)
        x = event.rawX
        y = event.rawY
    }
}
