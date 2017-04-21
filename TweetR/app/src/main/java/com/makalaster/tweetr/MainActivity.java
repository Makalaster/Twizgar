package com.makalaster.tweetr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
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

    private void getAccessToken() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url("https://api.twitter.com/oauth2/token")
                .post(body)
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
                    System.out.println("mtoken: " + mToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTimeline(String id) {
        OkHttpClient client = new OkHttpClient();
    }
}
