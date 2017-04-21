package com.makalaster.tweetr;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makalaster.tweetr.gson.Tweet;
import com.makalaster.tweetr.recycler.TimelineAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String mToken;
    private EditText mSearchEditText;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        mSearchEditText = (EditText) findViewById(R.id.search_text);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            getAccessToken();
            findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mToken != null) {
                        if (mSearchEditText.getText().toString().isEmpty()) {
                            mSearchEditText.setError("Please enter an ID");
                            mSearchEditText.requestFocus();
                        } else {
                            getTimeline(mSearchEditText.getText().toString());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Auth issue...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getAccessToken() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        String bearerToken = TwitterAppData.CONSUMER_KEY + ":" + TwitterAppData.CONSUMER_SECRET;
        byte[] byteToken = bearerToken.getBytes();
        String base64Token = Base64.encodeToString(byteToken, Base64.DEFAULT);

        System.out.println(base64Token);

        Request request = new Request.Builder()
                .url("https://api.twitter.com/oauth2/token")
                .post(body)
                //.header("Authorization", "Basic " + base64Token)
                .header("Authorization", "Basic UVlaRFRzYXB2QWw1cnQ2eWNIOFc1MjZ6STo4bUd2aGRsUVpuekxWWU1nMGE5R1hJS294VkdjSEp6eXlld3BZb2RqWmlWVUlUVUlvSw==")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("bad twit");
                }

                String jsonResponse = response.body().string();

                try {
                    JSONObject reply = new JSONObject(jsonResponse);
                    mToken = reply.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTimeline(String id) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.twitter.com/1.1/statuses/user_timeline.json?count=20&screen_name=" + id)
                .header("Authorization", "Bearer " + mToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {

                    throw new IOException("id not found");
                }

                String json = response.body().string();

                Gson gson = new Gson();
                Tweet[] tweets = gson.fromJson(json, Tweet[].class);

                final List<Tweet> tweetList = Arrays.asList(tweets);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecycler.setAdapter(new TimelineAdapter(tweetList));
                    }
                });

            }
        });
    }
}
