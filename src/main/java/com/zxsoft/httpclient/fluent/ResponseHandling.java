package com.zxsoft.httpclient.fluent;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 添加response handler响应处理器，获取想要的数据内容
 * @author xuwenjuan
 *
 */
public class ResponseHandling {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		Document result = Request.Get("http://baidu.com").execute().handleResponse(new ResponseHandler<Document>() {

			public Document handleResponse(HttpResponse response) throws IOException {

				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				//如果状态码大于300，抛出响应异常
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
				}
				//如果响应体为空，抛出客户端协议异常，并说明响应无内容实体
				if (entity == null) {
					throw new ClientProtocolException("response contains no content");
				}

				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
					ContentType contentType = ContentType.getOrDefault(entity);
					//如果响应实体的类型不是期望的类型，抛出客户端协议异常，并提示不期望的内容类型
					if (!contentType.equals(ContentType.APPLICATION_XML)) {
						throw new ClientProtocolException("unexpected content type:" + contentType);
					}
					Charset charset = contentType.getCharset();
					if (charset == null) {
						charset = HTTP.DEF_CONTENT_CHARSET;
					}
					//返回经过resonse hanlder处理后的Document
					return docBuilder.parse(entity.getContent(), charset.toString());
				} catch (ParserConfigurationException ex) {
					throw new IllegalStateException(ex);
				} catch (SAXException ex) {
					throw new ClientProtocolException("Malformed XML document", ex);
				}
			}
		});

		System.out.println(result);
	}
}
