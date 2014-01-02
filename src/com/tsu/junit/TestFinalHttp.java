package com.tsu.junit;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.test.AndroidTestCase;

/**
 * 
* @Project: AfinalExceise
* @PackagName: com.tsu.junit
* @ClassName: TestFinalHttp
* @Description: TODO
* @author tadeshanc@gmail.com
* @date 2013-12-31
 */
public class TestFinalHttp extends AndroidTestCase{

	private FinalHttp finalHttp;
	private String URL;
	
	/**
	 * FinalHttp���ص㣺
	 *  1.�ṩ�̳߳ز���
	 *  2.�ṩ get put ����
	 *  3.�ṩdownload ����
	 */
	/**
	 * ����FinalHttp�����ʵ������
	 *  public FinalHttp() {
	 *  
	 * 
	 * 	//�������ã���ʱ���汾�����ӷ�ʽ�ȵȣ�
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

		//ʵ���� httpClient
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        
        httpClient = new DefaultHttpClient(cm, httpParams);
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
                for (String header : clientHeaderMap.keySet()) {
                    request.addHeader(header, clientHeaderMap.get(header));
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            break;
                        }
                    }
                }
            }
        });
		
		//�����������ӵ�Handler��
        httpClient.setHttpRequestRetryHandler(new RetryHandler(maxRetries));
        //����һ��client��HashMap
        clientHeaderMap = new HashMap<String, String>();
        
    }
	 */
	
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		//��Ҫ������2������ 1.�ṩ��һ��httpClient 2.����httpParams
		finalHttp = new FinalHttp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		finalHttp = null;
	}

	
	
	/**
	 * ��ʱ�ҷ���FinalHttp�����첽������������û��������е�Ԫ���ԣ���Ϊ��ȡ���������,�����д������
	 * ����get������
	 * �����������������
	 *  public void get( String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
     *  	sendRequest(httpClient, httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, callBack);
     *	}
     *	httpContext ����װ��http�������Ϣ
     *  ������� url ��װ��һ�� HttpGet��Ϊ�������ݸ�sendRequest
     *  AjaxCallBack���첽�ӿ�
     *  
     *  ���� sendRequest ������
     *  
     *   protected <T> void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, AjaxCallBack<T> ajaxCallBack) {
     *  	if(contentType != null) {
     *      	uriRequest.addHeader("Content-Type", contentType);
     *   	}
     *    	new HttpHandler<T>(client, httpContext, ajaxCallBack,charset).executeOnExecutor(executor, uriRequest);
     *  }
     *  ����Ȼ���ǵ� contentType == null��˵��һ�£�
     *   HttpHandler  <T> extends  AsyncTask<Object, Object, Object> implements EntityCallBack
     *   ���Կ��� HttpHandler�̳��� AsyncTask (�����޸���һЩ���룬�Ժ���˵)����������� ������һ���̳߳��е� ����EntityCallBack�������Զ���Ľӿڡ�
     *   ���ò�˵��С����ǣ���������ɴ��Ҫ���ҿ��ˡ�
     *   
     *   public interface EntityCallBack {
     *		public void callBack(long count,long current,boolean mustNoticeUI);
	 *	 }
	 *
	 *	 HttpHandler ��Ȼ�Ǽ̳е� AsyncTask����ô����ض������µķ�����˳����
	 *		1. onPreExecute()
	 *		2. doInBackground(Params...)
	 *		3. onProgressUpdate(Progress...)
	 *		4. onPostExecute(Result)
	 *
	 *		@Override
	 *	protected Object doInBackground(Object... params) {
	 *		if(params!=null && params.length == 3){
	 *		targetUrl = String.valueOf(params[1]);
	 *		isResume = (Boolean) params[2];
	 *		}
	 *		try {
	 *			//��ʾ ��ʼ���� UI
	 *			publishProgress(UPDATE_START); // ��ʼ
	 *			makeRequestWithRetries((HttpUriRequest)params[0]);
	 *
	 *
	 * 		} catch (IOException e) {
	 *			publishProgress(UPDATE_FAILURE,e,0,e.getMessage()); // ����
	 *		}
     *
	 *		return null;
	 *	}
	 *	new HttpHandler<T>(client, httpContext, ajaxCallBack,charset).executeOnExecutor(executor, uriRequest);
	 * 	����Ȼ������ִ�е�ʱ��ֻ������uriRequest��so,length==1
	 * 	private void makeRequestWithRetries(HttpUriRequest request) throws IOException {
	 * 	
	 * 	//������lengthһ�������Կ������afinal��download �йأ������Ǳ���get�޹�
		if(isResume && targetUrl!= null){
			File downloadFile = new File(targetUrl);
			long fileLen = 0;
			if(downloadFile.isFile() && downloadFile.exists()){
				fileLen = downloadFile.length();
			}
			if(fileLen > 0)
				request.setHeader("RANGE", "bytes="+fileLen+"-");
		}
		
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
		while (retry) {
			try {
				if (!isCancelled()) {
					
					// ��ҿ������ڴ˴����������client
					HttpResponse response = client.execute(request, context);
					if (!isCancelled()) {
						handleResponse(response);
					} 
				}
				//����������
				return;
			} catch (UnknownHostException e) {
				publishProgress(UPDATE_FAILURE,e,0,"unknownHostException��can't resolve host");
				return;
			} catch (IOException e) {
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			} catch (NullPointerException e) {
				// HttpClient 4.0.x ֮ǰ��һ��bug
				// http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			}catch (Exception e) {
				cause = new IOException("Exception" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			}
		}
		if(cause!=null)
			throw cause;
		else
			throw new IOException("δ֪�������");
	}
	
		���������� handleResponse ����δ������ģ�
		private void handleResponse(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		
		//��������Ҳ�Ǵ���download����get�޹أ��Թ�
		if (status.getStatusCode() >= 300) {
			String errorMsg = "response status error code:"+status.getStatusCode();
			if(status.getStatusCode() == 416 && isResume){
				errorMsg += " \n maybe you have download complete.";
			}
			publishProgress(UPDATE_FAILURE,new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),status.getStatusCode() ,errorMsg);
		}
		//���ǵ�������
		else {
			try {
				HttpEntity entity = response.getEntity();
				Object responseBody = null;
				if (entity != null) {
					time = SystemClock.uptimeMillis();
					if(targetUrl!=null){
						responseBody = mFileEntityHandler.handleEntity(entity,this,targetUrl,isResume);
					}
					else{
						
						// ��������ǰѷ��ؽ����װ��һ�£�mStrEntityHandler ���Կ����Ƕ�String���а�װ�������afina���ܵ�֧�� xml��json��jsonArray��
						responseBody = mStrEntityHandler.handleEntity(entity,this,charset);
					}
						
				}
				// ���͸� ui �̣߳�ֱ������ onProgressUpdate��ִ��
				publishProgress(UPDATE_SUCCESS,responseBody);
				
			} catch (IOException e) {
				publishProgress(UPDATE_FAILURE,e,0,e.getMessage());
			}
			
		}
	}
		so,onProgressUpdate ����������һ����������ã�
		case UPDATE_SUCCESS:
			if(callback!=null)
				callback.onSuccess((T)values[1]);
			break;
	 */
	
	
	public void testGet(){
		finalHttp.get(URL,new AjaxCallBack<String>(){

			// �Ƚ���Ҫ����������
			@Override
			public void onLoading(long count, long current) {
				// TODO Auto-generated method stub
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}
			
		});
	}
}
