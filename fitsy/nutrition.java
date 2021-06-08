package com.example.fitsy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class nutrition extends AppCompatActivity {
    EditText search_term, upc_code;
    Button search_btn, fav_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        Objects.requireNonNull(getSupportActionBar()).hide();

        search_btn = findViewById(R.id.search_btn_id);
        fav_btn = findViewById(R.id.favorite_btn_id);
        search_term = findViewById(R.id.search_term_id);
        upc_code = findViewById(R.id.upc_code_id);

        upc_code.setText("847473001086");

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nutrition.this, SearchRes.class);
                intent.putExtra("search_term", search_term.getText().toString());
                intent.putExtra("upc_code", upc_code.getText().toString());
                intent.putExtra("search_num", 20);
                startActivity(intent);
            }
        });

        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nutrition.this, SearchRes.class);
                intent.putExtra("search_term", search_term.getText().toString());
                intent.putExtra("upc_code", upc_code.getText().toString());
                intent.putExtra("search_num", 5);
                startActivity(intent);
            }
        });

    }

}