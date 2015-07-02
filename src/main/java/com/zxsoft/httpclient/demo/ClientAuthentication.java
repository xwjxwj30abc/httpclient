package com.zxsoft.httpclient.demo;

import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *需要验证用户的请求
 * @author xuwenjuan
 *
 */
public class ClientAuthentication {

	public static void main(String[] args) throws ClientProtocolException, IOException {

		//证书
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials("userName", "password"));
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
				.build();

		try {
			HttpGet httpGet = new HttpGet("http://mail.zxisl.com");
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				System.out.println(response.getStatusLine());
				EntityUtils.consume(response.getEntity());
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}
}
