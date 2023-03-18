package com.example.jayjan_feud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class joinActivity extends AppCompatActivity {

    public String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_join);
    }

    public void post_data(String name, String room){
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        String post_url = "https://gabaafeud.mysticjayce.repl.co/user/" + user_id;

        JSONObject postData = new JSONObject();
        try{
            postData.put("name", name);
            postData.put("room", room);
        } catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST, post_url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(joinActivity.this, "Response" + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(joinActivity.this, "Error has occurred", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest2);
    }

    public void join(View view){
        EditText name_btn = (EditText) findViewById(R.id.name);
        EditText room_btn = (EditText) findViewById(R.id.room_code);
        //getUserId();
        //Checking if the room or name is empty
        if (name_btn.getText().toString().trim().equals("") || room_btn.getText().toString().trim().equals("")){
            Toast.makeText(this.getBaseContext(), "Invalid Name or Room ID", Toast.LENGTH_SHORT).show();
        }
        else{
            RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
            String base_url = "https://gabaafeud.mysticjayce.repl.co/userid";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(base_url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Toast.makeText(joinActivity.this, "Response" + response.getString("id"), Toast.LENGTH_SHORT).show();
                        user_id = response.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    post_data(name_btn.getText().toString(), room_btn.getText().toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(joinActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonObjectRequest);
        }


    }

}