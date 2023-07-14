package com.mendesgames.jogodavelha

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private lateinit var btnNovoJogo: AppCompatImageButton
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vitoria: MediaPlayer
    private var jogada = MediaPlayer()


    private lateinit var empates:TextView
    private lateinit var score1:TextView
    private lateinit var score2:TextView
    private lateinit var btnCor:ImageButton

    private var botoes: Array<Array<Int>> = arrayOf()
    private val jogoDaVelha = JogoDaVelha()
    private val gameView = GameView()
    private var tipoDeGame = -1
    private var jogador = ' '

    private var p1:Int = 0
    private var p2:Int = 0
    private var emp:Int = 0

    private lateinit var adView1: AdView
    private lateinit var adView2: AdView

    private val PREFS_NAME = "MyPrefs"
    private val PREF_ALERT_SHOWN = "alertShown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        botoes = arrayOf(
            arrayOf(R.id.btn00, R.id.btn01, R.id.btn02),
            arrayOf(R.id.btn10, R.id.btn11, R.id.btn12),
            arrayOf(R.id.btn20, R.id.btn21, R.id.btn22)
        )

        empates = findViewById(R.id.textEmpates)
        score1 = findViewById(R.id.textScore1)
        score2 = findViewById(R.id.textScore2)
        btnNovoJogo = findViewById(R.id.btnReiniciar)
        btnNovoJogo.setOnClickListener { reiniciar() }
        btnCor = findViewById<ImageButton>(R.id.btnCor)
        btnCor.setOnClickListener {
            mudarCor()
        }

        vitoria = MediaPlayer.create(this,R.raw.vitoria)
        jogada = MediaPlayer.create(this,R.raw.marcando)
        mediaPlayer = MediaPlayer.create(this, R.raw.jogando)
        mediaPlayer.isLooping = true


        tipoDeGame = intent.getIntExtra("tipoDeGame", 0)
        jogador = intent.getCharExtra("jogador",' ')
        jogoDaVelha.jogadorAtual = jogador

        if(tipoDeGame == 1){
            findViewById<TextView>(R.id.textView3).setText("IA:")
        }
        adView1 = this.findViewById<AdView>(R.id.adView2)
        adView2 = findViewById<AdView>(R.id.adView3)


       MobileAds.initialize(this) {}

        adView1.loadAd(AdRequest.Builder().build())
        adView2.loadAd(AdRequest.Builder().build())

        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }

        //mostrar mensagem de rating apos 5min, se o usuario ainda nao viu
        val handler= Handler()
        val time:Long = 5 * 60 * 1000
        handler.postDelayed({showRatingDialogOnce()},time)

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


    fun jogar(v: View){

        val btnAtual = v as ImageView
        if(!jogada.isPlaying){
            jogada.start()
        }
        val alert = AlertDialog.Builder(this,R.style.CustomAlertDialog)//Instancia caixa de alert
        for (i in botoes.indices) {
            for (j in botoes[i].indices) {
                if (btnAtual.id == botoes[i][j]) {

                    btnAtual.setImageResource(jogoDaVelha.id)//recebe a imagem de X
                    jogoDaVelha.jogar(i,j)

                    if(jogoDaVelha.jogadorAtual != jogador
                        && tipoDeGame == 1
                        && !verificaVencedor(jogoDaVelha.tabuleiro,'X')
                        && !verificarEmpate()){//se o jogador atual for X

                           val handler = Handler()
                            handler.postDelayed({
                                val (linha, coluna) = melhorJogada(jogoDaVelha.tabuleiro)
                                findViewById<ImageView>(botoes[linha][coluna]).isPressed
                                findViewById<ImageView>(botoes[linha][coluna]).performClick()

                            }, 500/2)//vai executar apos 2 segundos
                            jogoDaVelha.jogar(i,j)
                    }
                    btnAtual.isEnabled = false
                    break
                }
            }
        }

            //adciciona a mensagem de vitoria

            if(verificaVencedor(jogoDaVelha.tabuleiro,'O')){
                val lista = retornarPosicoesVitoria(jogoDaVelha.tabuleiro,'O')
                for (posicao in lista) {
                    val linha = posicao.first
                    val coluna = posicao.second
                    val buttonId = botoes[linha][coluna]
                    val btn = findViewById<ImageView>(buttonId)
                    val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_blink)
                    btn.startAnimation(blinkAnimation)
                }


                if(!vitoria.isPlaying){
                    vitoria.start()
                }
                alert.setIcon(gameView.o)

                p2++
                score2.text = p2.toString()

                /*>>*///vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
                /*>>>>para desabilitar os botoes apos a vitoria                       */
                /*>>*/for (i in botoes.indices) {
                /*>>*/    for (j in botoes[i].indices) {
                /*>>*/        findViewById<ImageView>(botoes[i][j]).isEnabled = false
                /*>>*/    }
                /*>>*/}
                /**///^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                //----------------------------------------------------------------

                if(tipoDeGame == 1) {
                    alert.setTitle("Você PERDEU!")
                }else{
                    alert.setTitle("Jogador O VENCEU!")
                }
                    .setPositiveButton("Novo jogo") { dialog, _ ->
                        reiniciar()
                    }
                alert.create()
                alert.show()

            }else if(verificaVencedor(jogoDaVelha.tabuleiro,'X')){

                val lista = retornarPosicoesVitoria(jogoDaVelha.tabuleiro,'X')
                for (posicao in lista) {
                    val linha = posicao.first
                    val coluna = posicao.second
                    val buttonId = botoes[linha][coluna]
                    val btn = findViewById<ImageView>(buttonId)
                    val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_blink)
                    btn.startAnimation(blinkAnimation)
                }

                if(!vitoria.isPlaying){
                    vitoria.start()
                }
                alert.setIcon(gameView.x)

                p1++
                score1.text = p1.toString()


                /*>>*///vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
                /*>>>>para desabilitar os botoes apos a vitoria                       */
                /*>>*/for (i in botoes.indices) {
                /*>>*/    for (j in botoes[i].indices) {
                /*>>*/        findViewById<ImageView>(botoes[i][j]).isEnabled = false
                /*>>*/    }
                /*>>*/}
                /**///^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                //----------------------------------------------------------------

                if(tipoDeGame == 1){
                    alert.setTitle("Você VENCEU!")
                }else{
                    alert.setTitle("Jogador X VENCEU!")
                }
                    .setPositiveButton("Novo jogo") { dialog, _ ->
                        reiniciar()
                    }

                alert.create()
                alert.show()
            }

        //verifica se houve empate
        if(verificarEmpate()){

            /*>>*///vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
            /*>>>>para desabilitar os botoes apos a vitoria                       */
            /*>>*/for (i in botoes.indices) {
                /*>>*/    for (j in botoes[i].indices) {
                    /*>>*/        findViewById<ImageView>(botoes[i][j]).isEnabled = false
                    /*>>*/    }
                /*>>*/}
            /**///^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            //----------------------------------------------------------------


            emp++
            empates.text = emp.toString()
            if(!vitoria.isPlaying){
                vitoria.start()
            }
            //--------------------------------------------------------------

            alert.setTitle("Empate!")
                .setPositiveButton("Novo Jogo") { dialog, _ ->
                    reiniciar()
                }
            alert.create()
            alert.show()
        }

    }



    private fun reiniciar(){
        jogoDaVelha.reiniciar()
        for (i in botoes.indices) {
            for (j in botoes[i].indices) {
                findViewById<ImageView>(botoes[i][j]).setImageResource(0)
                findViewById<ImageView>(botoes[i][j]).isEnabled = true
                findViewById<ImageView>(botoes[i][j]).clearAnimation()

            }
        }
    }
    fun voltar(v:View){
        val intent = Intent(this, InicioApp::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
    //-------------------------------------------------------------------------------------------


    // Função para determinar se um jogador venceu o jogo
    private fun verificaVencedor(tabuleiro: Array<CharArray>, jogador: Char): Boolean {
        val combinacoesVitoria = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Linhas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Colunas
            listOf(0, 4, 8), listOf(2, 4, 6) // Diagonais
        )

        for (comb in combinacoesVitoria) {
            if (tabuleiro[comb[0] / 3][comb[0] % 3] == jogador &&
                tabuleiro[comb[1] / 3][comb[1] % 3] == jogador &&
                tabuleiro[comb[2] / 3][comb[2] % 3] == jogador) {
                return true
            }
        }

        return false
    }

    // Função para verificar se o tabuleiro está completamente preenchido
    private fun tabuleiroCompleto(tabuleiro: Array<CharArray>): Boolean {
        for (i in tabuleiro.indices) {
            for (j in tabuleiro[i].indices) {
                if (tabuleiro[i][j] == ' ') {
                    return false
                }
            }
        }
        return true
    }

    private fun verificarEmpate():Boolean{
        return (tabuleiroCompleto(jogoDaVelha.tabuleiro)
                && !verificaVencedor(jogoDaVelha.tabuleiro,'X')
                && !verificaVencedor(jogoDaVelha.tabuleiro,'O'))
    }

    // Função Minimax recursiva
    private fun minimax(tabuleiro: Array<CharArray>, jogadorAtual: Char): Int {
        // Verifica se o jogador atual venceu
        if (verificaVencedor(tabuleiro, jogadorAtual)) {
            return if (jogadorAtual == 'X') 1 else -1
        }

        // Verifica se o jogo terminou em empate
        if (tabuleiroCompleto(tabuleiro)) {
            return 0
        }

        val jogadas = mutableListOf<Pair<Int, Int>>()
        val valores = mutableListOf<Int>()

        // Gera todas as possíveis jogadas e calcula os valores Minimax
        for (i in tabuleiro.indices) {
            for (j in tabuleiro[i].indices) {
                if (tabuleiro[i][j] == ' ') {
                    jogadas.add(Pair(i, j))
                    val novoTabuleiro = tabuleiro.map { it.clone() }.toTypedArray()
                    novoTabuleiro[i][j] = jogadorAtual
                    val valor = minimax(novoTabuleiro, if (jogadorAtual == 'X') 'O' else 'X')
                    valores.add(valor)
                }
            }
        }

        // Determina o melhor valor Minimax para o jogador atual
        return if (jogadorAtual == 'X') {
            valores.maxOrNull() ?: 0
        } else {
            valores.minOrNull() ?: 0
        }
    }

    // Função para determinar a melhor jogada usando o algoritmo Minimax
    private fun melhorJogada(tabuleiro: Array<CharArray>): Pair<Int, Int> {
        val jogadorAtual = 'X'
        val jogadas = mutableListOf<Pair<Int, Int>>()
        val valores = mutableListOf<Int>()

        // Gera todas as possíveis jogadas e calcula os valores Minimax
        for (i in tabuleiro.indices) {
            for (j in tabuleiro[i].indices) {
                if (tabuleiro[i][j] == ' ') {
                    jogadas.add(Pair(i, j))
                    val novoTabuleiro = tabuleiro.map { it.clone() }.toTypedArray()
                    novoTabuleiro[i][j] = jogadorAtual
                    val valor = minimax(novoTabuleiro, 'X')
                    valores.add(valor)
                }
            }
        }

        // Encontra o melhor valor Minimax
        val melhorValor = valores.minOrNull() ?: 0

        // Filtra as jogadas com o melhor valor Minimax
        val melhoresJogadas = jogadas.filterIndexed { index, _ -> valores[index] == melhorValor }

        // Verifica se alguma jogada pode levar à vitória imediata para o jogador atual
        val jogadaEscolhida = melhoresJogadas.firstOrNull { jogada ->
            val novoTabuleiro = tabuleiro.map { it.clone() }.toTypedArray()
            novoTabuleiro[jogada.first][jogada.second] = jogadorAtual
            verificaVencedor(novoTabuleiro, jogadorAtual)
        }


        if (jogadaEscolhida == null) {
            // Verifica se alguma jogada pode bloquear uma vitória imediata do outro jogador
            val jogadaBloqueio = melhoresJogadas.firstOrNull { jogada ->
                val novoTabuleiro = tabuleiro.map { it.clone() }.toTypedArray()
                novoTabuleiro[jogada.first][jogada.second] = if (jogadorAtual == 'O') 'X' else 'O'
                verificaVencedor(novoTabuleiro, if (jogadorAtual == 'O') 'X' else 'O')
            }
            println("Não possui jogada para vitoria")
            return jogadaBloqueio ?: melhoresJogadas.first()
        }

        return jogadaEscolhida
    }
    private fun retornarPosicoesVitoria(tabuleiro: Array<CharArray>, jogador: Char): List<Pair<Int, Int>> {
        val combinacoesVitoria = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Linhas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Colunas
            listOf(0, 4, 8), listOf(2, 4, 6) // Diagonais
        )

        for (comb in combinacoesVitoria) {
            val posicoes = mutableListOf<Pair<Int, Int>>()
            for (i in comb) {
                val linha = i / 3
                val coluna = i % 3
                if (tabuleiro[linha][coluna] == jogador) {
                    posicoes.add(Pair(linha, coluna))
                }
            }
            if (posicoes.size == 3) {
                return posicoes
            }
        }

        return emptyList()
    }

    fun mudarCor(){
        val constraintLayout: ConstraintLayout = findViewById(R.id.containerParent)
        val backgroundColor = (constraintLayout.background as? ColorDrawable)?.color

        if (backgroundColor == Color.BLACK) {
            for (i in botoes.indices) {
                for (j in botoes[i].indices) {
                    findViewById<ImageView>(botoes[i][j]).setBackgroundColor(Color.WHITE)
                }
            }
            findViewById<ConstraintLayout>(R.id.containerParent).setBackgroundColor(Color.WHITE)
            findViewById<ConstraintLayout>(R.id.container).setBackgroundColor(Color.BLACK)
            findViewById<GridLayout>(R.id.gridLayout).setBackgroundColor(Color.BLACK)
            btnCor.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            findViewById<TextView>(R.id.textView6).setTextColor(Color.BLACK)
            findViewById<TextView>(R.id.textView10).setTextColor(Color.BLACK)
            findViewById<TextView>(R.id.textView8).setTextColor(Color.BLACK)

        }


        else if(backgroundColor == Color.WHITE){
            for (i in botoes.indices) {
                for (j in botoes[i].indices) {
                    findViewById<ImageView>(botoes[i][j]).setBackgroundColor(Color.BLACK)
                }
            }
            findViewById<ConstraintLayout>(R.id.containerParent).setBackgroundColor(Color.BLACK)
            findViewById<ConstraintLayout>(R.id.container).setBackgroundColor(Color.WHITE)
            findViewById<GridLayout>(R.id.gridLayout).setBackgroundColor(Color.WHITE)
            btnCor.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            findViewById<TextView>(R.id.textView6).setTextColor(Color.WHITE)
            findViewById<TextView>(R.id.textView10).setTextColor(Color.WHITE)
            findViewById<TextView>(R.id.textView8).setTextColor(Color.WHITE)
        }



    }

    fun showRatingDialogOnce(){
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val alertShown = prefs.getBoolean(PREF_ALERT_SHOWN, false)

        if (!alertShown) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Avaliar o aplicativo")
            alertDialogBuilder.setMessage("Gostou do nosso aplicativo? Por favor, deixe uma avaliação na Play Store.")
            alertDialogBuilder.setPositiveButton("Avaliar") { dialog, _ ->
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mendesgames.jogodavelha")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mendesgames.jogodavelha")))
                }
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = alertDialogBuilder.create()
            dialog.show()

            prefs.edit().putBoolean(PREF_ALERT_SHOWN, true).apply()
        }
    }
}