package org.osumm.apiv2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osumm.apiv2.endpoints.Endpoint;
import org.osumm.apiv2.endpoints.EndpointNotRegisteredException;
import org.osumm.apiv2.endpoints.EndpointRegistrationException;
import org.osumm.apiv2.impl.JacksonSerializationProvider;
import org.osumm.apiv2.impl.JavaHttpClientProvider;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Main class for accessing the API. 
 * You should provide {@link #clientId clientId} and {@link #clientSecret clientSecret} 
 * from your osu! application settings page into builder.
 * There are default implementations of {@link HttpClientProviderBase} using java standard http client 
 * and {@link JsonSerializationProviderBase} using jackson json library but you can make own ones and provide them in builder.
 * <br/><br/>
 * <b>All API endpoints implementations should be registered using {@link #registerEndpoint(Class) registerEndpoint} before using.</b>
 * 
 * @author Master-chan
 *
 */
@Builder
public class OsuApi
{
	
	@Getter private final int version = 2;
	
	private final Map<Class<? extends Endpoint<?, ?>>, Endpoint<?, ?>> endpoints = new HashMap<>();
	private final ReadWriteLock endpointsLock = new ReentrantReadWriteLock();

	@Getter(AccessLevel.PACKAGE) private final long clientId;
	@Getter(AccessLevel.PACKAGE) private final String clientSecret;
	
	@Builder.Default @Getter private final String baseUrl = "https://osu.ppy.sh";
	@Builder.Default @Getter private final HttpClientProviderBase httpClientProvider = new JavaHttpClientProvider();
	@Builder.Default @Getter private final JsonSerializationProviderBase jsonSerializationProvider = new JacksonSerializationProvider();
	private final AuthHandler authHandler = new AuthHandler();
	
	/**
	 * Registers endpoint in this API instance.
	 * @param endpointClass - Class extending Endpoint
	 * @throws EndpointRegistrationException - if exception was thrown during registration.
	 */
	public void registerEndpoint(Class<? extends Endpoint<?, ?>> endpointClass) throws EndpointRegistrationException
	{
		endpointsLock.writeLock().lock();
		try
		{
			Endpoint<?, ?> endpointObject = (Endpoint<?, ?>) endpointClass.getConstructors()[0].newInstance(baseUrl, httpClientProvider, jsonSerializationProvider);
			endpoints.put(endpointClass, endpointObject);
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
		{
			throw new EndpointRegistrationException(e);
		}
		finally
		{
			endpointsLock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	private <R, R1> Endpoint<R, R1> getEndpointInstance(Class<? extends Endpoint<R, R1>> endpointClass)
	{
		endpointsLock.readLock().lock();
		try
		{
			return (Endpoint<R, R1>) endpoints.get(endpointClass);
		}
		finally
		{
			endpointsLock.readLock().unlock();
		}
	}
	
	/**
	 * Forcifully (re)generates current auth token. You probably shouldn't use this method unless you want to avoid lazy loading and refreshing.
	 * @return new auth token
	 */
	public String regenerateAuthToken() throws IOException
	{
		return authHandler.updateAuthToken(this);
	}

	/**
	 * Makes a request to provided endpoint class.
	 * @param endpointClass - class of endpoint. Should be registered with {@link #registerEndpoint(Class) registerEndpoint} before usage.
	 * @param request - request body object.
	 * @return Response object.
	 * @throws EndpointNotRegisteredException endpoint with provided class wasn't registered.
	 * @throws IOException propagates any IO exceptions from http and serialization providers. IOExceptions with code 404 on default HTTP provider usually indicate that requested entity (for example player) wasn't found.
	 */
	public <R, R1> R1 request(Class<? extends Endpoint<R, R1>> endpointClass, R request) throws EndpointNotRegisteredException, IOException
	{
		Endpoint<R, R1> endpoint = getEndpointInstance(endpointClass);
		if(endpoint == null) { throw new EndpointNotRegisteredException(endpointClass); }
		String authToken = authHandler.getAuthToken(this);
		R1 value = endpoint.request(request, authToken);
		return value;
	}

}
