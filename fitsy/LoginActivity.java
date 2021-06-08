package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "INFO_";
    Boolean Loggedin = true;
    Button login;
    EditText email,password;
    TextView create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        email = findViewById(R.id.id_email);
        password = findViewById(R.id.id_password);
        create = findViewById(R.id.text_view89);
        mAuth = FirebaseAuth.getInstance();
        onStart();
        //FirebaseAuth.getInstance().signOut();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    signIn(email.getText().toString(),password.getText().toString());
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser != null){
                        WelcomeActivity(v);
                    }
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(v);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
            Intent intent = new Intent(this,WelcomeActivity.class);
            Intent first = new Intent(this,dashboard.class);
            //startActivity(intent);
            startActivity(first);
        }
    }//Checks if user is already logged in



    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Loggedin = true;
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), dashboard.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Loggedin = false;
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }



    private void updateUI(FirebaseUser user) {

    }

    private void reload() { }

    public void createAccount(View view){
        Intent intent = new Intent(this,CreateAccount.class);
        startActivity(intent);
    }

    public void firsttime(View view){
        Intent intent = new Intent(this,FirstTimeSetup.class);
        startActivity(intent);
    }

    public void WelcomeActivity(View view){
        Intent intent = new Intent(this,dashboard.class);
        //intent.putExtra("account", (Parcelable) mAuth);
        startActivity(intent);
    }
}
