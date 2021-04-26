package org.osumm.apiv2.impl;

import java.io.IOException;

import org.osumm.apiv2.JsonSerializationProviderBase;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializationProvider implements JsonSerializationProviderBase
{

	private Gson gson;
	
	public GsonSerializationProvider()
	{
		gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}
	
	@Override
	public <T> T fromJson(String data, Class<T> type) throws IOException
	{
		return gson.fromJson(data, type);
	}

	@Override
	public String toJson(Object data) throws IOException
	{
		return gson.toJson(data);
	}
}
