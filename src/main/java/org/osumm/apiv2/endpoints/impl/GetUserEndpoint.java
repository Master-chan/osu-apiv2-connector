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
import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.EqualsAndHashCode;
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
		if(paramHolder.getUserId() < 1 && paramHolder.getUsername() == null)
		{
			throw new IllegalArgumentException("User request should have non-null username or valid userId (>=1)");
		}
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
	@EqualsAndHashCode
	@Builder
	public static class GetUserRequest
	{

		@Builder.Default private final long userId = -1;
		private final GameMode mode;
		private final String username;
	}

	@Getter
	@EqualsAndHashCode
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
		@JsonProperty("avatar_url") @SerializedName("avatar_url")
		private String avatarUrl;
		@JsonProperty("cover_url") @SerializedName("cover_url")
		private String coverUrl;
		@JsonProperty("has_supported") @SerializedName("has_supported")
		private boolean hasSupported;
		@JsonProperty("join_date") @SerializedName("join_date")
		private Timestamp joinDate;
		@JsonProperty("last_visit") @SerializedName("last_visit")
		private Timestamp lastVisitDate;
		@JsonProperty("user_achievements") @SerializedName("user_achievements")
		private Set<ProfileAchievement> achievements;
		@JsonProperty("country_code") @SerializedName("country_code")
		private String countryCode;
		@JsonProperty("default_group") @SerializedName("default_group")
		private String defaultGroup;
		@JsonProperty("profile_colour") @SerializedName("profile_colour")
		private String profileColor;
		@JsonProperty("is_active") @SerializedName("is_active")
		private boolean active;
		@JsonProperty("is_bot") @SerializedName("is_bot")
		private boolean bot;
		@JsonProperty("is_deleted") @SerializedName("is_deleted")
		private boolean deleted;
		@JsonProperty("is_online") @SerializedName("is_online")
		private boolean online;
		@JsonProperty("is_supporter") @SerializedName("is_supporter")
		private boolean supporter;
		@JsonProperty("pm_friends_only") @SerializedName("pm_friends_only")
		private boolean pmFriendsOnly;
		@JsonProperty("support_level") @SerializedName("support_level")
		private long supportLevel;
		@JsonProperty("scores_first_count") @SerializedName("scores_first_count")
		private long firstPlaces;
		@JsonProperty("follower_count") @SerializedName("follower_count")
		private long followers;
		@JsonProperty("favourite_beatmapset_count") @SerializedName("favourite_beatmapset_count")
		private long favoriteBeatmapsCount;
		@JsonProperty("unranked_beatmapset_count") @SerializedName("unranked_beatmapset_count")
		private long unrankedBeatmapsCount;
		@JsonProperty("graveyard_beatmapset_count") @SerializedName("graveyard_beatmapset_count")
		private long graveyeardBeatmapsCount;
		@JsonProperty("loved_beatmapset_count") @SerializedName("loved_beatmapset_count")
		private long lovedBeatmapsCount;
		@JsonProperty("ranked_and_approved_beatmapset_count") @SerializedName("ranked_and_approved_beatmapset_count")
		private long rankedAndApprovedBeatmapsCount;

		@Getter
		@EqualsAndHashCode
		@NoArgsConstructor
		public static class ProfileBadge
		{

			private String description;
			private String url;
			@JsonProperty("awarded_at") @SerializedName("awarded_at")
			private Timestamp awardedAt;
			@JsonProperty("image_url") @SerializedName("image_url")
			private String imageUrl;
		}

		@Getter
		@EqualsAndHashCode
		@NoArgsConstructor
		public static class ProfileAchievement
		{

			@JsonProperty("achievement_id") @SerializedName("achievement_id")
			private long id;
			@JsonProperty("achieved_at") @SerializedName("achieved_at")
			private Timestamp achievedAt;
		}

		@Getter
		@EqualsAndHashCode
		@NoArgsConstructor
		public static class ProfileStatisticsKudosu
		{

			private long total;
			private long available;
		}

		@Getter
		@EqualsAndHashCode
		@NoArgsConstructor
		public static class ProfileStatistics
		{

			private ProfileStatisticsLevel level;
			@JsonProperty("pp") @SerializedName("pp")
			private double performancePoints;
			@JsonProperty("global_rank") @SerializedName("global_rank")
			private long globalRank;
			@JsonProperty("ranked_score") @SerializedName("ranked_score")
			private long rankedScore;
			@JsonProperty("hit_accuracy") @SerializedName("hit_accuracy")
			private double hitAccuracy;
			@JsonProperty("play_count") @SerializedName("play_count")
			private long playcount;
			@JsonProperty("play_time") @SerializedName("play_time")
			private long playtime;
			@JsonProperty("total_score") @SerializedName("total_score")
			private long totalScore;
			@JsonProperty("total_hits") @SerializedName("total_hits")
			private long totalHits;
			@JsonProperty("maximum_combo") @SerializedName("maximum_combo")
			private long maximumCombo;
			@JsonProperty("is_ranked") @SerializedName("is_ranked")
			private boolean ranked;
			@JsonProperty("grade_counts") @SerializedName("grade_counts")
			private ProfileStatisticsGrades grades;
			@JsonProperty("rank") @SerializedName("rank")
			private ProfileStatisticsRank rank;
			@JsonProperty("replays_watched_by_others") @SerializedName("replays_watched_by_others")
			private long replaysWatchedByOthers;

			@Getter
			@EqualsAndHashCode
			@NoArgsConstructor
			public static class ProfileStatisticsLevel
			{

				private long current;
				private double progress;
			}

			@Getter
			@EqualsAndHashCode
			@NoArgsConstructor
			public static class ProfileStatisticsGrades
			{
				@JsonProperty("s") @SerializedName("s")
				private long sRanks;
				@JsonProperty("ss") @SerializedName("ss")
				private long ssRanks;
				@JsonProperty("sh") @SerializedName("sh")
				private long shRanks;
				@JsonProperty("ssh") @SerializedName("ssh")
				private long sshRanks;
				@JsonProperty("a") @SerializedName("a")
				private long aRanks;
			}

			@Getter
			@EqualsAndHashCode
			@NoArgsConstructor
			public static class ProfileStatisticsRank
			{

				private long global;
				private long country;
			}
		}
	}
}