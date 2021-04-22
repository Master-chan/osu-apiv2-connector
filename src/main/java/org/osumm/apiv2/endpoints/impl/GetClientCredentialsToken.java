package org.osumm.apiv2.endpoints.impl;

import java.io.IOException;
import java.net.URL;

import org.osumm.apiv2.HttpClientProviderBase;
import org.osumm.apiv2.JsonSerializationProviderBase;
import org.osumm.apiv2.endpoints.Endpoint;
import org.osumm.apiv2.endpoints.impl.GetClientCredentialsToken.ClientCredentialsRequest;
import org.osumm.apiv2.endpoints.impl.GetClientCredentialsToken.ClientCredentialsResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * APIv2 authorization flow
 * @author Master-chan
 * @see <a href="https://osu.ppy.sh/docs/index.html?bash#client-credentials-grant">Official osu!wiki reference</a>
 */
public class GetClientCredentialsToken extends Endpoint<ClientCredentialsRequest, ClientCredentialsResponse>
{
	
	private final String urlFormat;
	
	public GetClientCredentialsToken(String baseUrl, HttpClientProviderBase httpClientProvider, JsonSerializationProviderBase jsonProvider)
	{
		super(baseUrl, httpClientProvider, jsonProvider);
		urlFormat = baseUrl + "/oauth/token";
	}
	
	@Override
	public ClientCredentialsResponse request(ClientCredentialsRequest paramHolder, String authToken) throws IOException
	{
		return getJsonProvider()
				.fromJson(
						getHttpClientProvider().post(new URL(urlFormat), getJsonProvider().toJson(paramHolder), 5000), 
						ClientCredentialsResponse.class);
	}

	@Getter
	@RequiredArgsConstructor
	public static class ClientCredentialsRequest
	{
		@JsonProperty("client_id")		private final long clientId;
		@JsonProperty("client_secret")	private final String clientSecret;
		@JsonProperty("grant_type")		private String grantType = "client_credentials";
		@JsonProperty("scope")			private String scope = "public"; // Might change to "bot" for bot accounts in the future
	}
	
	@Getter
	@NoArgsConstructor
	public static class ClientCredentialsResponse
	{
		@JsonProperty("token_type")		private String tokenType;
		@JsonProperty("expires_in")		private long expiresIn;
		@JsonProperty("access_token")	private String accessToken;
	}

}
