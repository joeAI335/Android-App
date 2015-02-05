package com.example.clock;

import java.util.Timer;
import java.util.TimerTask;

import android.R.anim;
import android.content.Context;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StopWatchView extends LinearLayout {

	public StopWatchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public StopWatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public StopWatchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		tvHour = (TextView) findViewById(R.id.timeHour);
		tvHour.setText("0");
		tvMin = (TextView) findViewById(R.id.timeMin);
		tvMin.setText("0");
		tvSec = (TextView) findViewById(R.id.timeSec);
		tvSec.setText("0");
		tvMSec = (TextView) findViewById(R.id.timeMSec);
		tvMSec.setText("0");
		
		btnLap = (Button) findViewById(R.id.btnSWLap);
		btnPause = (Button) findViewById(R.id.btnSWPause);
		btnPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopTimer();
				
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
			}
		});
		btnReset = (Button) findViewById(R.id.btnSWReset);
		btnReset.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopTimer();
				tenMSec = 0;
				btnReset.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		btnResume = (Button) findViewById(R.id.btnSWResume);
		btnResume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startTimer();
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				
			}
		});
		btnStart = (Button) findViewById(R.id.btnSWStart);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startTimer();
				
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});
		btnLap.setVisibility(View.GONE);
		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);
		
		lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);
		adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
	    lvTimeList.setAdapter(adapter);
	    
	    showTimerTask = new TimerTask() {
			
			@Override
			public void run() {
				
			}
		};
		timer.schedule(showTimerTask, 200, 200);
	}
	
	private void startTimer () {
		if (timerTask == null) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					tenMSec++;
					
					
				}
			};
			timer.schedule(timerTask, 10, 10);
		}
	}
	
	private void stopTimer(){
		if (timerTask == null) {
			timerTask.cancel();
		}
	}
	private int tenMSec = 10;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private TimerTask showTimerTask = null;
	
	private TextView tvHour, tvMin, tvSec, tvMSec;
	private Button btnStart, btnPause, btnResume, btnReset, btnLap;
    private ListView lvTimeList;
    private ArrayAdapter<String> adapter; 
    
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case MSG_WHAT_SHOW_TIME:
				tvHour.setText(tenMSec/100/60/60+"");
				tvMin.setText(tenMSec/100/60%60+"");
				tvSec.setText(tenMSec/100%60+"");
				tvMSec.setText(tenMSec%100+"");
				break;

			default:
				break;
			}
    	};
    };
    
    private static final int MSG_WHAT_SHOW_TIME = 1; 
    
    public void onDestroy(){
    	timer.cancel();
    }
}
