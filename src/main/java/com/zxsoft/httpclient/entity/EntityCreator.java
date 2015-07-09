package com.zxsoft.httpclient.entity;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

/**
 *实体Entity类实例的创建
 * @author xuwenjuan
 *
 */
public class EntityCreator {

	public static void main(String[] args) throws ParseException, IOException {
		//A self contained, repeatable entity that obtains its content from a String
		//意味entity内容可以读取不止一次
		StringEntity entity = new StringEntity("important message", ContentType.create("text/plain", "UTF-8"));
		//设置块编码，HTTP/1.0不支持
		entity.setChunked(true);
		System.out.println(entity.getContentType());
		System.out.println(entity.getContentLength());
		System.out.println(entity.getContentEncoding());
		System.out.println(EntityUtils.toString(entity));
		System.out.println(EntityUtils.toByteArray(entity).length);
	}
}
