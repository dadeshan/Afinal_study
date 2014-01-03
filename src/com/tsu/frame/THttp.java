package com.tsu.frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

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
	private String TAG = "THttp";
	
	//固定为2个线程
	private final Executor executor = Executors.newFixedThreadPool(2);
	
	public THttp(){
		//userAgent 可不设置
		httpClient = new DefaultHttpClient();
	}
	//getHttp 方法
	
	public <T> void getHttpRequest(String uri,HttpRequestCallBack<T> callBack){
		httpGet = new HttpGet(uri);
		//方法去执行
		new HttpHandler<T>().executeOnExecutor(executor, callBack);
	}
	
	
	private final int HTTP_SUCCESS = 200;
	private final int HTTP_FAILURE = 100;
	private final int HTTP_LOADING = 300;
	private final class HttpHandler<T> extends AsyncTask<Object, Object, Object>{

		private HttpRequestCallBack<T> callBack;
		private Class<T> clazz ;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.i(TAG,"执行之前");
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				
				Log.i(TAG,"执行开始");
				callBack = (HttpRequestCallBack) params[0];

				//这个地方，我们肯定要获取泛型的类型，以便在下面进行对应的操作，但是泛型类型获取，我现在没能做到，so..直接给class赋特定值
				//想必afinal仍然是同样的问题
				clazz = (Class<T>) JSONObject.class;
				Log.i(TAG,"泛型获取："+clazz.getSimpleName());
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				Log.i(TAG,"响应码："+httpResponse.getStatusLine().getStatusCode());
				
				if(httpResponse.getStatusLine().getStatusCode()==200){
					Object entity = null;
					if(clazz.getSimpleName().equals(JSONObject.class.getSimpleName())){
						Log.i(TAG,"jsonbject 泛型判断");
						entity = handlerJsonObjectEntity(httpResponse);
					}else{
						//...
					}
					
					publishProgress(HTTP_SUCCESS,entity);
				}
				
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG,e.getMessage());
			}
			
			return null;
		}
		
		
		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			int code = (Integer) values[0];		
			switch (code) {
			case HTTP_SUCCESS:
				callBack.requestSuccess((T) values[1]);
				break;

			case HTTP_LOADING:
				
				break;
			case HTTP_FAILURE:
				
				break;
			}
		}
		
	
		/**
		 * 处理jsonArray的handler
		 * @param httpResponse
		 * @return
		 * @throws Exception
		 */
		JSONObject handlerJsonObjectEntity(HttpResponse httpResponse) throws Exception {
			// TODO Auto-generated method stub
			InputStream inputStream = httpResponse.getEntity().getContent();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len = inputStream.read(buffer)) !=-1 ){
				outSteam.write(buffer, 0, len);
				//1.状态 2.进度,不再写了
				//publishProgress(HTTP_LOADING,current);
			}
			String data = new String(outSteam.toByteArray());
			outSteam.close();
			inputStream.close();
			return new JSONObject(data);
		}
		
		
	}
	
	
	public interface HttpRequestCallBack<T>{
		
		//1.loading
		void requestLoding(long total,long current);
		//2.success
		void requestSuccess(T t);
		//3.failure
		void requestFailure(Exception e);
	}
}
