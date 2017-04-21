package com.makalaster.tweetr.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makalaster.tweetr.gson.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makalaster on 4/20/17.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TweetHolder> {
    private List<Tweet> mTweets = new ArrayList<>();

    public TimelineAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new TweetHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        Tweet currentTweet = mTweets.get(position);

        holder.mBody.setText(currentTweet.getText());
        holder.mTime.setText(currentTweet.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
