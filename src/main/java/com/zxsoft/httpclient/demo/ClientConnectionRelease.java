package com.zxsoft.httpclient.demo;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *http客户端和响应的连接释放
 * @author xuwenjuan
 *
 */
public class ClientConnectionRelease {

	public static void main(String[] args) throws Exception {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://www.baidu.com");
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					//简单打印entity内容
					System.out.println(EntityUtils.toString(entity, "utf-8"));

					//终止http请求的方法
					//httpGet.abort();

				}
			} finally {
				//释放response连接
				response.close();
			}
		} finally {
			//释放httpClient连接
			httpClient.close();
		}
	}
}
