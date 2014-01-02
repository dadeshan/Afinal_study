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
 * @Description: дһ���Լ����ռ�Http���󣬵�Ȼ�ˣ��ܼ��
 * 
 * ����������һ��afinal������ܹ���
 * 1.��װһ��Http����
 * 2.ִ����һ���̳߳��С�
 * 3.�ṩ�ص����� ��
 * 
 * ok�����ǵļܹ�Ҳ�������������������������֧��json�ġ��ӿڵ�һ��ʹ�� �ڲ���
 */
public class THttp {

	private HttpClient httpClient;
	private HttpGet httpGet;
	
	//�̶�Ϊ2���߳�
	private final Executor executor = Executors.newFixedThreadPool(2);
	
	public THttp(){
		//userAgent �ɲ�����
		httpClient = AndroidHttpClient.newInstance(null);
	}
	//getHttp ����
	
	public <T> void getHttpRequest(String uri,HttpRequestCallBack<T> callBack){
		httpGet = new HttpGet(uri);
		//����ȥִ��
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
