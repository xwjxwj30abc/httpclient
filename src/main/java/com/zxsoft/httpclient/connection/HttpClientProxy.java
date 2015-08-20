package com.zxsoft.httpclient.connection;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;

/**
 * 三种代理请求方式设置
 * @author fgq
 *
 */
public class HttpClientProxy {

	//httpclient客户端通过RoutePlanner设置代理
	public CloseableHttpClient setProxy1() {
		HttpHost proxy = new HttpHost("192.168.32.111", 18000);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		CloseableHttpClient httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		return httpClient;
	}

	//httpclient客户端直接设置代理
	public CloseableHttpClient setProxy2() {
		HttpHost proxy = new HttpHost("192.168.32.111", 18000);
		CloseableHttpClient httpClient = HttpClients.custom().setProxy(proxy).build();
		return httpClient;
	}

	//对单个请求设置代理
	public CloseableHttpResponse setProxy3() throws ClientProtocolException, IOException {
		HttpHost proxy = new HttpHost("192.168.32.111", 18000);
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		httpGet.setConfig(config);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpGet);
		return response;
	}

	//通过定制的RoutPlanner路由设置代理

	public CloseableHttpClient setProxy4() {
		HttpRoutePlanner routePlanner = new HttpRoutePlanner() {

			public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context)
					throws HttpException {
				return new HttpRoute(target, null, new HttpHost("192.168.32.111", 18000),
						"https".equalsIgnoreCase(target.getSchemeName()));
			}
		};
		CloseableHttpClient httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		return httpClient;
	}

	public static void main(String[] args) {
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		HttpHost target = new HttpHost("localhost", 80, "http");
		HttpClientProxy pro = new HttpClientProxy();
		try {
			//CloseableHttpResponse response = pro.setProxy1().execute(target, httpGet);
			//CloseableHttpResponse response = pro.setProxy2().execute(target, httpGet);
			CloseableHttpResponse response = pro.setProxy4().execute(target, httpGet);
			System.out.println("客户端代理设置请求结果：" + response.getStatusLine());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("单个请求设置代理执行结果：" + pro.setProxy3().getStatusLine());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
