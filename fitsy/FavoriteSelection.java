package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FavoriteSelection extends AppCompatActivity {
    Button favorite;
    ImageView foodImage;
    TextView label, cal, fat, carbs, category;
    private int ii;
    ArrayList<String> favorites = new ArrayList<String>();
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_selection);
        Objects.requireNonNull(getSupportActionBar()).hide();

        favorite = findViewById(R.id.favorite_id);
        foodImage = findViewById(R.id.food_img_id);

        mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        label = findViewById(R.id.foodLabel_id);
        cal = findViewById(R.id.calories_id);
        fat = findViewById(R.id.fat_id);
        carbs = findViewById(R.id.carbs_id);
        category = findViewById(R.id.category_id);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {
                    String s = (task.getResult().child("fav_num").getValue().toString());
                    try{
                        ii = Integer.parseInt(s);
                    }
                    catch (NumberFormatException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

    new Thread(new Runnable() {
        @Override
        public void run() {
            Bundle bundle = getIntent().getExtras();
            String imageurl = bundle.getString("food_url");
            Log.d("INFO_URL_", imageurl);

            URL url = null;
            try {
                url = new URL(imageurl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                foodImage.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("favorites").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {

                }
            }
        });


        new JsonReader().execute();
        FirebaseDatabaseModifier firebaseDatabaseModifier = new FirebaseDatabaseModifier();

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String ingr = bundle.getString("food_id");
                mDatabase.child("users").child(mAuth.getUid()).child("favorites").child(String.valueOf(ii)).setValue(ingr);
                ii++;
                firebaseDatabaseModifier.updateFav(mAuth.getCurrentUser().getUid(),ii);
            }
        });



    }

    private class JsonReader extends AsyncTask<URL, Void, Void> {
        protected Void doInBackground(URL... urls) {
            String line = "";

            JSONObject object = new JSONObject();
            StringBuilder sb = new StringBuilder();
            //log =  "-74.535278";
            //lat = "40.382118";
            //ingr = "food_bsp9q1taipgkwkbih6nu6bqgy1op";
            URL url = null;
            Bundle bundle = getIntent().getExtras();
            String ingr = bundle.getString("food_id");
            try {
                url = new URL("https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&ingr=" + ingr + "&app_id=e1d10299&app_key=cb4f5562503a7fcd0f98f26cd72c9309&measure=#Measure_gram");
                Log.d("TAG_INFOurl", url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection con = null;
            try {
                assert url != null;
                con = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert con != null;
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                object = new JSONObject(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Log.d("TAG_INFO",object.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getString("foodId"));
            Log.d("TAG_INFO", object.toString());

            JSONObject finalObject = object;
            runOnUiThread(new Runnable() {

                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    try {
                        label.setText(finalObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getString("label"));
                        cal.setText(finalObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getJSONObject("nutrients").getString("ENERC_KCAL")+" Cal");
                        fat.setText("Fat: "+finalObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getJSONObject("nutrients").getString("FAT")+"g");
                        carbs.setText("Carbs: "+finalObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getJSONObject("nutrients").getString("CHOCDF")+"g");
                        category.setText("Category: "+finalObject.getJSONArray("hints").getJSONObject(0).getJSONObject("food").getString("category"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            //name[i] = (object.getJSONArray("list").getJSONObject(i).getString("name"));
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }



}