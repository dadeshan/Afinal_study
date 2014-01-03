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
	private String TAG = "THttp";
	
	//�̶�Ϊ2���߳�
	private final Executor executor = Executors.newFixedThreadPool(2);
	
	public THttp(){
		//userAgent �ɲ�����
		httpClient = new DefaultHttpClient();
	}
	//getHttp ����
	
	public <T> void getHttpRequest(String uri,HttpRequestCallBack<T> callBack){
		httpGet = new HttpGet(uri);
		//����ȥִ��
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
			Log.i(TAG,"ִ��֮ǰ");
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				
				Log.i(TAG,"ִ�п�ʼ");
				callBack = (HttpRequestCallBack) params[0];

				//����ط������ǿ϶�Ҫ��ȡ���͵����ͣ��Ա���������ж�Ӧ�Ĳ��������Ƿ������ͻ�ȡ��������û��������so..ֱ�Ӹ�class���ض�ֵ
				//���afinal��Ȼ��ͬ��������
				clazz = (Class<T>) JSONObject.class;
				Log.i(TAG,"���ͻ�ȡ��"+clazz.getSimpleName());
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				Log.i(TAG,"��Ӧ�룺"+httpResponse.getStatusLine().getStatusCode());
				
				if(httpResponse.getStatusLine().getStatusCode()==200){
					Object entity = null;
					if(clazz.getSimpleName().equals(JSONObject.class.getSimpleName())){
						Log.i(TAG,"jsonbject �����ж�");
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
		 * ����jsonArray��handler
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
				//1.״̬ 2.����,����д��
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
