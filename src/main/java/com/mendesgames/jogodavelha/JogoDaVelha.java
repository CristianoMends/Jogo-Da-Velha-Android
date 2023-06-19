package com.mendesgames.jogodavelha;

public class JogoDaVelha {
    private char[][] tabuleiro;
    private char jogadorAtual;
    private int empates=0;
    private int posicaoVitoria[];
    private int consecutivos0=0;
    private int consecutivos1=0;
    private int consecutivos2=0;
    private int score1=0;
    private int score2=0;
    GameView gameView = new GameView();
    public JogoDaVelha() {
        tabuleiro = new char[3][3];
        jogadorAtual = 'X'; // Começa com o jogador X
        posicaoVitoria = new int[2];
        inicializarTabuleiro();
    }

    public int[] getPosicaoVitoria() {
        return posicaoVitoria;
    }


    public char getJogadorAtual(){
        return this.jogadorAtual;
    }

    public int getScore1() {
        return score1;
    }

    public int getConsecutivos0(){
        return this.consecutivos0;
    }
    public void setConsecutivos0(int n){
        this.consecutivos0 = n;
    }
    public int getConsecutivos1(){
        return this.consecutivos1;
    }
    public void setConsecutivos1(int n){
        this.consecutivos1 = n;
    }

    public int getConsecutivos2(){
        return this.consecutivos2;
    }
    public void setConsecutivos2(int n){
        this.consecutivos2 = n;
    }

    public int getScore2(){
        return this.score2;
    }
    public int getEmpates(){
        return this.empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
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
        }
    }

    public boolean verificarVitoria() {
        // Verifica linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] != ' ' && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][0] == tabuleiro[i][2]) {
                if (jogadorAtual == 'O'){
                    score1++;
                }else{
                    score2++;
                }
                return true;
            }
        }

        // Verifica colunas
        for (int j = 0; j < 3; j++) {
            if (tabuleiro[0][j] != ' ' && tabuleiro[0][j] == tabuleiro[1][j] && tabuleiro[0][j] == tabuleiro[2][j]) {
                if (jogadorAtual == 'O'){
                    score1++;
                }else{
                    score2++;
                }
                return true;
            }
        }

        // Verifica diagonais
        if (tabuleiro[0][0] != ' ' && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[0][0] == tabuleiro[2][2]) {
            if (jogadorAtual == 'O'){
                score1++;
            }else{
                score2++;
            }
            return true;
        }

        if (tabuleiro[0][2] != ' ' && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[0][2] == tabuleiro[2][0]) {
            if (jogadorAtual == 'O'){
                score1++;
            }else{
                score2++;
            }
            return true;
        }

        return false;
    }

    public boolean verificarEmpate() {
        // Verifica se todas as posições estão preenchidas
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void reiniciar() {
        jogadorAtual = 'X';
        // Preenche o tabuleiro com espaços em branco
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = ' ';
            }
        }
    }
}


