package com.makalaster.tweetr.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Makalaster on 4/20/17.
 */

public class TweetHolder extends RecyclerView.ViewHolder {
    public TextView mBody, mTime;

    public TweetHolder(View itemView) {
        super(itemView);

        mBody = (TextView) itemView.findViewById(android.R.id.text1);
        mTime = (TextView) itemView.findViewById(android.R.id.text2);
    }
}
