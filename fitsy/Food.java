package com.example.fitsy;

import java.util.ArrayList;

public class Food {
    private ArrayList<String> favorite;

    public Food(ArrayList<String> favorite){
        this.favorite = favorite;
    }

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<String> favorite) {
        this.favorite = favorite;
    }

}
