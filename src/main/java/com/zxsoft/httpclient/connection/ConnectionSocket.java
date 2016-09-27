package com.zxsoft.httpclient.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class ConnectionSocket {

	public static void main(String[] args) throws IOException {

		HttpClientContext clientContext = HttpClientContext.create();
		PlainConnectionSocketFactory sf = PlainConnectionSocketFactory.getSocketFactory();

		Socket socket = sf.createSocket(clientContext);
		int timeout = 1000;
		HttpHost target = new HttpHost("localhost");
		InetSocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }),
				80);
		sf.connectSocket(timeout, socket, target, remoteAddress, null, clientContext);
		//与Connection manager整合
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create().register("http", sf)
				.build();
		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
		HttpClients.custom().setConnectionManager(cm).build();

	}
}
