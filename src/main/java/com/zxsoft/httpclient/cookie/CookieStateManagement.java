package com.zxsoft.httpclient.cookie;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

public class CookieStateManagement {
	public static void main(String[] args) {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		Lookup<CookieSpecProvider> lookup = null;
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("name", "value");
		cookie.setVersion(0);
		cookie.setDomain(".cnzxsoft.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		context.setCookieSpecRegistry(lookup);
		HttpGet httpGet = new HttpGet("http://baidu.com");
		try {
			CloseableHttpResponse response1 = httpClient.execute(httpGet, context);
			response1.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CookieOrigin cookieOrigin = context.getCookieOrigin();
		CookieSpec cookieSpec = context.getCookieSpec();
		Lookup<CookieSpecProvider> re = context.getCookieSpecRegistry();
		CookieStore store = context.getCookieStore();
		System.out.println(cookieOrigin.toString());
		System.out.println(cookieSpec.toString());
		System.out.println(re.toString());
		System.out.println(store.toString());

	}
}
