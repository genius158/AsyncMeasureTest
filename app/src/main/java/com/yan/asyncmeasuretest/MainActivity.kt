package com.yan.asyncmeasuretest

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val fab by lazy { findViewById<View>(R.id.tvTest) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        fab.setOnClickListener { view ->
            paddingAnim(true)
        }
    }

    /**
     * padding动画，纯测试 requestLayout的调用
     */
    private var valueAnim = ValueAnimator.ofFloat(0F, 1F).apply {
        addUpdateListener { anim ->
            val ratio = anim.animatedValue as Float
            val padding = 300
            fab.setPadding(
                (padding * ratio).toInt(),
                (padding * ratio).toInt(),
                (padding * ratio).toInt(),
                (padding * ratio).toInt()
            )
        }
    }

    private fun paddingAnim(anim: Boolean) {
        valueAnim.cancel()
        valueAnim.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}