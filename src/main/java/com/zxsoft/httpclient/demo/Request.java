package com.zxsoft.httpclient.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
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

	//对响应进行处理
	public void overrideResponseHandlers() throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://localhost/json");

		ResponseHandler<MyJsonObject> rh = new ResponseHandler<MyJsonObject>() {
			public MyJsonObject handleResponse(final HttpResponse response) throws IOException {
				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
				}
				if (entity == null) {
					throw new ClientProtocolException("Response contains no content");
				}
				Gson gson = new GsonBuilder().create();
				Reader reader = new InputStreamReader(entity.getContent(), ContentType.getOrDefault(entity)
						.getCharset());
				return gson.fromJson(reader, MyJsonObject.class);
			}
		};
		MyJsonObject myjson = httpClient.execute(httpget, rh);
		System.out.println(myjson.toString());
	}
}
