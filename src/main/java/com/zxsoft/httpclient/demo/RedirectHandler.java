package com.zxsoft.httpclient.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 重定向
 * @author fgq
 *
 */
public class RedirectHandler {

	public static void main(String[] args) throws IOException, URISyntaxException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpget = new HttpGet("http://www.weibo.com/");
		CloseableHttpResponse response = httpclient.execute(httpget, context);
		try {
			HttpHost target = context.getTargetHost();
			List<URI> redirectLocations = context.getRedirectLocations();
			URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
			System.out.println("Final HTTP location: " + location.toASCIIString());
		} finally {
			response.close();
		}
	}
}
