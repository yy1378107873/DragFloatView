package com.song.kotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

/**
 * @author : songjun
 * @create : 2020/4/29
 */

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById<TextView>(R.id.tv);
        textView?.postDelayed(
                Runnable {
                    var add = add(5, 6);
                    textView.setText(add.toString())
                }, 200
        )
        textView.setOnClickListener {
            startActivity(Intent(this,SecondActivity().javaClass))
//            val intent = Intent();
//            intent.setClass(this,SecondActivity().javaClass);
        }
    }

    fun add(a: Int, b: Int): Int {
        return a + b;
    }
}
