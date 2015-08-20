package com.zxsoft.httpclient.demo;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

public class RequestRetry {

	/**
	 * 定制异常处理机制
	 * @return
	 */
	public HttpRequestRetryHandler customRequestretryHandler() {
		HttpRequestRetryHandler myhandler = new HttpRequestRetryHandler() {

			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

				if (executionCount >= 5) {
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					return false;
				}
				if (exception instanceof UnknownHostException) {
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					return false;
				}
				if (exception instanceof SSLException) {
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					return true;
				}
				return false;
			}
		};
		return myhandler;
	}

	public static void main(String[] args) throws IOException {
		RequestRetry requestRetry = new RequestRetry();
		CloseableHttpClient httpClient = HttpClients.custom().setRetryHandler(requestRetry.customRequestretryHandler())
				.build();
		httpClient.close();
	}
}
