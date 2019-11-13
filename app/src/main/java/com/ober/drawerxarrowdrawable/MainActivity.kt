package com.ober.drawerxarrowdrawable

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var drawerXArrowDrawable: DrawerXArrowDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerXArrowDrawable = DrawerXArrowDrawable(this, DrawerXArrowDrawable.Mode.ARROW)
        //drawerXArrowDrawable.setBarThickness(50f)
        drawerXArrowDrawable.setColor(Color.WHITE)
        supportActionBar?.setHomeAsUpIndicator(drawerXArrowDrawable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        randomlySetMode()

        zoomed_image.setImageDrawable(DrawerXArrowDrawable(this, DrawerXArrowDrawable.Mode.ARROW))
    }

    private fun randomlySetMode() {
        when (Random.nextInt(3)) {
            0 -> {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        drawerXArrowDrawable.setMode(DrawerXArrowDrawable.Mode.DRAWER)
                        randomlySetMode()
                    },
                    1000
                )
            }
            1 -> {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        drawerXArrowDrawable.setMode(DrawerXArrowDrawable.Mode.ARROW)
                        randomlySetMode()
                    },
                    1000
                )
            }
            2 -> {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        drawerXArrowDrawable.setMode(DrawerXArrowDrawable.Mode.X)
                        randomlySetMode()
                    },
                    1000
                )
            }
        }
    }
}
