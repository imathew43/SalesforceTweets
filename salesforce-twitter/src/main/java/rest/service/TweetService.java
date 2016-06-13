package rest.service;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.servlet.DispatcherServlet;
import rest.client.AuthorizationException;
import rest.client.TweetFetchException;
import rest.client.TwitterApi;
import rest.service.entity.Tweet;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import java.util.List;


/**
 * Created by imathew on 6/10/16.
 */

@Controller
@EnableAutoConfiguration
public class TweetService {

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/twitter/getTweets/{screen_name}/{tweet_count}", method = RequestMethod.GET)
    @ResponseBody
    List<Tweet> home(@PathVariable("screen_name") String screenName, @PathVariable("tweet_count") String tweetCount) {
        try {
            int numberOfTweets = Integer.parseInt(tweetCount);
            return TwitterApi.getInstance().getTweets(screenName,numberOfTweets);
        } catch (AuthorizationException ae) {
            throw new WebApplicationException("Problem authenticating: " + ae.getMessage());
        }
        catch (TweetFetchException tfe) {
            throw new WebApplicationException("Could not fetch tweets: " + tfe.getMessage());
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Tweet count must be a number... you put:" + tweetCount);
        }
    }


    public static void main(String[] args) throws Exception {
       SpringApplication.run(TweetService.class,args);
    }
}
