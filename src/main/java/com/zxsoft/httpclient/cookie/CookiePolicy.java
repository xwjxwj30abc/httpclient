package com.zxsoft.httpclient.cookie;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CookiePolicy {

	public static void main(String[] args) {
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).build();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
		RequestConfig localConfig = RequestConfig.copy(globalConfig).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
				.build();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		httpGet.setConfig(localConfig);
	}
}
