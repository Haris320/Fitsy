package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class account extends AppCompatActivity {
    ImageView pfp;
    TextView name_txt, height_txt, weight_txt, age_txt, sex_txt;
    Button update;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name_txt = findViewById(R.id.name_pfp);
        height_txt = findViewById(R.id.height_pfp);
        weight_txt = findViewById(R.id.weight_pfp);
        age_txt = findViewById(R.id.age_pfp);
        sex_txt = findViewById(R.id.sex_pfp);

        pfp = findViewById(R.id.user_pfp_id);

        update = findViewById(R.id.update_pfp);


        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {

                    sex_txt.setText("Sex: "+task.getResult().child("sex").getValue().toString());
                    name_txt.setText(task.getResult().child("name").getValue().toString());
                    height_txt.setText("Height: "+task.getResult().child("height").getValue().toString()+"in");
                    weight_txt.setText("Weight: "+task.getResult().child("weight").getValue().toString()+"lbs");
                    age_txt.setText("Age: "+task.getResult().child("age").getValue().toString());

                    if(task.getResult().child("sex").getValue().toString().equals("male"))
                        pfp.setImageResource(R.drawable.man);
                    else if(task.getResult().child("sex").getValue().toString().equals("female"))
                        pfp.setImageResource(R.drawable.woman);

                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateActivity(v);
            }
        });

    }

    public void UpdateActivity(View view){
        Intent intent = new Intent(this,FirstTimeSetup.class);
        startActivity(intent);
    }
}
