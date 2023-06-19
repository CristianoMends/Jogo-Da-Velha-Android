package com.mendesgames.jogodavelha

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var btnReset: Button;

    private lateinit var empates:TextView;
    private lateinit var score1:TextView;
    private lateinit var score2:TextView;

    var botoes: Array<Array<Int>> = arrayOf()
    val jogoDaVelha = JogoDaVelha()
    val gameView = GameView()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        botoes = arrayOf(
            arrayOf(R.id.btn00, R.id.btn01, R.id.btn02),
            arrayOf(R.id.btn10, R.id.btn11, R.id.btn12),
            arrayOf(R.id.btn20, R.id.btn21, R.id.btn22)
        )

        empates = findViewById(R.id.textEmpates)
        score1 = findViewById(R.id.textScore1)
        score2 = findViewById(R.id.textScore2)

    }
    fun coisar(v: View){
        val alert = AlertDialog.Builder(this);//Instancia caixa de alert
        for (i in botoes.indices) {
            for (j in botoes[i].indices) {
                if(findViewById<ImageView>(botoes[i][j]).isPressed){
                    val btnAtual = findViewById<ImageView>(botoes[i][j])

                        if(jogoDaVelha.jogadorAtual == 'X'){//se o jogador atual for X
                            btnAtual.setImageResource(gameView.x)//recebe a imagem de X
                        }else{
                            btnAtual.setImageResource(gameView.o)//se nao recebe a imagem de O
                        }
                        btnAtual.isEnabled = false
                        jogoDaVelha.jogar(i,j);
                }
            }
        }
        //verifica se houve empate
        if(jogoDaVelha.verificarEmpate()){
            jogoDaVelha.setConsecutivos0(jogoDaVelha.getConsecutivos0() + 1);
            jogoDaVelha.setConsecutivos1(0);
            jogoDaVelha.setConsecutivos2(0);
            //--------------------------------------------------------------
            //conta empates consecutivos e adiciona mensagem personalizada
            if(jogoDaVelha.getConsecutivos0() == 1){
                alert.setMessage(R.string.empate1)
            }else if(jogoDaVelha.getConsecutivos0() == 2){
                alert.setMessage(R.string.empate2)
            }else if(jogoDaVelha.getConsecutivos0() == 3){
                alert.setMessage(R.string.empate3)
            }else if(jogoDaVelha.getConsecutivos0() == 4){
                alert.setMessage(R.string.empate4)
            }
            //--------------------------------------------------------------

            jogoDaVelha.setEmpates(jogoDaVelha.getEmpates() + 1);

            //se tem adiciona uma mesagem
            alert.setTitle("Empatou!")
                .setPositiveButton("Reiniciar") { dialog, _ ->
                    reiniciar()
                }
            alert.create()
            alert.show()
            empates.setText(jogoDaVelha.empates.toString())
        }

        //----------------------------------------------------------------------

        if(jogoDaVelha.verificarVitoria() && !jogoDaVelha.verificarEmpate()){
            //se tem adiciona uma mesagem
            alert.setMessage("Parabêns!")
            if(jogoDaVelha.jogadorAtual == 'X'){
                alert.setIcon(R.drawable.o)
                jogoDaVelha.setConsecutivos2(jogoDaVelha.getConsecutivos2() + 1);
                jogoDaVelha.setConsecutivos1(0);
                jogoDaVelha.setConsecutivos0(0);
                //--------------------------------------------------------------
                //conta empates consecutivos e adiciona mensagem personalizada
                if(jogoDaVelha.getConsecutivos2() == jogoDaVelha.getConsecutivos1()){

                }else if(jogoDaVelha.getConsecutivos2() == 1){
                    alert.setMessage(getString(R.string.Jogador2) + getString(R.string.vitoria1))
                }else if(jogoDaVelha.getConsecutivos2() == 2){
                    alert.setMessage(getString(R.string.Jogador2) + getString(R.string.vitoria2))
                }else if(jogoDaVelha.getConsecutivos2() == 3){
                    alert.setMessage(getString(R.string.Jogador2) + getString(R.string.vitoria3))
                }else if(jogoDaVelha.getConsecutivos2() == 4){
                    alert.setMessage(getString(R.string.Jogador2) + getString(R.string.vitoria4))
                }
                //--------------------------------------------------------------
            }else{
                alert.setIcon(R.drawable.x)
                jogoDaVelha.setConsecutivos1(jogoDaVelha.getConsecutivos1() + 1);
                jogoDaVelha.setConsecutivos0(0);
                jogoDaVelha.setConsecutivos2(0);
                //--------------------------------------------------------------
                //conta empates consecutivos e adiciona mensagem personalizada
                if(jogoDaVelha.getConsecutivos1() == 1){
                    alert.setMessage(getString(R.string.Jogador1) + getString(R.string.vitoria1))
                }else if(jogoDaVelha.getConsecutivos1() == 2){
                    alert.setMessage(getString(R.string.Jogador1) + getString(R.string.vitoria2))
                }else if(jogoDaVelha.getConsecutivos1() == 3){
                    alert.setMessage(getString(R.string.Jogador1) + getString(R.string.vitoria3))
                }else if(jogoDaVelha.getConsecutivos1() == 4){
                    alert.setMessage(getString(R.string.Jogador1) + getString(R.string.vitoria4))
                }
                //--------------------------------------------------------------
            }
            alert.setTitle("VENCEU!")
                .setPositiveButton("Reiniciar") { dialog, _ ->
                    reiniciar()
                }
            alert.create()
            alert.show()
            score1.setText(jogoDaVelha.score1.toString())
            score2.setText(jogoDaVelha.score2.toString())

        }
        //----------------------------------------------------------------------
    }
    fun reiniciar(){
        jogoDaVelha.reiniciar()
        for (i in botoes.indices) {
            for (j in botoes[i].indices) {
                findViewById<ImageView>(botoes[i][j]).setImageResource(0);
                findViewById<ImageView>(botoes[i][j]).isEnabled = true;

            }
        }
    }


}