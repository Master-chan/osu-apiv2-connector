package org.osumm.apiv2.endpoints;

import lombok.Getter;

public class EndpointNotRegisteredException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6195970793024949575L;
	
	@Getter private final Class<? extends Endpoint<?,?>> endpoint;
	

	public EndpointNotRegisteredException(Class<? extends Endpoint<?,?>> endpoint)
	{
		super("Endpoint class not registered: " + endpoint.getCanonicalName());
		this.endpoint = endpoint;
	}
	
}
