package com.zxsoft.httpclient.demo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

/**
 * 自定义httpclient
 * @author fgq
 *
 */
public class CustomClient {

	public static void main(String[] args) throws IOException {

		ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (keepAlive == -1) {
					keepAlive = 5000;
				}
				return keepAlive;
			}
		};
		CloseableHttpClient httpclient = HttpClients.custom().setKeepAliveStrategy(keepAliveStrategy).build();
		httpclient.close();
	}
}
