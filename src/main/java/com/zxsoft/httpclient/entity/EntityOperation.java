package com.zxsoft.httpclient.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class EntityOperation {

	//注意instream流的关闭
	public void getFullEntity() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					//do something useful
				} finally {
					instream.close();
				}
			}
		} finally {
			response.close();
		}
	}

	//如果只需要entity的一部分，就可以直接关闭response
	public void getPortionEntity() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				int byteOne = instream.read();
				int byteTwo = instream.read();
				System.out.println(byteOne);
				System.out.println(byteTwo);
			}
		} finally {
			response.close();
		}
	}

	//推荐的消耗实体entity的方式
	public void getEntity() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long len = entity.getContentLength();
				if (len != -1 && len < 2048) {
					//不建议使用EntityUtils，除非能确定响应实体由受信任的http服务器产生，并且长度有限
					System.out.println(EntityUtils.toString(entity));
				} else {
					//
				}
			}
		} finally {
			response.close();
		}
	}

	public void getbufferedEntity() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				//如果entity内容不止读一次，就可以通过BufferedHttpEntity类包装httpEntity,以便于读入内存缓冲区
				entity = new BufferedHttpEntity(entity);
			}
		} finally {
			response.close();
		}
	}

	//从文件中加载内容到post,put请求的entity中
	public void postFileEntity() {
		File file = new File("test.txt");
		FileEntity fileEntity = new FileEntity(file, ContentType.create("text/plain", "UTF_8"));
		HttpPost httppost = new HttpPost("http://localhost/action.do");
		httppost.setEntity(fileEntity);
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		EntityOperation operation = new EntityOperation();
		operation.getFullEntity();
		operation.getPortionEntity();
	}
}
