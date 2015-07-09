package com.zxsoft.httpclient.demo;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class ClientCustomContext {

	public final static void main(String[] args) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			CookieStore cookieStore = new BasicCookieStore();
			HttpContext context = new BasicHttpContext();

			// Create local HTTP context
			//HttpClientContext localContext = HttpClientContext.create();
			HttpClientContext localContext = HttpClientContext.adapt(context);

			// Bind custom cookie store to the local context
			localContext.setCookieStore(cookieStore);
			HttpGet httpget = new HttpGet("http://localhost/");
			System.out.println("Executing request " + httpget.getRequestLine());

			// Pass local context as a parameter
			CloseableHttpResponse response = httpclient.execute(httpget, localContext);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				List<Cookie> cookies = cookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("Local cookie: " + cookies.get(i));
				}
				EntityUtils.consume(response.getEntity());
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
}
