package com.tsu.afinalexceise;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsu.frame.THttp;
import com.tsu.frame.THttp.HttpRequestCallBack;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private String TAG = "MainActivity";
	private THttp tHttp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tHttp = new THttp();
	}

	
	public void onclick(View view){
		
		switch (view.getId()) {
		case R.id.btn_thttp:
			testThttp();
			break;

		}
	}
	
	private void testThttp(){
		//司考吧的公开接口来测试
		tHttp.getHttpRequest("http://lawedu.duapp.com/getAndroidMZPL?mzpl=0&aid=0", new HttpRequestCallBack<JSONObject>() {

			@Override
			public void requestLoding(long total, long current) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void requestSuccess(JSONObject t) {
				// TODO Auto-generated method stub
				try {
					Log.i(TAG,t.getString("caseAna"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void requestFailure(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}


