package com.example.clock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class PlayAlarmAty extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	//mp = MediaPlayer.create(this, R.raw.music);
    	mp.start();
    }
    
    private MediaPlayer mp;
    
    protected void onPause(){
    	super.onPause();
    	finish();
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	mp.stop();
    	mp.release();
    }
}
