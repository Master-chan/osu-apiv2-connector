package org.osumm.apiv2.endpoints;

public class EndpointRegistrationException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3055010872251388638L;
	
	public EndpointRegistrationException(Throwable cause)
	{
		super("Exception in endpoint registration:", cause);
	}
}
