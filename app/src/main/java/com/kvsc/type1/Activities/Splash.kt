package com.kvsc.type1.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.kvsc.type1.R


class Splash : AppCompatActivity() {
    lateinit var shine: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        shine = findViewById(R.id.shine)
        shineAnimation()
        Handler().postDelayed({
            val i = Intent(this@Splash, Login::class.java)
            startActivity(i)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun shineAnimation() {
        // attach the animation layout Using AnimationUtils.loadAnimation
        val anim = AnimationUtils.loadAnimation(this, R.anim.left_right)
        shine.startAnimation(anim)
        // override three function There will error
        // line below the object
        // click on it and override three functions
        anim.setAnimationListener(object : Animation.AnimationListener {
            // This function starts the
            // animation again after it ends
            override fun onAnimationEnd(p0: Animation?) {
                val layoutParams: ViewGroup.LayoutParams = shine.getLayoutParams()
                layoutParams.width = 0
                shine.setLayoutParams(layoutParams)
            }

            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

        })
    }
    companion object {
        var SPLASH_TIME_OUT = 1000
    }
}