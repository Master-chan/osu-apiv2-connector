package org.osumm.apiv2.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.osumm.apiv2.HttpClientProviderBase;

/**
 * Default https client implementation using java built-in client
 * @author Master-chan
 *
 */
public class JavaHttpClientProvider implements HttpClientProviderBase
{

	@Override
	public String post(URL url, String params, int timeout) throws IOException
	{
		return post(url, params, timeout, null);
	}
	
	@Override
	public String post(URL url, String params, int timeout, String authToken) throws IOException
	{
		HttpURLConnection connection = openConnection(url, timeout, null);
		connection.setRequestProperty("Accept","application/json");
		connection.setRequestProperty("Content-Type","application/json");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		//connection.connect();

		try(DataOutputStream dos = new DataOutputStream(connection.getOutputStream()))
		{
			dos.writeBytes(params);
			dos.flush();
		}
		
		if(connection.getResponseCode() != 200) 
		{
			throw new IOException("HTTP Response code: " + connection.getResponseCode());
		}
		
		return readContent(connection);
	}

	@Override
	public String get(URL url, int timeout) throws IOException
	{
		return get(url, timeout, null);
	}
	
	@Override
	public String get(URL url, int timeout, String authToken) throws IOException
	{
		HttpURLConnection connection = openConnection(url, timeout, authToken);
		connection.connect();

		if(connection.getResponseCode() != 200) 
		{
			throw new IOException("HTTP Response code: " + connection.getResponseCode());
		}

		return readContent(connection);
	}
	
	private String readContent(HttpURLConnection connection) throws IOException
	{
		try(InputStream inputStream = connection.getInputStream())
		{
			String contentEncoding = connection.getContentEncoding();
			InputStream wrapper = inputStream;
			if(contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip"))
			{
				wrapper = new GZIPInputStream(inputStream);
			}
			byte[] buf = new byte[1024];
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			for(int len; (len = wrapper.read(buf)) > 0;)
			{
				outputStream.write(buf, 0, len);
			}
			// Would be better to use guava Charsets here probably(?)
			return outputStream.toString("UTF-8");
		}
	}
	
	private HttpURLConnection openConnection(URL url, int timeout, String authToken) throws IOException
	{
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestProperty("Accept-Encoding", "gzip");
		httpConn.setConnectTimeout(timeout);
		httpConn.setReadTimeout(timeout);
		if(authToken != null && authToken.length() > 0)
		{
			httpConn.setRequestProperty("Authorization", String.format("Bearer %s", authToken));
		}
		return httpConn;
	}

}
