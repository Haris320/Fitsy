package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class dashboard extends AppCompatActivity {
    RelativeLayout run_layout, bike_layout, lift_layout, food_layout, account_layout, signout;
    ImageView pfp;
    TextView welcome;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private final User userData = new User();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_dashboard);

        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = mAuth.getCurrentUser();

        signout = findViewById(R.id.id_signout);
        run_layout = findViewById(R.id.id_run);
        bike_layout = findViewById(R.id.id_bike);
        lift_layout = findViewById(R.id.id_lift);
        food_layout = findViewById(R.id.id_food);
        account_layout = findViewById(R.id.id_account);

        welcome = findViewById(R.id.text_view89);
        pfp = findViewById(R.id.user_pfp);

        FirebaseDatabaseModifier data = new FirebaseDatabaseModifier();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {

                    userData.setSex(task.getResult().child("sex").getValue().toString());
                    userData.setName(task.getResult().child("name").getValue().toString());
                    userData.setHeight(task.getResult().child("height").getValue().toString());
                    userData.setWeight(task.getResult().child("weight").getValue().toString());
                    userData.setAge(task.getResult().child("age").getValue().toString());
                    welcome.setText("Welcome, "+userData.getName());

                    if(userData.getSex().toLowerCase().equals("male"))
                        pfp.setImageResource(R.drawable.man);
                    else if(userData.getSex().toLowerCase().equals("female"))
                        pfp.setImageResource(R.drawable.woman);

                }
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginActivity(v);
            }
        });

        run_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunActivity(v);
            }
        });

        bike_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwimActivity(v);
            }
        });

        lift_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiftActivity(v);
            }
        });

        food_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NutritionActivity(v);
            }
        });

        account_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountActivity(v);
            }
        });

    }

    public void LoginActivity(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void RunActivity(View view){
        Intent intent = new Intent(this,run.class);
        startActivity(intent);
    }

    public void SwimActivity(View view){
        Intent intent = new Intent(this,Swim_excersize.class);
        startActivity(intent);
    }

    public void LiftActivity(View view){
        Intent intent = new Intent(this,lift.class);
        startActivity(intent);
    }

    public void NutritionActivity(View view){
        Intent intent = new Intent(this,nutrition.class);
        startActivity(intent);
    }

    public void AccountActivity(View view){
        Intent intent = new Intent(this,account.class);
        startActivity(intent);
    }
}