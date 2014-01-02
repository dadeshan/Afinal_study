package com.tsu.frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

/**
 * @author tadeshanc@gmail.com
 * @date 2014-1-2
 * @Description: 写一个自己的终极Http请求，当然了，很简洁
 * 
 * 我们来分析一下afinal的总体架构：
 * 1.封装一个Http请求。
 * 2.执行在一个线程池中。
 * 3.提供回调方法 。
 * 
 * ok，我们的架构也是依照这个方法来，我们来个支持json的。接口等一律使用 内部类
 */
public class THttp {

	private HttpClient httpClient;
	private HttpGet httpGet;
	
	//固定为2个线程
	private final Executor executor = Executors.newFixedThreadPool(2);
	
	public THttp(){
		//userAgent 可不设置
		httpClient = AndroidHttpClient.newInstance(null);
	}
	//getHttp 方法
	
	public <T> void getHttpRequest(String uri,HttpRequestCallBack<T> callBack){
		httpGet = new HttpGet(uri);
		//方法去执行
		new HttpHandler<T>().executeOnExecutor(executor, callBack);
	}
	
	
	private final class HttpHandler<T> extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				Object entity = jsonEntityHandler.handler(this,httpResponse);
				publishProgress(HttpStatus.SC_OK,entity);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			int code = (Integer) values[0];
			T t = (T) values[1];
		}
		
		
		private final class JSONObjectEntity{

			public JSONObject handler(HttpResponse httpResponse) throws Exception {
				// TODO Auto-generated method stub
				InputStream inputStream = httpResponse.getEntity().getContent();
				ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inputStream.read(buffer)) !=-1 ){
					outSteam.write(buffer, 0, len);
				}
				outSteam.close();
				inputStream.close();
				return new JSONObject(new String(outSteam.toByteArray()));
			}
			
		}
	}
	
	
	interface HttpRequestCallBack<T>{
		
		//1.loading
		void requestLoding(long total,long current);
		//2.success
		void requestSuccess(T t);
		//3.failure
		void requestFailure(Exception e);
	}
}
