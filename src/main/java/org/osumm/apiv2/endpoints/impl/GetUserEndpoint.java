package org.osumm.apiv2.endpoints.impl;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Set;

import org.osumm.apiv2.HttpClientProviderBase;
import org.osumm.apiv2.JsonSerializationProviderBase;
import org.osumm.apiv2.endpoints.Endpoint;
import org.osumm.apiv2.endpoints.impl.GetUserEndpoint.GetUserRequest;
import org.osumm.apiv2.endpoints.impl.GetUserEndpoint.GetUserResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Endpoint for getting user information. Request parameters should contain userId or username and gamemode (optional). API request will always be made using userId if it is present.
 * <br/>
 * <br/>
 * <b>Example:</b>
 * <pre>
 * {@code
 * GetUserResponse user = api.request(GetUserEndpoint.class, GetUserEndpoint.GetUserRequest.builder().username("Anemic Witch").build());
 * }
 * </pre>
 * 
 * @see <a href="https://osu.ppy.sh/docs/index.html?bash#get-user">Official osu!wiki reference</a>
 * @author Master-chan
 *
 */
public class GetUserEndpoint extends Endpoint<GetUserRequest, GetUserResponse>
{
	
	public GetUserEndpoint(String baseUrl, HttpClientProviderBase httpClientProvider,JsonSerializationProviderBase jsonProvider)
	{
		super(baseUrl, httpClientProvider, jsonProvider);
		urlFormat = baseUrl + "/api/v2/users/%s/%s?key=%s";
	}

	private final String urlFormat;

	@Override
	public GetUserResponse request(GetUserRequest paramHolder, String authToken) throws IOException
	{
		String url = String.format(urlFormat, 
				paramHolder.getUserId() >= 1 ? paramHolder.getUserId() : paramHolder.getUsername(), 
				paramHolder.getMode() != null ? paramHolder.getMode() : "",
				paramHolder.getUserId() >= 1 ? "id" : "username");
		String value = getHttpClientProvider().get(new URL(url), 5000, authToken);
		GetUserResponse response = getJsonProvider().fromJson(value, GetUserResponse.class);
		return response;
	}

	public static enum GameMode
	{
		FRUITS, MANIA, OSU, TAIKO;
		
		@Override
		public String toString()
		{
			return name().toLowerCase();
		}
	}
	
	@Getter
	@Builder
	public static class GetUserRequest
	{
		@Builder.Default private final long userId = -1;
		private final GameMode mode;
		private final String username;
	}
	
	@Getter
	@NoArgsConstructor
	public static class GetUserResponse
	{

		private String username;
		private long id;
		private String playmode;
		private String twitter;
		private String website;
		private String location;
		private String discord;
		private Set<ProfileBadge> badges;
		private String interests;
		private String title;
		private ProfileStatistics statistics;
		private ProfileStatisticsKudosu kudosu;
											
		@JsonProperty("avatar_url")			private String avatarUrl;
		@JsonProperty("cover_url")			private String coverUrl;
		@JsonProperty("has_supported")		private boolean hasSupported;
		@JsonProperty("join_date")			private Timestamp joinDate;
		@JsonProperty("last_visit")			private Timestamp lastVisitDate;
		@JsonProperty("user_achievements")	private Set<ProfileAchievement> achievements;
		@JsonProperty("country_code")		private String countryCode;
		@JsonProperty("default_group")		private String defaultGroup;
		@JsonProperty("profile_colour")		private String profileColor;
		@JsonProperty("is_active")			private boolean active;
		@JsonProperty("is_bot")				private boolean bot;
		@JsonProperty("is_deleted")			private boolean deleted;
		@JsonProperty("is_online")			private boolean online;
		@JsonProperty("is_supporter")		private boolean supporter;
		@JsonProperty("pm_friends_only")	private boolean pmFriendsOnly;
		@JsonProperty("support_level")		private long supportLevel;
		
		@JsonProperty("scores_first_count")						private long firstPlaces;
		@JsonProperty("follower_count")							private long followers;
		@JsonProperty("favourite_beatmapset_count")				private long favoriteBeatmapsCount;
		@JsonProperty("unranked_beatmapset_count")				private long unrankedBeatmapsCount;
		@JsonProperty("graveyard_beatmapset_count")				private long graveyeardBeatmapsCount;
		@JsonProperty("loved_beatmapset_count")					private long lovedBeatmapsCount;
		@JsonProperty("ranked_and_approved_beatmapset_count")	private long rankedAndApprovedBeatmapsCount;

		
		@Getter
		@NoArgsConstructor
		public static class ProfileBadge
		{
			private	String description;
			private String url;
			@JsonProperty("awarded_at")	private Timestamp awardedAt;
			@JsonProperty("image_url")	private String imageUrl;
		}
		
		@Getter
		@NoArgsConstructor
		public static class ProfileAchievement
		{
			@JsonProperty("achievement_id")		private long id;
			@JsonProperty("achieved_at")		private Timestamp achievedAt;
		}
		
		@Getter
		@NoArgsConstructor
		public static class ProfileStatisticsKudosu
		{
			private long total;
			private long available;
		}
		
		@Getter
		@NoArgsConstructor
		public static class ProfileStatistics
		{
			private ProfileStatisticsLevel level;
			@JsonProperty("pp")				private double performancePoints;
			@JsonProperty("global_rank")	private long globalRank;
			@JsonProperty("ranked_score")	private long rankedScore;
			@JsonProperty("hit_accuracy")	private double hitAccuracy;
			@JsonProperty("play_count")		private long playcount;
			@JsonProperty("play_time")		private long playtime;
			@JsonProperty("total_score")	private long totalScore;
			@JsonProperty("total_hits")		private long totalHits;
			@JsonProperty("maximum_combo")	private long maximumCombo;
			@JsonProperty("is_ranked")		private boolean ranked;
			@JsonProperty("grade_counts")	private ProfileStatisticsGrades grades;
			@JsonProperty("rank")			private ProfileStatisticsRank rank;
			@JsonProperty("replays_watched_by_others")	private long replaysWatchedByOthers;
			
			@Getter
			@NoArgsConstructor
			public static class ProfileStatisticsLevel
			{
				private long current;
				private double progress;
			}
			@Getter
			@NoArgsConstructor
			public static class ProfileStatisticsGrades
			{
				@JsonProperty("s")		private long sRanks;
				@JsonProperty("ss")		private long ssRanks;
				@JsonProperty("sh")		private long shRanks;
				@JsonProperty("ssh")	private long sshRanks;
				@JsonProperty("a")		private long aRanks;
			}
			@Getter
			@NoArgsConstructor
			public static class ProfileStatisticsRank
			{
				private long global;
				private long country;
			}
		}
	}
}