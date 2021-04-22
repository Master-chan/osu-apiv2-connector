package org.osumm.apiv2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.osumm.apiv2.endpoints.impl.GetClientCredentialsToken;
import org.osumm.apiv2.endpoints.impl.GetClientCredentialsToken.ClientCredentialsRequest;
import org.osumm.apiv2.endpoints.impl.GetClientCredentialsToken.ClientCredentialsResponse;

import com.googlecode.concurentlocks.ReadWriteUpdateLock;
import com.googlecode.concurentlocks.ReentrantReadWriteUpdateLock;

/**
 * Auth tokens handler.
 * @author Master-chan
 *
 */
public class AuthHandler
{
	
	private GetClientCredentialsToken authEndpoint;
	private final ReadWriteUpdateLock lock = new ReentrantReadWriteUpdateLock();
	
	private String authToken;
	private long expire;
	
	public String updateAuthToken(OsuApi api) throws IOException
	{
		lock.writeLock().lock();
		try
		{
			if(authEndpoint == null)
			{
				authEndpoint = new GetClientCredentialsToken(api.getBaseUrl(), api.getHttpClientProvider(), api.getJsonSerializationProvider());
			}
			// Grab new token
			ClientCredentialsResponse token = authEndpoint.request(new ClientCredentialsRequest(api.getClientId(), api.getClientSecret()), null);
			expire = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(token.getExpiresIn()) - TimeUnit.SECONDS.toMillis(5);
			authToken = token.getAccessToken();
			return authToken;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}
	
	public String getAuthToken(OsuApi api) throws IOException
	{
		lock.updateLock().lock();
		try
		{
			if(authToken == null || System.currentTimeMillis() >= expire)
			{
				lock.writeLock().lock();
				try
				{
					if(authToken == null || System.currentTimeMillis() >= expire)
					{
						return updateAuthToken(api);
					}
				}
				finally
				{
					lock.writeLock().unlock();
				}
			}
			return authToken;
		}
		finally
		{
			lock.updateLock().unlock();
		}
	}
	
}
