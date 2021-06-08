package com.example.fitsy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class swim extends AppCompatActivity {
    EditText freestyle, butterfly, backstroke;
    Button update_time;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim);

        freestyle = findViewById(R.id.freestyle_time_id);
        butterfly = findViewById(R.id.butterfly_time_id);
        backstroke = findViewById(R.id.backstroke_time);
        update_time = findViewById(R.id.update_time_id);
        FirebaseDatabaseModifier firebaseDatabaseModifier = new FirebaseDatabaseModifier();
        User time = new User();

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {
                    freestyle.setHint("Freestyle (50yd) [Current " +task.getResult().child("t_freestyle").getValue().toString()+"s]");
                    butterfly.setHint("Butterfly (50yd) [Current "+task.getResult().child("t_butterfly").getValue().toString()+"s]");
                    backstroke.setHint("Backstroke (50yd) [Current "+task.getResult().child("t_backstroke").getValue().toString()+"s]");
                }
            }
        });


        update_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase+++", "Error getting data", task.getException());
                        }
                        else {

                            time.setSex(task.getResult().child("sex").getValue().toString());
                            time.setName(task.getResult().child("name").getValue().toString());
                            time.setHeight(task.getResult().child("height").getValue().toString());
                            time.setWeight(task.getResult().child("weight").getValue().toString());
                            time.setAge(task.getResult().child("age").getValue().toString());
                            time.setM_bench(task.getResult().child("m_bench").getValue().toString());
                            time.setM_deadlift(task.getResult().child("m_deadlift").getValue().toString());
                            time.setM_bicepcurl(task.getResult().child("m_bicepcurl").getValue().toString());
                            time.setFav_num((Integer) task.getResult().child("fav_num").getValue());
                            time.setT_freestyle(freestyle.getText().toString());
                            time.setT_butterfly(butterfly.getText().toString());
                            time.setT_backstroke(backstroke.getText().toString());

                            firebaseDatabaseModifier.writeNewUser(mAuth.getCurrentUser().getUid(),String.valueOf(time.getAge()),
                                    String.valueOf(time.getWeight()), String.valueOf(time.getHeight()), time.getSex(),
                                    time.getName(),time.getM_bicepcurl(),time.getM_bench(),time.getM_deadlift(),
                                    time.getT_freestyle(),time.getT_butterfly(),time.getT_backstroke(),time.getFav_num());

                        }
                    }
                });
            }
        });

    }
}