package com.g9fabflix.fabflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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




public class AndroidSearch extends AppCompatActivity {

    private EditText search;
    private Button SearchButton;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_search);

        search = (EditText) findViewById(R.id.search);
        SearchButton = (Button) findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String moviename=null;
                final String title = search.getText().toString();
                if (title.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a movie title", Toast.LENGTH_LONG).show();
                    return;
                }


                moviename = title;

                String url = "http://ec2-54-183-12-45.us-west-1.compute.amazonaws.com:8080/project2/MovieSearch?title="+moviename+"&year=&director=&actor=&genre=&type=search";

                JsonArrayRequest sr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {



                @Override
                            public void onResponse(JSONArray response) {

                                jsonArray = response;


                                if (jsonArray!=null) {


                                    Intent i = new Intent(AndroidSearch.this,movies.class);
                                    i.putExtra("title",title);

                                    startActivity(i);

                                }


                                else{

                                    Toast.makeText(getApplication(), "Does not exist", Toast.LENGTH_LONG).show();
                                    return;

                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Response", "search ERROR" + jsonArray.toString());
                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(sr);
            }
        });
    }
}
