package rest.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import rest.client.AuthorizationException;

/**
 * Created by imathew on 6/8/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TwitterAuthorizationResponse {

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("access_token")
    public String accessToken;

    public String getAccessToken() throws AuthorizationException {
        if (tokenType == null || !tokenType.equals("bearer")) {
            throw new AuthorizationException("Null or incorrect token type: " + tokenType);
        } else if (accessToken == null) {
            throw new AuthorizationException("Null access token.");
        }
        return accessToken;
    }
}
