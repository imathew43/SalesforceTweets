package rest.client.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imathew on 6/9/16.
 */
public class TwitterTimelineResponse {
    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("count")
    public int count;
}
