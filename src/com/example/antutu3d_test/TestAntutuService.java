package com.example.antutu3d_test;

import java.io.IOException;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;

public class TestAntutuService extends Service{
	public final static int  MSG_START_CLICK = 1;

	private Handler mHandler;
	TestReceiver receiver = new TestReceiver();

	private boolean mClickStarted = false;

	//receiver
	private class TestReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//receive intent to start Antutu test
			String s = intent.getAction();
			if(s.equals("com.example.antutu3d.Start_BROADCAST")) {
				StartClickTest();
			} else if(s.equals("com.example.antutu3d.Stop_BROADCAST")) {
				stopClickTest();
			}
		}
	}
	@Override
	public void onStart(Intent intent, int startId) {
		mHandler = new ClickStarTestHander();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.antutu3d.Start_BROADCAST");
		registerReceiver(receiver, filter);
		super.onStart(intent, startId);
	}
	
	class ClickStarTestHander extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	    	switch (msg.what) {
	    	case MSG_START_CLICK: {
	    		if (mHandler  !=  null) {
	    			mHandler.sendEmptyMessageDelayed(MSG_START_CLICK, 40 * 1000);
	    		}

	    		if (!mClickStarted) {
	    			mHandler.removeMessages(MSG_START_CLICK);
	    			break;
	    		}
	    		if(isSearchCenterForeground()){
	    			//topActivity is Antutu will start test
	    			int inputSource = InputDevice.SOURCE_UNKNOWN;
	    			inputSource = getSource(inputSource, InputDevice.SOURCE_TOUCHSCREEN);
	    			sendTap(inputSource,1200,540);
	    		}else{
	    			try {
	    				//topActivity is not Antutu ,will lunch Antutu 
	    				Runtime.getRuntime().exec("am start -S com.antutu.benchmark.full/com.antutu.benchmark.full.UnityPlayerActivity");
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		break;
	    		}
	    	}
	    }
    }
	    public boolean isSearchCenterForeground() {
	        Context context = getApplicationContext();
	        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
	        if (!tasks.isEmpty()) {
	            ComponentName topActivity = tasks.get(0).topActivity;
	            if (topActivity.getPackageName().equals("com.antutu.benchmark.full")) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    private final int getSource(int inputSource, int defaultSource) {
	        return inputSource == InputDevice.SOURCE_UNKNOWN ? defaultSource : inputSource;
	    }

	    //send tap
	    private void sendTap(int inputSource, float x, float y) {
	        long now = SystemClock.uptimeMillis();
	        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x, y, 1.0f);
	        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x, y, 0.0f);
	    }
	    
	    private void injectMotionEvent(int inputSource, int action, long when, float x, float y, float pressure) {
	        final float DEFAULT_SIZE = 1.0f;
	        final int DEFAULT_META_STATE = 0;
	        final float DEFAULT_PRECISION_X = 1.0f;
	        final float DEFAULT_PRECISION_Y = 1.0f;
	        final int DEFAULT_DEVICE_ID = 0;
	        final int DEFAULT_EDGE_FLAGS = 0;
	        MotionEvent event = MotionEvent.obtain(when, when, action, x, y, pressure, DEFAULT_SIZE,DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y, DEFAULT_DEVICE_ID,
	                DEFAULT_EDGE_FLAGS);
//	        event.setSource(inputSource);
	        Log.i("xiaoxi", "injectMotionEvent: " + event);
	        //INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH
	        InputManager iMgr = (InputManager) getSystemService(INPUT_SERVICE);
	        if (iMgr != null) {
	        	getSystemService(INPUT_SERVICE);
	        	//InputManager.getInstance().injectInputEvent(event,InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
	        	iMgr.injectInputEvent(event, InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH );
	        }
	    }

	private void StartClickTest() {
		if (mHandler != null) {
			mClickStarted = true;
			mHandler.sendEmptyMessage(MSG_START_CLICK);
		}
	}
	
	private void stopClickTest() {
		mClickStarted = false;
		mHandler.removeMessages(MSG_START_CLICK);
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
