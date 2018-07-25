package com.g9fabflix.fabflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class movies extends AppCompatActivity{


    JSONArray jsonArray;
    ArrayList<String> list = new ArrayList<>();
    public boolean lock =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        final ArrayList<String> list = new ArrayList<>();

        final String title = getIntent().getExtras().getString("title");

        final String moviename;


        moviename = title;
        String url = "http://ec2-54-183-12-45.us-west-1.compute.amazonaws.com:8080/project2/MovieSearch?title="+moviename+"&year=&director=&actor=&genre=&type=search";
        Log.d("URL: ",url);



        JsonArrayRequest sr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.i("Response", response.toString());
                        jsonArray = response;

                        try {


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = new JSONObject();
                                json = response.getJSONObject(i);
                                String title = json.getString("m_title");
                                String year = json.getString("m_year");
                                String director = json.getString("m_director");
                                String genres = json.getString("m_genres");
                                String actors = json.getString("m_actors");
                                list.add("Title:  "+title+"\n"+"Year:  "+year+"\n"+"Director:  " + director+"\n"+"Genres:  "+genres+"\n"+"Stars:"+"\n"+actors);
                            }


                            displaydata(list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }},


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response", "movie ERROR");
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(sr);


    }

    private void displaydata(ArrayList<String> list) {
        final ArrayAdapter<String> Alist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ListView lv = (ListView) findViewById(R.id.ListView);
        if (lv != null) {
            lv.setAdapter(Alist);
        } else {
            Toast.makeText(getApplicationContext(), "Some problem", Toast.LENGTH_LONG).show();
            return;
        }
    }





}
