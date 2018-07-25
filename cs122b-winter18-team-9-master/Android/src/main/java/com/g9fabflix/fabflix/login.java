package com.g9fabflix.fabflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity{

    Button bLogin;
    EditText etUsername, etPassword;
    TextView bleh;
    private int counter = 5;
    JSONObject json;
    int length = Toast.LENGTH_LONG;
    Toast toast;
    boolean auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toast = Toast.makeText(this, "Fabflix Mobile",Toast.LENGTH_SHORT);
        toast.show();
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        assert bLogin!=null;
        bleh = (TextView) findViewById(R.id.tvinfo);

        bleh.setText("Number of attempts remaining: 5");

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = etUsername.getText().toString();
                final String pass = etPassword.getText().toString();

                if (user.length() == 0 || pass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter UserName / Password", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = "http://ec2-54-183-12-45.us-west-1.compute.amazonaws.com:8080/project2/Login?username="+user+"&password="+pass;


                JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url,json,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Log.i("Response", response.toString());
                                String info = null;


                                try {
                                    info = response.getString("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (info.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplication(), "Welcome to our app", Toast.LENGTH_LONG).show();
                                    Intent MovieSearch = new Intent(login.this, AndroidSearch.class);//SearchPage
                                    startActivity(MovieSearch);

                                }
                                else {
                                    Toast.makeText(getApplication(), "Invalid login.", Toast.LENGTH_LONG).show();
                                    counter--;

                                    bleh.setText("Number of attempts remaining: " + String.valueOf(counter));

                                    if(counter == 0){
                                        bLogin.setEnabled(false);
                                    }

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Response", "ERROR");
                                Toast.makeText(getApplication(), "Unable to connect to server", Toast.LENGTH_LONG).show();
                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(sr);


            }
        });
    }



    /*private void validate(String userName, String passWord){
        if((userName.equals("Admin")) &&(passWord.equals("1234"))){
            Intent intent = new Intent(login.this, test.class);
            startActivity(intent);
        }
        else{
            counter--;

            info.setText("Number of attempts remaining: " + String.valueOf(counter));

            if(counter == 0){
                bLogin.setEnabled(false);
            }
        }
    }*/



}
