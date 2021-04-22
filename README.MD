# osu! API v2 Java Connector

## Building:

_Clone, build and install artifact into your local maven repo:_
```bash
git clone https://github.com/Master-chan/osu-apiv2-connector.git
cd osu-apiv2-connector
mvn clean install
```

_Include maven dependency:_
```xml
    <dependency>
        <groupId>org.osumm</groupId>
        <artifactId>osu-apiv2-connector</artifactId>
        <version>0.1-SNAPSHOT</version>
    </dependency>
```

## Usage:
1. Go to https://osu.ppy.sh/home/account/edit create new OAuth application, copy client id and secret.

2. Initialize API object:
```java
OsuApi api = OsuApi.builder()
		.clientId(<your client id>)
		.clientSecret("<your client secret")
		.build();

// Register default getUser endpoint:
api.registerEndpoint(GetUserEndpoint.class);
```

3. Grab user data:
```java
GetUserResponse user = api.request(GetUserEndpoint.class, GetUserRequest.builder().username("Anemic Witch").build());
System.out.printf("%s (id: %d), Rank #%s (%.1fpp) Badges: %d\n", 
		user.getUsername(), user.getId(), user.getStatistics().getGlobalRank(), user.getStatistics().getPerformancePoints(), user.getBadges().size());
```

There aren't any other endpoints implemented yet and I don't really plan to until APIv2 becomes main one.

## Adding endpoints:

Endpoints are designed to accept parameters and return values wrapped as immutable java objects.
You can easily make own endpoint handlers by extending `Endpoint` class. Look at `GetUserEndpoint` for examples.

## Overriding default http and json providers

By default this library uses java http client and jackson json library but you can provide your own implementations with `HttpClientProviderBase` and `JsonSerializationProviderBase`.

<ins>**Note that default `GetUserEndpoint` uses jackson specific annotations for field naming.**</ins>