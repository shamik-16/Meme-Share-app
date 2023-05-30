package com.example.mymemes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView imgMeme;
    String MemeUrl;
    ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgMeme = findViewById(R.id.imgView);
        pgBar = findViewById(R.id.pgBar);

        LoadMeme();


    }

    private void LoadMeme() {

        pgBar.setVisibility(View.VISIBLE);

        String url = "https://meme-api.com/gimme";

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            MemeUrl = object.getString("url");
                            Glide.with(MainActivity.this).load(MemeUrl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    pgBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this,"Image can't load",Toast.LENGTH_LONG).show();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    pgBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(imgMeme);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this,"Can't Load meme",Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void NextMeme(View view) {
        LoadMeme();
    }

    public void ShareMeme(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey friend, checkout this cool meme : "+MemeUrl);
        Intent chooser = Intent.createChooser(intent,"Share this meme using...");
        startActivity(chooser);

    }
}