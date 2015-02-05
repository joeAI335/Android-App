package com.example.clock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TimerView extends LinearLayout {

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TimerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	protected void onFinishInflate() {
		super.onFinishInflate();
		btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopTimer();
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
			}
		});
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopTimer();
				
				etHour.setText("0");
				etMinutes.setText("0");
				etSeconds.setText("0");
				
				btnReset.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		btnResume = (Button) findViewById(R.id.btnResume);
		btnResume.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startTimer();
				
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				
			}
		});
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startTimer();
				
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.VISIBLE);
			}
		});
		
		etHour = (EditText) findViewById(R.id.etHours);
		etMinutes = (EditText) findViewById(R.id.etMinutes);
		etSeconds = (EditText) findViewById(R.id.etSeconds);
		
		etHour.setText("00");
		etHour.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());
					
					if (value>59) {
						etHour.setText("59");
					}else if(value<0){
						etHour.setText("00");
					}
				}
				checkToEnableBtnStart();	
				
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			public void afterTextChanged(Editable s) {	
			}
		});
		etMinutes.setText("00");
		etMinutes.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());
					
					if (value>59) {
						etMinutes.setText("59");
					}else if(value<0){
						etMinutes.setText("00");
					}
				}
				checkToEnableBtnStart();	
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			
			}
			public void afterTextChanged(Editable s) {
				
			}
		});
		etSeconds.setText("00");
		etSeconds.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());
					
					if (value>59) {
						etSeconds.setText("59");
					}else if(value<0){
						etSeconds.setText("00");
					}
				}
				checkToEnableBtnStart();	
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		btnStart.setVisibility(View.VISIBLE);
		btnStart.setEnabled(false);
		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);
	}
	
	private void checkToEnableBtnStart(){
		btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()))&&(Integer.parseInt(etHour.getText().toString())>0)||
				(!TextUtils.isEmpty(etMinutes.getText()))&&(Integer.parseInt(etMinutes.getText().toString())>0)||
				(!TextUtils.isEmpty(etSeconds.getText()))&&(Integer.parseInt(etSeconds.getText().toString())>0));
	}
	
	private void startTimer(){
		if (timerTask==null) {
			allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60+
					Integer.parseInt(etMinutes.getText().toString())*60+
					Integer.parseInt(etSeconds.getText().toString());
			timerTask = new TimerTask() {
				
				@Override
				public void run() {
					allTimerCount--;
					
					handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
					
					if (allTimerCount<=0) {
						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
						stopTimer();
					}
				}
			};
			
			timer.schedule(timerTask, 1000, 1000);
		}
	}
	
	private void stopTimer(){
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask = null;
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_TIME_TICK:
				
				int hour = allTimerCount/60/60;
				int min = (allTimerCount/60)%60;
				int sec = allTimerCount%60;
				
				etHour.setText(hour+"");
				etMinutes.setText(min+"");
				etSeconds.setText(sec+"");
				
				break;
			case MSG_WHAT_TIME_IS_UP:
				new AlertDialog.Builder(getContext()).
				                        setTitle("Time is up").setMessage("Time is up").
				                        setNegativeButton("Cancel", null).show();
				
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
				
				break;
			default:
				break;
			}
		};
	};
	
	private static final int MSG_WHAT_TIME_IS_UP = 1;
	private static final int MSG_WHAT_TIME_TICK = 2;
	
	private int allTimerCount = 0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private Button btnStart, btnPause, btnResume, btnReset;
	private EditText etHour, etMinutes, etSeconds;

}
