package com.makalaster.tweetr.gson;

/**
 * Created by Makalaster on 4/20/17.
 */

public class Tweet {
    private String created_at, text;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
