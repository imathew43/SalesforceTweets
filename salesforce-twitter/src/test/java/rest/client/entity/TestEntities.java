package rest.client.entity;

import java.util.ArrayList;
import java.util.List;
import rest.service.entity.Tweet;

/**
 * Created by imathew on 6/13/16.
 */
public class TestEntities {
    public static TwitterAuthorizationResponse getTestSuccessAuthResponse() {
        TwitterAuthorizationResponse tar = new TwitterAuthorizationResponse();
        tar.accessToken = "aaa";
        tar.tokenType = "bearer";
        return tar;
    }

    public static TwitterAuthorizationResponse getTestBadTypeAuthResponse() {
        TwitterAuthorizationResponse tar = new TwitterAuthorizationResponse();
        tar.accessToken = "aaa";
        tar.tokenType = "definitelyNotBearer";
        return tar;
    }

    public static List<Tweet> getNonEmptyResponseList() {
        ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
        Tweet tw= new Tweet();
        tw.retweetCount = 1;
        tw.tweetContent = "Hi there";
        tw.user = new Tweet.User();
        tw.user.name = "nobody";
        tw.user.screenName = "nobody";
        tw.user.profileImageLink = "http://nothing.com/nobody.jpg";
        tweetList.add(tw);
        return tweetList;
    }
}
