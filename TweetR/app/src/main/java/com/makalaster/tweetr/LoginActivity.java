package com.makalaster.tweetr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity {
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.println(generateNonce());

        System.out.println(generateSignature());

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                return false;
            }
        });
    }

    public String generateNonce() {
        StringBuilder sb = new StringBuilder(32);
        String selectFrom = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 32; i++) {
            Random selector = new Random();

            sb.append(selectFrom.charAt(selector.nextInt(selectFrom.length())));
        }
        return sb.toString();
    }

    public String generateSignature() {
        //POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&oauth_callback%3Dhttp%3A%2f%2flocalhost%3A3000%26oauth_consumer_key%3DQYZDTsapvAl5rt6ycH8W526zI%26oauth_nonce%3DQLGJhMioMWbT5T6V6NHD8lfuskphbAKi%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1492782077%26oauth_version%3D1.0
        //oauth_callback%3Dhttp%3A%2f%2flocalhost%3A3000%26oauth_consumer_key%3DQYZDTsapvAl5rt6ycH8W526zI%26oauth_nonce%3DQLGJhMioMWbT5T6V6NHD8lfuskphbAKi%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1492782077%26oauth_version%3D1.0

        //String baseString = "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
        String baseString = "POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&oauth_callback%3Dhttp%3A%2f%2flocalhost%3A3000%26oauth_consumer_key%3DQYZDTsapvAl5rt6ycH8W526zI%26oauth_nonce%3DQLGJhMioMWbT5T6V6NHD8lfuskphbAKi%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%1492787567%26oauth_version%3D1.0";
        String keyString = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
        byte[] byteBase = baseString.getBytes();

        try {
            String type = "HmacSHA1";
            SecretKeySpec secret = new SecretKeySpec(keyString.getBytes(), type);
            Mac mac = Mac.getInstance(type);
            mac.init(secret);

            byte[] byteToken = mac.doFinal(byteBase);

            return Base64.encodeToString(byteToken, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

}
