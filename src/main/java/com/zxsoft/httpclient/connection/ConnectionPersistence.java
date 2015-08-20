package com.zxsoft.httpclient.connection;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

public class ConnectionPersistence {
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, HttpException {

		BasicHttpClientConnectionManager basicConnManager = new BasicHttpClientConnectionManager();
		HttpContext context = HttpClientContext.create();
		HttpRoute route = new HttpRoute(new HttpHost("www.baidu.com", 80));
		ConnectionRequest connRequest = basicConnManager.requestConnection(route, null);
		HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
		basicConnManager.connect(conn, route, 1000, context);
		basicConnManager.routeComplete(conn, route, context);

		HttpRequestExecutor exeRequest = new HttpRequestExecutor();
		context.setAttribute("target host", new HttpHost("www.baidu.com", 80));
		HttpGet get = new HttpGet("http://www.baidu.com/");
		exeRequest.execute(get, conn, context);
		//most important
		basicConnManager.releaseConnection(conn, null, 1, TimeUnit.SECONDS);

		CloseableHttpClient client = HttpClients.custom().setConnectionManager(basicConnManager).build();
		client.execute(get);
	}
}
