package com.example.jayjan_feud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class createActivity extends AppCompatActivity {

    public String user_id;
    public String room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_create);
    }

    public void lobby_page(){
        Intent intent = new Intent(this, lobbyActivity.class);
        intent.putExtra("host", true);
        intent.putExtra("room", room_id);
        startActivity(intent);
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
                Toast.makeText(createActivity.this, "Response" + response, Toast.LENGTH_LONG).show();
                lobby_page();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(createActivity.this, "Error has occurred", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest2);
    }

    public void add_user(String name, String room){
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        String base_url = "https://gabaafeud.mysticjayce.repl.co/userid";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(base_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Toast.makeText(createActivity.this, "Response" + response.getString("id"), Toast.LENGTH_SHORT).show();
                    user_id = response.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                post_data(name, room);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(createActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void create(View view){
        EditText room_btn = (EditText) findViewById(R.id.room_btn);
        EditText name_btn = (EditText) findViewById(R.id.name_btn);

        room_id = room_btn.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());

        JSONObject jsonObject = new JSONObject();
        String base_url = "https://gabaafeud.mysticjayce.repl.co/room/" + room_btn.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, base_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(createActivity.this, "Response" + response, Toast.LENGTH_LONG).show();
                add_user(name_btn.getText().toString(), room_btn.getText().toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(createActivity.this, "Error has occurred", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);


    }

}