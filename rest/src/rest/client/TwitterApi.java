package rest.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import com.

/**
 * Created by imathew on 6/7/16.
 */
public class TwitterApi {
    String bearerToken;
    private static final String ENCODING_TYPE = "UTF-8";

    public boolean authenticate(String consumerKey, String consumerSecret) {
        try {
            //1.1 urlencode each value.
            consumerKey = URLEncoder.encode(consumerKey, ENCODING_TYPE);
            consumerSecret = URLEncoder.encode(consumerSecret, ENCODING_TYPE);

            //1.2 concatenate key and secret.
            String concatenatedConsumer = consumerKey + ":" + consumerSecret;

            //1.3 Base64 encode.
            String encodedConsumer = Base64.getEncoder().encodeToString(concatenatedConsumer.getBytes());

            //2.

        } catch (UnsupportedEncodingException uee) {
            System.out.println("Unsupported encoding: " + ENCODING_TYPE);
        }
        return false;
    }


}
