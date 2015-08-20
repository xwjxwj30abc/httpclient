package com.zxsoft.httpclient.future;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpRequestFutureTask;

public class FutureService {
	private HttpClient httpClient;
	private ExecutorService executorService;

	public FutureService(HttpClient httpClient, ExecutorService executorService) {
		this.httpClient = httpClient;
		this.executorService = executorService;
	}

	public FutureRequestExecutionService getFutureRequestExecutionService() {
		return new FutureRequestExecutionService(httpClient, executorService);
	}

	static class MyResponseHandler implements ResponseHandler<Boolean> {

		public Boolean handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			return response.getStatusLine().getStatusCode() == 200;
		}

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		HttpClient httpClient = HttpClientBuilder.create().setMaxConnPerRoute(5).build();
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		FutureService service = new FutureService(httpClient, executorService);

		FutureRequestExecutionService futureRrequestExecutionService = service.getFutureRequestExecutionService();
		HttpRequestFutureTask<Boolean> task = futureRrequestExecutionService.execute(
				new HttpGet("http://www.baidu.com"), HttpClientContext.create(), new MyResponseHandler());

		boolean ok = task.get();
		System.out.println(ok);
		System.out.println(task.taskDuration());
	}
}
