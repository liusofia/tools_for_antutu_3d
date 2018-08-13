package com.example.antutu3d_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	   private Button mStartButton;
	   private Button mStopButton;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//start service
		startService(new Intent(this, TestAntutuService.class));
		mStartButton = (Button)findViewById(R.id.StartTestButton);
		mStopButton = (Button)findViewById(R.id.StopTestButton);
		mStopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendStop();
			}
		});
		
		mStartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendStart();
			}
		});
	}
	public void sendStart(){
		//send start AnTuTu intent
		Intent i = new Intent();
		i.setAction("com.example.antutu3d.Start_BROADCAST");
		sendBroadcast(i);
	}
	public void sendStop(){
		Intent i = new Intent();
		i.setAction("com.example.antutu3d.Stop_BROADCAST");
		sendBroadcast(i);
	}
}