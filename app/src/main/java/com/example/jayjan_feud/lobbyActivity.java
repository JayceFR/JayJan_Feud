package com.example.jayjan_feud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class lobbyActivity extends AppCompatActivity {

    public boolean host = false;
    public boolean free = true;
    public String room_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        host = intent.getBooleanExtra("host", false);
        room_id = intent.getStringExtra("room");
        setContentView(R.layout.activity_lobby);

        if (host == true) {
            //Toast.makeText(lobbyActivity.this, "Ya we rock", Toast.LENGTH_SHORT).show();
            Button start_btn = (Button) findViewById(R.id.start_btn);
            start_btn.setVisibility(View.VISIBLE);
        }
        update();
    }

    public void refresh(View view){
        if (free){
            update();
        }
        else{
            Toast.makeText(lobbyActivity.this, "Don't make haste, Still Processing!", Toast.LENGTH_SHORT).show();
        }
    }

    public String beautify(String response){
        response = response.replaceAll("\"", "");
        response = response.substring(1,response.length()-1);
        String[] repsonse_list = response.split(",");
        String new_text = "";
        for (int i = 0; i< repsonse_list.length; i++){
            new_text += repsonse_list[i] + "\n";
        }
        return new_text;
    }

    public void start_game(){
        Intent intent = new Intent(this, gameActivity.class);
        intent.putExtra("room", room_id);
        startActivity(intent);
    }

    public void start(View view){
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        //String post_url = "https://gabaafeud.mysticjayce.repl.co/user/1";
        String post_url = "https://gabaafeud.mysticjayce.repl.co/room/" + room_id;
        JSONObject postData = new JSONObject();
        Toast.makeText(lobbyActivity.this, room_id, Toast.LENGTH_SHORT).show();
        try{
            postData.put("room", room_id);
        } catch(JSONException e){
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, post_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(lobbyActivity.this, "Started", Toast.LENGTH_SHORT).show();
                start_game();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(lobbyActivity.this, "Error has occured", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void update(){
        free = false;
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        //String post_url = "https://gabaafeud.mysticjayce.repl.co/user/1";
        String post_url = "https://gabaafeud.mysticjayce.repl.co/players/" + room_id;
        TextView player_list = (TextView) findViewById(R.id.players_list);
        JSONObject postData = new JSONObject();
        Toast.makeText(lobbyActivity.this, room_id, Toast.LENGTH_SHORT).show();
        try{
            postData.put("room", room_id);
        } catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST,post_url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(lobbyActivity.this, "Response" + response, Toast.LENGTH_SHORT).show();
                player_list.setText(beautify(response.toString()));
                free = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(lobbyActivity.this, "Error has occurred", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest2);
    }

}