package org.osumm.apiv2;

import java.io.IOException;
import java.net.URL;

import org.osumm.apiv2.impl.JavaHttpClientProvider;

/**
 * HTTP client provider interface.
 * @author Master-chan
 * @see {@link JavaHttpClientProvider}
 */
public interface HttpClientProviderBase
{
	
	/**
	 * Makes POST call to specific URL. Currently only used in authorization flow and uses "application/json" as content type.
	 * @param url - destination
	 * @param params - POST parameters body
	 * @param timeout - connection timeout
	 */
	public String post(URL url, String params, int timeout) throws IOException;
	
	/**
	 * Makes POST call to specific URL. Currently only used in authorization flow and uses "application/json" as content type.
	 * @param url - destination
	 * @param params - POST parameters body
	 * @param timeout - connection timeout
	 * @param authToken - authentication token
	 */
	public String post(URL url, String params, int timeout, String authToken) throws IOException;
	
	/**
	 * Makes GET call to specific URL.
	 * @param url - destination (with parameters)
	 * @param timeout - connection timeout
	 */
	public String get(URL url, int timeout) throws IOException;
	
	/**
	 * Makes GET call to specific URL with provided authentication token.
	 * @param url - destination (with parameters)
	 * @param timeout - connection timeout
	 * @param authToken - authentication token
	 */
	public String get(URL url, int timeout, String authToken) throws IOException;
	
}
