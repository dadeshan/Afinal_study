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
