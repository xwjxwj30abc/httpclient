package com.zxsoft.httpclient.caching;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;

/**
 *http客户端使用缓存示例
 * @author xuwenjuan
 *
 */
public class CacheUsage {

	//缓存如何使用？？？？
	public static void main(String[] args) throws ClientProtocolException, IOException {

		CacheConfig cacheConfig = CacheConfig.custom().setMaxCacheEntries(1000).setMaxObjectSize(819200).build();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		CloseableHttpClient cachingClient = CachingHttpClients.custom().setCacheConfig(cacheConfig)
				.setDefaultRequestConfig(requestConfig).build();
		HttpCacheContext context = HttpCacheContext.create();
		HttpGet httpGet = new HttpGet("http://i.baidu.com");
		CloseableHttpResponse response = cachingClient.execute(httpGet, context);
		Header head = response.getFirstHeader("Cache-Control");
		System.out.println(head.toString());
		try {
			CacheResponseStatus responseStatus = context.getCacheResponseStatus();
			switch (responseStatus) {
			case CACHE_HIT:
				System.out.println("a response was generated from the cache with no requests sent upstream");
				break;
			case CACHE_MODULE_RESPONSE:
				System.out.println("the response came from an upstream server");
				break;
			case VALIDATED:
				System.out
						.println("the response was generated from the cache after validating the entry with the origin server");
				break;
			case CACHE_MISS:
				System.out.println("cache miss");
				break;
			default:
				break;
			}
		} finally {
			response.close();
		}

	}
}
