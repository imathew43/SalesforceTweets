package rest.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import rest.client.entity.*;
import rest.service.entity.Tweet;
//import com.

/**
 * Created by imathew on 6/7/16.
 */
public class TwitterApi {
    public String bearerToken;
    private static final String ENCODING_TYPE = "UTF-8";

    private static final String BASE_URI = "https://api.twitter.com";
    private static final String AUTH_PATH = "oauth2/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_PREFIX = "Basic ";
    private static final String AUTH_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";

    private static final String ENV_CONSUMER_SECRET = "twitterConsumerSecret";
    private static final String ENV_CONSUMER_KEY = "twitterConsumerKey";
    private static final String AUTH_POST_BODY = "grant_type=client_credentials";

    private static final String TWEET_AUTH_PREFIX = "Bearer ";
    private static final String SCREEN_NAME = "screen_name";
    private static final String EXCLUDE_REPLIES = "exclude_replies";
    private static final String TWEET_PATH = "1.1/statuses/user_timeline.json";

    private Client client;

    private WebTarget baseTarget;

    private static TwitterApi instance = null;

   /* public static void main(String[] args) {
        TwitterApi inst = getInstance();
        System.out.println(instance.bearerToken);
    }*/

    public static TwitterApi getInstance() {
        if (instance == null) {
            instance = new TwitterApi();
            instance.authenticate();
        }
        return instance;
    }

    private TwitterApi() {
        client = ClientBuilder.newClient();
        baseTarget = client.target(BASE_URI);
    }

    public boolean authenticate() {
        //use environment variables
        if (null == System.getenv(ENV_CONSUMER_KEY) || null == System.getenv(ENV_CONSUMER_SECRET)) {
            return false;
        }
        return authenticate(System.getenv(ENV_CONSUMER_KEY),System.getenv(ENV_CONSUMER_SECRET));
    }

    public boolean authenticate(String consumerKey, String consumerSecret) {
        //https://dev.twitter.com/oauth/application-only
        try {
            //1.1 urlencode each value.
            consumerKey = URLEncoder.encode(consumerKey, ENCODING_TYPE);
            consumerSecret = URLEncoder.encode(consumerSecret, ENCODING_TYPE);

            //1.2 concatenate key and secret.
            String concatenatedConsumer = consumerKey + ":" + consumerSecret;

            //1.3 Base64 encode.
            String encodedConsumer = Base64.getEncoder().encodeToString(concatenatedConsumer.getBytes());

            //2.
            //client.
            WebTarget target = baseTarget.path(AUTH_PATH);

            String authString = AUTH_PREFIX + encodedConsumer;
            ObjectMapper om = new ObjectMapper();
            Invocation request = target.request(AUTH_CONTENT_TYPE).header(AUTH_HEADER,authString).buildPost(Entity.entity(AUTH_POST_BODY,AUTH_CONTENT_TYPE));
            Response response = request.invoke();
            if (response.getStatusInfo().getStatusCode() == 200) {
                //System.out.println("Success: " + response.readEntity(String.class));
                TwitterAuthorizationResponse tar = response.readEntity(TwitterAuthorizationResponse.class);
                bearerToken = tar.getAccessToken();
                return true;
            } else {
                System.out.println("Status code for auth: " + response.getStatusInfo().getStatusCode() + ": " + response.getStatusInfo().getReasonPhrase());
                System.out.println(response.readEntity(String.class));
            }
        } catch (UnsupportedEncodingException uee) {
            System.out.println("Unsupported encoding: " + ENCODING_TYPE);
        } catch (AuthorizationException ae) {
            System.out.println(ae.getMessage());
        }
        return false;
    }

    public List<Tweet> getTweets(String userName, int tweetCount) throws TweetFetchException {
        WebTarget target = baseTarget.path(TWEET_PATH).queryParam(SCREEN_NAME,userName).queryParam(EXCLUDE_REPLIES,"true");
        String authString = TWEET_AUTH_PREFIX + bearerToken;
        Invocation request = target.request().header(AUTH_HEADER, authString).buildGet();
        System.out.println("Web request: " + target.getUri().toString());
        Response response = request.invoke();
        if (response.getStatusInfo().getStatusCode() == 200) {
            List<Tweet> tweets = response.readEntity(new GenericType<List<Tweet>>() {});
            if (tweetCount >= tweets.size()) {
                tweetCount = tweets.size() -1;
            }
            tweets = tweets.subList(0, tweetCount);
            return tweets;
        } else {
            System.out.println(response.readEntity(String.class));
            throw new TweetFetchException("Could net fetch tweets(" + response.getStatusInfo().getStatusCode() + ")");
        }


    }

}
