package com.example.fitsy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Objects;

public class SearchRes extends AppCompatActivity {
    String ingr = "";
    int stat = 0;
    private int term;
    ListView listView;
    SearchStuff searchStuff;
    ArrayList<SearchStuff> arrayList = new ArrayList<SearchStuff>();
    String[] img_url = new String[20];
    String[] label = new String[20];
    String[] food_id = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res);
        Objects.requireNonNull(getSupportActionBar()).hide();
        new JsonReader().execute();
        Bundle bundle = getIntent().getExtras();

        String SearchTerm = bundle.getString("search_term");
        String UPC = bundle.getString("upc_code");
         term = bundle.getInt("search_num");


        listView = findViewById(R.id.id_listview);

        if (SearchTerm.equals("")){
            ingr = UPC;
            stat = 2;
        }
        else {
            ingr = SearchTerm;
            stat = 1;
        }

        SearchStuff searchStuff1 = new SearchStuff("https://cdn.iconscout.com/icon/premium/png-256-thumb/no-image-2840213-2359555.png","","123123");
        arrayList.add(searchStuff1);

        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.adapter_listview, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = position;
                Log.d("Array*", String.valueOf(arrayList.get(position).getId()));
                Intent intent = new Intent(SearchRes.this, FavoriteSelection.class);
                intent.putExtra("food_id", arrayList.get(position).getId());
                intent.putExtra("food_url", arrayList.get(position).getImage_url());
                startActivity(intent);

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
            try {
                if(stat==1)
                    url = new URL("https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&ingr=" + ingr + "&app_id=e1d10299&app_key=cb4f5562503a7fcd0f98f26cd72c9309&measure=#Measure_gram");
                else
                    url = new URL("https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&upc=" + ingr + "&app_id=e1d10299&app_key=cb4f5562503a7fcd0f98f26cd72c9309&measure=#Measure_gram");
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


            for (int i = 0; i < 20; i++) {
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

                try {
                    if (object.getJSONArray("hints").getJSONObject(i).getJSONObject("food").getString("image") == null)
                        img_url[i] = "https://cdn.iconscout.com/icon/premium/png-256-thumb/no-image-2840213-2359555.png";
                    else
                        img_url[i] = (object.getJSONArray("hints").getJSONObject(i).getJSONObject("food").getString("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.d("Arrays", Arrays.toString(img_url));
            Log.d("Arrays+", Arrays.toString(label));

            //name[i] = (object.getJSONArray("list").getJSONObject(i).getString("name"));
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int b;
            if(stat==2)
                b=1;
            else
                b=term;
            for (int i = 0; i < b; i++) {
                if(img_url[i]==null)
                    img_url[i]="https://cdn.iconscout.com/icon/premium/png-256-thumb/no-image-2840213-2359555.png";
                searchStuff = new SearchStuff(img_url[i], label[i],food_id[i]);
                arrayList.add(searchStuff);
            }
            Log.d("Array+++", arrayList.get(0).getImage_url());
        }
    }

    public class ListViewAdapter extends ArrayAdapter<SearchStuff> {
        Context mainContext;
        int xml;
        List<SearchStuff> list;

        public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<SearchStuff> objects) {
            super(context, resource, objects);
            mainContext = context;
            xml = resource;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mainContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View adapterLayout = inflater.inflate(xml, null);
            TextView title = adapterLayout.findViewById(R.id.id_adapter_textview);
            ImageView imageView = adapterLayout.findViewById(R.id.id_adapter_image_view);

            SearchStuff searchList = list.get(position);
            title.setText(searchList.getTitle());

            new ImageDownloaderTask(imageView).execute(searchList.getImage_url());

            return adapterLayout;

        }

        @SuppressLint("StaticFieldLeak")
        class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

            private final WeakReference<ImageView> imageViewReference;

            public ImageDownloaderTask(ImageView imageView) {
                imageViewReference = new WeakReference<ImageView>(imageView);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                return downloadBitmap(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (isCancelled()) {
                    bitmap = null;
                }

                if (imageViewReference != null) {
                    ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Drawable placeholder = null;
                            imageView.setImageDrawable(placeholder);
                        }
                    }
                }
            }

            private Bitmap downloadBitmap(String url) {
                HttpURLConnection urlConnection = null;
                try {
                    URL uri = new URL(url);
                    urlConnection = (HttpURLConnection) uri.openConnection();

                    final int responseCode = urlConnection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        return null;
                    }

                    InputStream inputStream = urlConnection.getInputStream();
                    if (inputStream != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    }
                } catch (Exception e) {
                    urlConnection.disconnect();
                    Log.w("ImageDownloader", "Error " + url);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return null;
            }

        }


    }
}