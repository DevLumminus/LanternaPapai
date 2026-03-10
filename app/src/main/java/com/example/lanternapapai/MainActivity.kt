package com.example.lanternapapai

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import com.google.android.material.slider.Slider
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var flashLigado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]

        val slider = findViewById<Slider>(R.id.sliderIntensity)
        val btnSOS = findViewById<Button>(R.id.btnSOS)

        // BOTÃO PRINCIPAL DA LANTERNA
        val btnFlash = findViewById<FrameLayout>(R.id.btnFlash)
        val imgFlash = findViewById<ImageView>(R.id.imgFlash)

        btnFlash.setOnClickListener {
            flashLigado = !flashLigado
            cameraManager.setTorchMode(cameraId, flashLigado)

            if (flashLigado) {
                imgFlash.setImageResource(R.drawable.twotone_brightness_6_24)
                btnFlash.setBackgroundColor(Color.YELLOW)
            } else {
                imgFlash.setImageResource(R.drawable.outline_dark_mode_24)
                btnFlash.setBackgroundColor(Color.GRAY)
            }
        }

        // CONTROLE DE INTENSIDADE
        slider.addOnChangeListener { _, value, _ ->
            try {
                cameraManager.turnOnTorchWithStrengthLevel(cameraId, value.toInt())
            } catch (_: Exception) {}
        }

        // BOTÃO SOS
        btnSOS.setOnClickListener {
            sos()
        }
    }

    private fun sos() {
        Thread {
            val short = 200L
            val long = 600L
            val gap = 200L

            fun flash(time: Long) {
                cameraManager.setTorchMode(cameraId, true)
                Thread.sleep(time)
                cameraManager.setTorchMode(cameraId, false)
                Thread.sleep(gap)
            }

            try {
                // S
                repeat(3) { flash(short) }
                Thread.sleep(400)
                // O
                repeat(3) { flash(long) }
                Thread.sleep(400)
                // S
                repeat(3) { flash(short) }
            } catch (_: Exception) {}
        }.start()
    }
}