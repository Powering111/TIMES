package com.payrespect.times;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{
    private Button timeCheckbtn;
    private TextView timetxt, clicktxt, sinceLasttxt;
    private timing rn;
    private int clicked;
    private Time checked,last,minInterval,maxInterval;
    private ArrayList<String> TIME_LIST;
    private ArrayAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        timeCheckbtn=findViewById(R.id.timecheck);
        Button timeResetbtn = findViewById(R.id.timeinit);
        Button clickbtn = findViewById(R.id.BigButton);
        timetxt=findViewById(R.id.timetext);
        clicktxt=findViewById(R.id.clicked);
        sinceLasttxt=findViewById(R.id.sincelast);

        TIME_LIST=new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,TIME_LIST);
        listView = findViewById(R.id.timelist);
        listView.setAdapter(adapter);
        //TODO checked,last,minInterval,maxInterval init
        checked= new Time();
        last=new Time();
        minInterval=new Time();
        maxInterval=new Time();

        Handler handler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                //Every Time
                if (msg.what == 1) {
                    //long ct = msg.arg1;
                    //String s = ct / 60000 + ":" + String.format("%02d",ct / 1000 % 60) + ":" + String.format("%02d",ct / 10 % 100);
                    checked.setValue(msg.arg1);
                    timetxt.setText(checked.getTime());
                }return true;

            }
        });
        timeCheckbtn.setOnClickListener(this);
        timeResetbtn.setOnClickListener(this);
        clickbtn.setOnTouchListener(this);
        rn= new timing(handler);
        Thread th = new Thread(rn);
        th.start();
        clicked=0;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.timecheck: // Start/Stop Button Pressed
                if(rn.getTimerStarted()){
                    rn.setTimerStarted(false);
                    timeCheckbtn.setText(getText(R.string.startChecking));
                }else{
                    rn.setTimerStarted(true);
                    timeCheckbtn.setText(getText(R.string.stopChecking));
                }
                break;
            case R.id.timeinit: // Initialize Button Pressed
                rn.init();
                clicked=0;
                timetxt.setText(getText(R.string.zeroTime).toString());
                clicktxt.setText(String.valueOf(clicked));
                sinceLasttxt.setText(getText(R.string.zeroTime).toString());
                TIME_LIST.clear();
                adapter.notifyDataSetChanged();
                if(rn.getTimerStarted()){
                    timeCheckbtn.performClick();
                }
                break;

        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch(v.getId()){
            case R.id.BigButton: //Big Button Pressed
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(rn.getTimerStarted()) {
                        clicktxt.setText(String.valueOf(++clicked));
                        sinceLasttxt.setText(timetxt.getText().toString());

                        TIME_LIST.add(checked.getTime());
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
        return false;
    }
}

class Time{
    private int value;
    private String time;
    Time(){
        value=0;
        time="00:00:00";
    }
    public Time(int val) {
        setValue(val);
    }
    public void setValue(int val){
        value = val;
        time = val / 60000 + ":" + String.format("%02d", val / 1000 % 60) + ":" + String.format("%02d", val / 10 % 100);
    }
    public int getValue(){
        return value;
    }
    public String getTime(){
        return time;
    }
    public String toString(){
        return time;
    }
}