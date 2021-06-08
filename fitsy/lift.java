package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class lift extends AppCompatActivity {

    EditText max_bicepcurl, max_bench, max_deadlift;
    Button updateMax;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_lift);
        mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        updateMax = findViewById(R.id.updatelift_id);
        max_bench = findViewById(R.id.max_bench_id);
        max_deadlift = findViewById(R.id.max_deadlift_id);
        max_bicepcurl = findViewById(R.id.max_bicep_curl_id);
        FirebaseDatabaseModifier firebaseDatabaseModifier = new FirebaseDatabaseModifier();
        User lift = new User();


        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {
                    max_bicepcurl.setHint("Max Bicep Curl (lbs) [Current " +task.getResult().child("m_bicepcurl").getValue().toString()+"]");
                    max_deadlift.setHint("Max Deadlift (lbs) [Current "+task.getResult().child("m_deadlift").getValue().toString()+"]");
                    max_bench.setHint("Max Bench (lbs) [Current "+task.getResult().child("m_bench").getValue().toString()+"]");
                }
            }
        });


        updateMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase+++", "Error getting data", task.getException());
                        }
                        else {

                            lift.setSex(task.getResult().child("sex").getValue().toString());
                            lift.setName(task.getResult().child("name").getValue().toString());
                            lift.setHeight(task.getResult().child("height").getValue().toString());
                            lift.setWeight(task.getResult().child("weight").getValue().toString());
                            lift.setAge(task.getResult().child("age").getValue().toString());
                            lift.setT_backstroke(task.getResult().child("t_backstroke").getValue().toString());
                            lift.setT_butterfly(task.getResult().child("t_butterfly").getValue().toString());
                            lift.setT_freestyle(task.getResult().child("t_freestyle").getValue().toString());
                            //lift.setFav_num((Integer) task.getResult().child("fav_num").getValue());
                            lift.setM_bench(max_bench.getText().toString());
                            lift.setM_bicepcurl(max_bicepcurl.getText().toString());
                            lift.setM_deadlift(max_deadlift.getText().toString());


                            firebaseDatabaseModifier.writeNewUser(mAuth.getCurrentUser().getUid(),String.valueOf(lift.getAge()),
                                    String.valueOf(lift.getWeight()), String.valueOf(lift.getHeight()), lift.getSex(),
                                    lift.getName(),lift.getM_bicepcurl(),lift.getM_bench(),lift.getM_deadlift(),
                            lift.getT_freestyle(),lift.getT_butterfly(),lift.getT_backstroke(),lift.getFav_num());

                        }
                    }
                });
            }
        });

    }


}