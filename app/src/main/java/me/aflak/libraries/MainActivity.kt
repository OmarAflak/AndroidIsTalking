package me.aflak.libraries

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import me.aflak.androidistalking.AndroidIsTalking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isTalking = findViewById<AndroidIsTalking>(R.id.is_talking)
        findViewById<Button>(R.id.start).setOnClickListener {
            isTalking.start()
        }
        findViewById<Button>(R.id.stop).setOnClickListener {
            isTalking.stop()
        }
        isTalking.start()
    }
}
