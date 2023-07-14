package com.mendesgames.jogodavelha;

import java.util.ArrayList;
import java.util.List;

public class JogoDaVelha {
    private char[][] tabuleiro;
    private char jogadorAtual;
    private int id;



    GameView gameView = new GameView();

    public JogoDaVelha() {
        tabuleiro = new char[3][3];
        id = gameView.getX();
        inicializarTabuleiro();
    }

    public char getJogadorAtual(){
        return this.jogadorAtual;
    }
    public void setJogadorAtual(char l){
        this.jogadorAtual = l;
    }

    public int getId() {
        return id;
    }

    public char[][] getTabuleiro() {
        return tabuleiro;
    }

    private void inicializarTabuleiro() {
        // Preenche o tabuleiro com espaços em branco
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = ' ';
            }
        }
    }

    public void jogar(int linha, int coluna) {
        if (tabuleiro[linha][coluna] == ' ') {
            tabuleiro[linha][coluna] = jogadorAtual;
            jogadorAtual = (jogadorAtual == 'X') ? 'O' : 'X';

            if (jogadorAtual == 'X'){
                id = gameView.getX();
            } else if (jogadorAtual == 'O') {
                id = gameView.getO();
            }

        }
    }

    public void reiniciar() {
        jogadorAtual = 'X';
        id = gameView.getX();

        // Preenche o tabuleiro com espaços em branco
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = ' ';
            }
        }
    }


}


