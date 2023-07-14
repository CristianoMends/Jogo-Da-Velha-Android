package com.mendesgames.jogodavelha

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


@Suppress("DEPRECATION")
class InicioApp : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var adView1: AdView
    private lateinit var adView2: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        val handler = Handler()
        handler.postDelayed({
            setContentView(R.layout.activity_inicio_app)

               adView1 = findViewById(R.id.adView)
                adView2 = findViewById(R.id.adView3)

                MobileAds.initialize(this) {}

                adView1.loadAd(AdRequest.Builder().build())
                adView2.loadAd(AdRequest.Builder().build())
            mediaPlayer = MediaPlayer.create(this, R.raw.inicio)
            mediaPlayer.isLooping = true
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }
        }, 1000)

    }
    override fun onResume() {
        super.onResume()
        if (this::mediaPlayer.isInitialized && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    fun iniciarGame(v: View){
        var tipoDeGame = 0//1 == jogador vs cpu
        var jogador = ' '
        val btn = v as Button
        if(btn.id == R.id.btnCpu){
            tipoDeGame = 1
        }

            val intent = Intent(this, MainActivity::class.java)
            jogador = 'X'
            intent.putExtra("tipoDeGame", tipoDeGame)
            intent.putExtra("jogador", jogador)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()

    }
}