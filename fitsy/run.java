package com.example.fitsy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class run extends AppCompatActivity {
    Button begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Objects.requireNonNull(getSupportActionBar()).hide();
        begin = findViewById(R.id.begin_run);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runWorkout(v);
            }
        });
    }

    public void runWorkout(View view){
        Intent intent = new Intent(this,run_workout.class);
        startActivity(intent);
    }
}