package com.zxsoft.httpclient.cookie;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

public class CookiePersistence {

	public static void main(String[] args) {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("name", "value");
		cookie.setVersion(0);
		cookie.setDomain(".cnzxsoft.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

	}
}
