package com.makalaster.tweetr.gson;

import java.util.ArrayList;

/**
 * Created by Makalaster on 4/20/17.
 */

public class Timeline {
    private ArrayList<Tweet> root;

    public ArrayList<Tweet> getRoot() {
        return root;
    }

    public void setRoot(ArrayList<Tweet> root) {
        this.root = root;
    }
}
