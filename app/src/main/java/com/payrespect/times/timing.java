package com.payrespect.times;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
public class timing implements Runnable{
    private long nowTime,deltaTime,checkedTime;

    private Handler handler;
    private boolean stopped,timerStart;

    public timing(Handler h){
        handler=h;
        nowTime=System.currentTimeMillis();
        stopped=false;
        timerStart=false;
    }
    public boolean getTimerStarted(){
//        Log.d("Debugging[SubThread]","in Sub Thread, returning "+timerStart);
        return timerStart;
    }
    public void setTimerStarted(boolean b){
        timerStart=b;
//        Log.d("Debugging[SubThread]","in Sub Thread, in timerStart "+b+timerStart);
        nowTime=System.currentTimeMillis();
    }
    public void init(){
//        Log.d("Debugging[SubThread]","in Sub Thread, in init()");
        checkedTime=0;
    }
    @Override
    public void run(){
//        Log.d("Debugging[SubThread]","in run()");
        while(!stopped){
//            Log.d("Debugging[SubThread]","in run() in while(!stopped)");
            if(timerStart){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Log.d("Debugging[SubThread]","in run() in if(timerStart)");
                try {

                    deltaTime=System.currentTimeMillis()-nowTime;
                    nowTime=deltaTime+nowTime;
//                    Log.d("Debugging[SubThread]", "in Sub Thread, in if(timerStart).");
                    checkedTime += deltaTime;
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.arg1 = (int) checkedTime;
                    handler.sendMessage(msg);
                }catch(Exception e){
                    e.printStackTrace();
                    stop();
                }
            }
        }
    }
    public void stop(){
//        Log.d("Debugging[SubThread]",Thread.currentThread().getName()+"종료됨");
        stopped=true;

    }
}
