package com.mendesgames.jogodavelha;

import android.view.*;

public class GameView {
    private int x;
    private int o;
    public GameView(){
         x = R.drawable.x;
         o = R.drawable.o;
    }
    public int getX(){
        return this.x;
    }
    public int getO(){
        return this.o;
    }

}
