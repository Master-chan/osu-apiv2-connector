package org.osumm.apiv2;

import java.io.IOException;

import org.osumm.apiv2.impl.JacksonSerializationProvider;

/**
 * json serialization provider interface.
 * @author Master-chan
 * @see {@link JacksonSerializationProvider}
 */
public interface JsonSerializationProviderBase
{
	
	/**
	 * Deserializes object from json string.
	 * @param data - json string
	 * @param type - object class
	 */
	public <T> T fromJson(String data, Class<T> type) throws IOException;
	
	/**
	 * Serializes object to json string.
	 * @param data - object
	 */
	public String toJson(Object data) throws IOException;
	
}
