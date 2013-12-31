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
