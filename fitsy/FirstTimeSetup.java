package com.example.fitsy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Objects;

public class FirstTimeSetup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText name_set;
    private NumberPicker height_selection, weight_selection, age_selection;
    private DatabaseReference mDatabase;

    Button confirm;
    private String[] pickerVals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_setup);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth=FirebaseAuth.getInstance();
        weight_selection = findViewById(R.id.weight_selection);
        height_selection = findViewById(R.id.height_selection);
        age_selection = findViewById(R.id.age_selection);

        name_set = findViewById(R.id.editTextTextPersonName);
        confirm = findViewById(R.id.confirm_button);

        FirebaseDatabaseModifier firebaseDatabaseModifier = new FirebaseDatabaseModifier();
        weight_selection.setMaxValue(300);
        weight_selection.setMinValue(120);

        height_selection.setMinValue(48);
        height_selection.setMaxValue(90);

        age_selection.setMinValue(13);
        age_selection.setMaxValue(100);

        Spinner spinner = (Spinner) findViewById(R.id.sex_spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(FirstTimeSetup.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sex_array));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabaseModifier.writeNewUser(mAuth.getCurrentUser().getUid(),String.valueOf(age_selection.getValue()),
                        String.valueOf(weight_selection.getValue()), String.valueOf(height_selection.getValue()),
                        spinner.getSelectedItem().toString().toLowerCase(),name_set.getText().toString()
                ,"","","","","","",0);
                WelcomeActivity(v);
            }
        });


    }

    public void WelcomeActivity(View view){
        Intent intent = new Intent(this,dashboard.class);
        startActivity(intent);
    }
}