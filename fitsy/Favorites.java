package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class Favorites extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    ListView listView;
    public static int favnumber;
    SearchStuff searchStuff;
    ArrayList<SearchStuff> arrayList = new ArrayList<SearchStuff>();
    private ArrayList<String> favorites = new ArrayList<String>();
    private String[] img_url;
    private String[] label;
    private String[] food_id;


    public Favorites() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listview_2_id);

        int fave = (int) mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("fav_num").get().getResult().getValue();

        System.out.println(fave);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("favorites").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase+++", "Error getting data", task.getException());
                }
                else {

                    for(int i=0; i<favnumber;i++) {
                        favorites.add(task.getResult().child(String.valueOf(i)).getValue().toString());
                        Log.d("CHILD_INFO",favorites.get(i));
                    }
                }
            }
        });

        new JsonReader().execute();

        Log.d("TAG_1", "favnum  "+favnumber);
        img_url = new String[favnumber];
        label = new String[favnumber];
        food_id = new String[favnumber];

    }



    private class JsonReader extends AsyncTask<URL, Void, Void> {
        protected Void doInBackground(URL... urls) {
            String line = "";

            JSONObject object = new JSONObject();
            StringBuilder sb = new StringBuilder();

            //log =  "-74.535278";
            //lat = "40.382118";
            //ingr = "food_bsp9q1taipgkwkbih6nu6bqgy1op";



            Log.d("TAG_2", "fav size  "+favorites.size());
            Log.d("TAG_2", "favnum  "+favnumber);
            for(int i=0;i<favnumber;i++) {
                Log.d("TAG_", "forloop");
                URL url = null;
                try {
                    url = new URL("https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&ingr=" + favorites.get(i) + "&app_id=e1d10299&app_key=cb4f5562503a7fcd0f98f26cd72c9309&measure=#Measure_gram");
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
                try {
                    if (object.getJSONArray("hints").getJSONObject(i).getJSONObject("food").getString("image") == null)
                        img_url[i] = "https://cdn.iconscout.com/icon/premium/png-256-thumb/no-image-2840213-2359555.png";
                    else
                        img_url[i] = (object.getJSONArray("hints").getJSONObject(i).getJSONObject("food").getString("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    label[i] = (object.getJSONArray("hints").getJSONObject(i).getJSONObject("food").getString("label"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    food_id[i] = (object.getJSONArray("hints").getJSONObject(i).getJSONObject("food").getString("foodId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            //Log.d("TAG_INFO_", label[0]);


            //name[i] = (object.getJSONArray("list").getJSONObject(i).getString("name"));
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < favnumber; i++) {
                if(img_url[i]==null)
                    img_url[i]="https://cdn.iconscout.com/icon/premium/png-256-thumb/no-image-2840213-2359555.png";
                searchStuff = new SearchStuff(img_url[i], label[i],food_id[i]);
                arrayList.add(searchStuff);
            }
            //Log.d("Array+++", arrayList.get(0).getImage_url());
        }
    }




}