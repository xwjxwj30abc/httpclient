package com.zxsoft.httpclient.connection;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 *客户端连接池管理，为执行多线程提供连接请求
 * @author xuwenjuan
 *
 */
public class PoolingConnectionManager {

	public static void main(String[] args) throws InterruptedException, IOException {

		//设置connection manager
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		HttpHost localhost = new HttpHost("localhost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

		String[] urisToGet = { "http://www.baidu.com/", "http://dict.youdao.com/", "http://www.ibm.com/",
		"http://msdn.itellyou.cn/" };

		GetThread[] threads = new GetThread[urisToGet.length];
		for (int i = 0; i < threads.length; i++) {
			HttpGet httpget = new HttpGet(urisToGet[i]);
			threads[i] = new GetThread(httpClient, httpget);
		}

		for (int j = 0; j < threads.length; j++) {
			threads[j].start();
		}
		for (int j = 0; j < threads.length; j++) {
			threads[j].join();
		}
		httpClient.close();

	}

	static class GetThread extends Thread {
		private final CloseableHttpClient httpClient;
		private final HttpContext context;
		private final HttpGet httpGet;

		public GetThread(CloseableHttpClient httpClient, HttpGet httpGet) {
			this.httpClient = httpClient;
			this.context = HttpClientContext.create();
			this.httpGet = httpGet;
		}

		@Override
		public void run() {
			try {
				CloseableHttpResponse response = httpClient.execute(httpGet, context);
				try {
					HttpEntity entity = response.getEntity();
					System.out.println(EntityUtils.toString(entity));
				} finally {
					response.close();
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
