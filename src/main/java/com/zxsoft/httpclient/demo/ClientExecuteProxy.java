package com.zxsoft.httpclient.demo;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 使用代理发送请求
 * @author xuwenjuan
 *
 */
public class ClientExecuteProxy {

	public static void main(String[] args) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {

			HttpHost target = new HttpHost("localhost", 443, "https");
			//设置代理服务器
			HttpHost proxy = new HttpHost("127.0.0.1", 80, "http");

			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			HttpGet httpGet = new HttpGet("/");
			httpGet.setConfig(config);
			CloseableHttpResponse response = httpClient.execute(target, httpGet);
			try {
				System.out.println(response.getStatusLine().getStatusCode());
				EntityUtils.consume(response.getEntity());
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}
}
