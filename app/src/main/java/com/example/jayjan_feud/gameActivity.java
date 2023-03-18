package com.example.jayjan_feud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class gameActivity extends AppCompatActivity {

    public CountDownTimer countDownTimer;
    public TextView timer;
    public TextView title_bar;
    public TextView quest_box;
    public EditText answer_box;
    public TextView answer_text;
    public String room_id;
    //public long timeLeftInMilliseconds = 960000;
    public long timeLeftInMilliseconds = 60000;
    public int count = 0;
    public String[] quest_list;
    public String[] answers;
    public int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        room_id = intent.getStringExtra("room");
        timer = (TextView) findViewById(R.id.timer);
        quest_list = new String[8];
        answers = new String[8];
        quest_box = (TextView) findViewById(R.id.quest_box);
        answer_box = (EditText) findViewById(R.id.answer_txt);
        title_bar = (TextView) findViewById(R.id.title_bar);
        answer_text = (TextView) findViewById(R.id.answer_box);

        load_quests();


    }

    public void beautify(String response){
        response = response.replaceAll("\"", "");
        response = response.substring(1,response.length()-1);
        String[] response_list = response.split(",");
        for (int i = 1; i<response_list.length - 1; i++){
            String quest = response_list[i].split(":")[1];
            quest_list[i-1] = quest;
        }
        print_quest();
    }

    public String soup(String response){
        response = response.replaceAll("\"", "");
        response = response.replaceAll("null", "");
        response = response.substring(1, response.length()-2);
        String[] response_list = response.split(",");
        String question = response_list[1].split(":")[1];
        for (int i = 0; i < 8; i++) {
            String ans = response_list[i+2].split(":")[1];
            if (!ans.trim().equals("null")){
                answers[i] = ans;
            }

        }
        Toast.makeText(gameActivity.this, question, Toast.LENGTH_LONG).show();
        return question;
    }

    public void print_quest(){
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        //String post_url = "https://gabaafeud.mysticjayce.repl.co/user/1";
        //TextView quest_box = (TextView) findViewById(R.id.quest_box);
        String post_url = "https://gabaafeud.mysticjayce.repl.co/quest/" + quest_list[count].trim();
        JSONObject postData = new JSONObject();
        Toast.makeText(gameActivity.this, "quest_lidt" + quest_list[count], Toast.LENGTH_SHORT).show();
        try{
            postData.put("room", room_id);
        } catch(JSONException e){
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, post_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s = soup(response);
                quest_box.setText(s);
                answer_box.setVisibility(View.VISIBLE);
                timeLeftInMilliseconds = 60000;
                start_quest_timer(0);
                //Toast.makeText(gameActivity.this, "Response" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(gameActivity.this, "Error has occured", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void load_quests(){
        RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
        //String post_url = "https://gabaafeud.mysticjayce.repl.co/user/1";
        String post_url = "https://gabaafeud.mysticjayce.repl.co/room/" + room_id;
        JSONObject postData = new JSONObject();
        Toast.makeText(gameActivity.this, room_id, Toast.LENGTH_SHORT).show();
        try{
            postData.put("room", room_id);
        } catch(JSONException e){
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, post_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(gameActivity.this, "Response" + response, Toast.LENGTH_SHORT).show();
                beautify(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(gameActivity.this, "Error has occured", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    //Question -> 0
    //Answer -> 1

    public void display_answer(){
        timeLeftInMilliseconds = 60000;
        title_bar.setText("CHECK YOUR ANSWERS!");
        quest_box.setText(answer_box.getText().toString() + "\n" + "Check Your Answer With The Answer List Below and Enter Your Score" );
        EditText score_box = (EditText) findViewById(R.id.score_box);
        score_box.setText("");
        score_box.setVisibility(View.VISIBLE);
        answer_box.setVisibility(View.INVISIBLE);
        String ans = "";
        for (int i = 0; i< answers.length; i++){
            ans += answers[i] + "\n";
        }
        answer_text.setText(ans);
        start_quest_timer(1);
    }



    public void start_quest_timer(int choice){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                update_timer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(gameActivity.this,"TIME UP! ",Toast.LENGTH_SHORT ).show();
                if(choice == 0 ){
                    display_answer();
                }
                if(choice == 1){
                    if (count < 7){
                        count ++;
                        answer_text.setText("");
                        answer_box.setText("");
                        title_bar.setText("FEUD TIME");
                        EditText score_box = (EditText) findViewById(R.id.score_box);
                        String score_entered = score_box.getText().toString();
                        if (score_entered.trim().equals("")){
                            score_entered = "0";
                        }
                        score += Integer.parseInt(score_entered);
                        score_box.setVisibility(View.INVISIBLE);
                        TextView score_handler = (TextView) findViewById(R.id.score_holder);
                        score_handler.setText(Integer.toString(score));
                        load_quests();
                    }

                }

            }
        }.start();
    }

    public void update_timer(){
        int minutes = (int) timeLeftInMilliseconds/ 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10){
            timeLeftText += "0";
        }
        timeLeftText += seconds;

        timer.setText(timeLeftText);
    }

}