package com.zxsoft.httpclient.demo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *Get请求
 * @author xuwenjuan
 *
 */
public class Request {

	public static void main(String[] args) throws Exception {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			HttpGet httpGet = new HttpGet("http://www.baidu.com");
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				//handleResponse实现对请求响应HttpResponse的处理
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status:" + status);
					}
				}
			};
			//执行get请求
			httpClient.execute(httpGet, responseHandler);
		} finally {
			httpClient.close();
		}

	}
}
