package org.osumm.apiv2.endpoints;

import java.io.IOException;

import org.osumm.apiv2.HttpClientProviderBase;
import org.osumm.apiv2.JsonSerializationProviderBase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Interface for endpoint requests. 
 * @author Master-chan
 *
 * @param <R> Request object data type
 * @param <R1> Response object data type
 */
@RequiredArgsConstructor
public abstract class Endpoint<R, R1>
{

	@Getter protected final String baseUrl;
	@Getter protected final HttpClientProviderBase httpClientProvider;
	@Getter protected final JsonSerializationProviderBase jsonProvider;
	
	/**
	 * Makes synchronous request using provided HTTP(S) client
	 * @param client
	 * @param paramHolder
	 * @return
	 */
	public abstract R1 request(R paramHolder, String authToken) throws IOException;
	
}
