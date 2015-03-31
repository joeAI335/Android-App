package com.example.tulingdemo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements HttpGetListener, OnClickListener{
    
	private HttpData httpData;
	private List<ListData> lists;
	private ListView lv;
	private EditText sendText;
	private Button send_btn;
	private String content_str;
	private TextAdapter adapter;
	private String[] welcome_arr;
	private double currentTime, oldTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
    
	private void initView(){
		lv = (ListView) findViewById(R.id.lv);
		sendText = (EditText) findViewById(R.id.sendText);
		send_btn = (Button) findViewById(R.id.send_btn);
		lists = new ArrayList<ListData>();
		send_btn.setOnClickListener(this);
		adapter = new TextAdapter(lists, this);
		lv.setAdapter(adapter);
		ListData listData;
		listData = new ListData(getRandomWelcomTips(), ListData.RECEIVER);
		lists.add(listData);
	}
	
	private String getRandomWelcomTips(){
		String welcome_tip = null;
		welcome_arr = this.getResources().getStringArray(R.array.welcome_tips);
		int index = (int) (Math.random()*(welcome_arr.length - 1));
		welcome_tip = welcome_arr[index];
		return welcome_tip;
	}
	@Override
	public void getDataUrl(String data) {
		parseText(data);
	}
	
	public void parseText(String str){
		try {
			JSONObject jb = new JSONObject();
			ListData listData;
			listData = new ListData(jb.getString("text"), ListData.RECEIVER);
			lists.add(listData);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		content_str = sendText.getText().toString();
		ListData listData;
		listData = new ListData(content_str, ListData.SEND);
		String dropk = content_str.replace(" ", "");
		String droph = dropk.replace("\n", "");
		lists.add(listData);
		if (lists.size() > 30) {
			for (int i = 0; i < lists.size(); i++) {
				lists.remove(i);
			}
		}
		adapter.notifyDataSetChanged();
		httpData = (HttpData) new HttpData("http://www.tuling123.com/openapi/api?key=6af9822f5491fadfc142b53818bbd63a&info="
				+ droph, this).execute();
	}
	
	private String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyyÄêmmÔÂddÈÕ   HH:MM:SS");
		Date curDate = new Date(0);
		String str = format.format(curDate);
		if (currentTime - oldTime >= 5*60*1000) {
			oldTime = currentTime;
			return str;
		}else{
			return "";
		}
	}
	
}
