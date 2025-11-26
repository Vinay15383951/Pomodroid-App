package com.example.pomodoro

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ImageButton

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create layout programmatically
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Top bar
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(0xFFEEEEEE.toInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val backButton = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_revert)
            setBackgroundColor(0x00000000)
            setOnClickListener { finish() }
        }

        val title = TextView(this).apply {
            text = "About Pomodoro"
            textSize = 20f
            setPadding(16, 0, 0, 0)
            setTextColor(0xFF333333.toInt())
        }

        header.addView(backButton)
        header.addView(title)

        val webView = WebView(this)

        // ✅ Apply smooth scrolling and performance settings here
        webView.apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true

            // Smooth scrolling
            isVerticalScrollBarEnabled = true
            isHorizontalScrollBarEnabled = false
            overScrollMode = WebView.OVER_SCROLL_IF_CONTENT_SCROLLS
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true

            // Optional: for better rendering performance
            setLayerType(WebView.LAYER_TYPE_HARDWARE, null)
        }


        val url = intent.getStringExtra("url") ?: "https://pomofocus.io"
        webView.loadUrl(url)

        layout.addView(header)
        layout.addView(webView)

        setContentView(layout)
    }
}
