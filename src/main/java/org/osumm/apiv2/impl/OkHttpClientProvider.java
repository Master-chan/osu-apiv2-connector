package org.osumm.apiv2.impl;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.osumm.apiv2.HttpClientProviderBase;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Http provider using OkHttp library
 * @author Master-chan
 *
 */
public class OkHttpClientProvider implements HttpClientProviderBase
{
	
	
	private OkHttpClient client;
	
	public OkHttpClientProvider(Proxy proxy)
	{
		client = new OkHttpClient.Builder()
				// OkHttp doesn't support timeout per-request without creating new client every time;
				.readTimeout(5, TimeUnit.SECONDS)
				.writeTimeout(5, TimeUnit.SECONDS)
				.proxy(proxy != null ? proxy : Proxy.NO_PROXY)
				.build();
	}
	
	public OkHttpClientProvider()
	{
		this(null);
	}

	@Override
	public String post(URL url, String params, int timeout) throws IOException
	{
		return post(url, params, timeout, null);
	}

	@Override
	public String post(URL url, String params, int timeout, String authToken) throws IOException
	{
		Request request = new Request.Builder()
				.url(url)
				.addHeader("Authorization", String.format("Bearer %s", authToken))
				.addHeader("Accept-Encoding", "gzip")
				.post(RequestBody.create(params, MediaType.get("appliation/json")))
				.build();
		
		try(Response response = client.newCall(request).execute())
		{
			// Mimic default HTTP client provide behavior
			if(response.code() != 200) 
			{
				throw new IOException("HTTP Response code: " + response.code());
			}
			
			if("gzip".equalsIgnoreCase(response.header("Content-Encoding")))
			{
				return unzip(response.body());
			}
			else
			{
				return response.body().string();
			}
		}
	}

	@Override
	public String get(URL url, int timeout) throws IOException
	{
		return get(url, timeout, null);
	}

	@Override
	public String get(URL url, int timeout, String authToken) throws IOException
	{
		Request request = new Request.Builder()
				.url(url)
				.addHeader("Authorization", String.format("Bearer %s", authToken))
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		try(Response response = client.newCall(request).execute())
		{
			// Mimic default HTTP client provide behavior
			if(response.code() != 200) 
			{
				throw new IOException("HTTP Response code: " + response.code());
			}
			
			if("gzip".equalsIgnoreCase(response.header("Content-Encoding")))
			{
				return unzip(response.body());
			}
			else
			{
				return response.body().string();
			}
		}
	}

	private String unzip(ResponseBody body) throws IOException
	{
		try(GzipSource responseBody = new GzipSource(body.source());
		BufferedSource source = Okio.buffer(responseBody))
		{
			return source.readUtf8();
		}
	}
}
