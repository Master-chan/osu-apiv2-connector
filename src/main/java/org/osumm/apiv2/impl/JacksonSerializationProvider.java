package org.osumm.apiv2.impl;

import java.io.IOException;

import org.osumm.apiv2.JsonSerializationProviderBase;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Default serialization provider using jackson json library.
 * @author Master-chan
 *
 */
public class JacksonSerializationProvider implements JsonSerializationProviderBase
{
	
	private final ObjectMapper jsonMapper;
	
	public JacksonSerializationProvider()
	{
		jsonMapper = new ObjectMapper(new JsonFactory());
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonMapper.findAndRegisterModules();
	}

	@Override
	public <T> T fromJson(String data, Class<T> type) throws IOException
	{
		return jsonMapper.readValue(data, type);
	}

	@Override
	public String toJson(Object data) throws IOException
	{
		return jsonMapper.writeValueAsString(data);
	}
}
