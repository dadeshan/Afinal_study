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
	 * FinalHttp的重点：
	 *  1.提供线程池操作
	 *  2.提供 get put 方法
	 *  3.提供download 方法
	 */
	/**
	 * 看看FinalHttp是如何实例化的
	 *  public FinalHttp() {
	 *  
	 * 
	 * 	//参数设置（超时、版本、连接方式等等）
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

		//实例化 httpClient
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
		
		//设置重新连接的Handler，
        httpClient.setHttpRequestRetryHandler(new RetryHandler(maxRetries));
        //建立一个client的HashMap
        clientHeaderMap = new HashMap<String, String>();
        
    }
	 */
	
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		//主要进行了2个操作 1.提供了一个httpClient 2.设置httpParams
		finalHttp = new FinalHttp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		finalHttp = null;
	}

	
	
	/**
	 * 此时我发现FinalHttp都是异步操作，我现在没法对其进行单元测试，因为获取不到结果啊,我随便写个方法
	 * 看看get方法：
	 * 他调用了这个方法：
	 *  public void get( String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
     *  	sendRequest(httpClient, httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, callBack);
     *	}
     *	httpContext ：封装了http请求的信息
     *  将传入的 url 组装成一个 HttpGet作为参数传递给sendRequest
     *  AjaxCallBack：异步接口
     *  
     *  看看 sendRequest 方法：
     *  
     *   protected <T> void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, AjaxCallBack<T> ajaxCallBack) {
     *  	if(contentType != null) {
     *      	uriRequest.addHeader("Content-Type", contentType);
     *   	}
     *    	new HttpHandler<T>(client, httpContext, ajaxCallBack,charset).executeOnExecutor(executor, uriRequest);
     *  }
     *  很显然我们的 contentType == null。说明一下：
     *   HttpHandler  <T> extends  AsyncTask<Object, Object, Object> implements EntityCallBack
     *   可以看到 HttpHandler继承了 AsyncTask (作者修改了一些代码，以后再说)，所以这就是 运行在一个线程池中的 任务。EntityCallBack是作者自定义的接口。
     *   不得不说，小伙伴们，真正的面纱就要被揭开了。
     *   
     *   public interface EntityCallBack {
     *		public void callBack(long count,long current,boolean mustNoticeUI);
	 *	 }
	 *
	 *	 HttpHandler 既然是继承的 AsyncTask，那么任务必定在以下的方法中顺序处理：
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
	 *			//表示 开始更新 UI
	 *			publishProgress(UPDATE_START); // 开始
	 *			makeRequestWithRetries((HttpUriRequest)params[0]);
	 *
	 *
	 * 		} catch (IOException e) {
	 *			publishProgress(UPDATE_FAILURE,e,0,e.getMessage()); // 结束
	 *		}
     *
	 *		return null;
	 *	}
	 *	new HttpHandler<T>(client, httpContext, ajaxCallBack,charset).executeOnExecutor(executor, uriRequest);
	 * 	很显然，我们执行的时候只传入了uriRequest。so,length==1
	 * 	private void makeRequestWithRetries(HttpUriRequest request) throws IOException {
	 * 	
	 * 	//和上面length一样，可以看出这和afinal的download 有关，和我们本次get无关
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
					
					// 大家看到，在此处调用了你的client
					HttpResponse response = client.execute(request, context);
					if (!isCancelled()) {
						handleResponse(response);
					} 
				}
				//结束、返回
				return;
			} catch (UnknownHostException e) {
				publishProgress(UPDATE_FAILURE,e,0,"unknownHostException：can't resolve host");
				return;
			} catch (IOException e) {
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			} catch (NullPointerException e) {
				// HttpClient 4.0.x 之前的一个bug
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
			throw new IOException("未知网络错误");
	}
	
		接着来看看 handleResponse 是如何处理结果的：
		private void handleResponse(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		
		//很明显这也是处理download，与get无关，略过
		if (status.getStatusCode() >= 300) {
			String errorMsg = "response status error code:"+status.getStatusCode();
			if(status.getStatusCode() == 416 && isResume){
				errorMsg += " \n maybe you have download complete.";
			}
			publishProgress(UPDATE_FAILURE,new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),status.getStatusCode() ,errorMsg);
		}
		//我们的在这里
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
						
						// 在这里，就是把返回结果包装了一下，mStrEntityHandler 可以看到是对String进行包装，这就是afina介绍的支持 xml、json、jsonArray等
						responseBody = mStrEntityHandler.handleEntity(entity,this,charset);
					}
						
				}
				// 发送给 ui 线程，直接引起 onProgressUpdate的执行
				publishProgress(UPDATE_SUCCESS,responseBody);
				
			} catch (IOException e) {
				publishProgress(UPDATE_FAILURE,e,0,e.getMessage());
			}
			
		}
	}
		so,onProgressUpdate 看看，就这一句对我们有用：
		case UPDATE_SUCCESS:
			if(callback!=null)
				callback.onSuccess((T)values[1]);
			break;
	 */
	
	
	public void testGet(){
		finalHttp.get(URL,new AjaxCallBack<String>(){

			// 比较重要的三个方法
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
