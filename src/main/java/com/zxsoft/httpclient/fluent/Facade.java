package com.zxsoft.httpclient.fluent;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

/**
 * 通过流操作，简化请求
 * @author xuwenjuan
 *
 */
public class Facade {

	public static void main(String[] args) throws ClientProtocolException, IOException {

		//执行get请求并设置超时时间，返回响应状态
		StatusLine status = Request.Get("http://baidu.com").connectTimeout(1000).socketTimeout(1000).execute()
				.returnResponse().getStatusLine();
		System.out.println(status.toString());

		//post请求
		String url = "";
		Request.Post(url).useExpectContinue().version(HttpVersion.HTTP_1_1)
				.bodyString("important stuff", ContentType.DEFAULT_TEXT).execute().returnContent().asBytes();
		//post请求
		Request.Post(url).addHeader("X-Custom-header", "stuff").viaProxy(new HttpHost("", 18000))
		.bodyForm(Form.form().add("username", "vip").add("password", "secret").build()).execute()
		.saveContent(new File("result.dump"));
	}
}
