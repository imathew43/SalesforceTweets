package rest.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imathew on 6/9/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Tweet {

    @JsonProperty("text")
    public String tweetContent;

    @JsonProperty("retweet_count")
    public int retweetCount;

    @JsonProperty("user")
    public User user;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class User {
        @JsonProperty("name")
        public String name;

        @JsonProperty("screen_name")
        public String screenName;

        @JsonProperty("profile_image_url")
        public String profileImageLink;

    }
}
