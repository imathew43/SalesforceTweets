package rest.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;
import rest.client.entity.TestEntities;
import rest.client.entity.TwitterAuthorizationResponse;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javax.ws.rs.core.GenericType;

/**
 * Created by imathew on 6/13/16.
 */
public class TwitterApiTest {

    @Mock
    private WebTarget webTargetMock;

    @Mock
    private Response responseMock;

    @Mock
    private Invocation invocationMock;

    @Mock
    private Invocation.Builder invocationBuilderMock;

    @InjectMocks
    private TwitterApi twitterApi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when (webTargetMock.path(anyString())).thenReturn(webTargetMock);
        when(webTargetMock.request(anyString())).thenReturn(invocationBuilderMock);
        when (webTargetMock.request()).thenReturn(invocationBuilderMock);
        when (webTargetMock.queryParam(any(),any())).thenReturn(webTargetMock);
        when(invocationBuilderMock.buildPost(any())).thenReturn(invocationMock);
        when(invocationBuilderMock.buildGet()).thenReturn(invocationMock);
        when(invocationBuilderMock.header(anyString(),anyString())).thenReturn(invocationBuilderMock);
        when(invocationMock.invoke()).thenReturn(responseMock);
        when (responseMock.getStatusInfo()).thenReturn(Response.Status.OK);
        when (webTargetMock.getUri()).thenReturn(URI.create("testUri"));
    }

    @Test
    public void testAuthorizationFailStatusCode() {
        when (responseMock.getStatusInfo()).thenReturn(Response.Status.BAD_REQUEST);
        assertFalse(twitterApi.authenticate("aaa","bbb"));
    }

    @Test
    public void testAuthorizationFailBadType() {
        when (responseMock.readEntity(TwitterAuthorizationResponse.class)).thenReturn(TestEntities.getTestBadTypeAuthResponse());
        assertFalse(twitterApi.authenticate("aaa","bbb"));
    }

    @Test
    public void testAuthorizationSuccess() {
        when (responseMock.readEntity(TwitterAuthorizationResponse.class)).thenReturn(TestEntities.getTestSuccessAuthResponse());
        assertTrue(twitterApi.authenticate("aaa","bbb"));

    }

    @Test
    public void testGetTweetsSuccess() throws TweetFetchException {
        when (responseMock.readEntity(any(GenericType.class))).thenReturn(TestEntities.getNonEmptyResponseList());
        when (responseMock.readEntity(TwitterAuthorizationResponse.class)).thenReturn(TestEntities.getTestSuccessAuthResponse());
        assertTrue(twitterApi.authenticate("aaa","bbb"));
        Assert.notEmpty(twitterApi.getTweets("userName",1));
    }

    @Test(expected=TweetFetchException.class)
    public void testGetTweetsFailure() throws TweetFetchException {
        when (responseMock.readEntity(TwitterAuthorizationResponse.class)).thenReturn(TestEntities.getTestSuccessAuthResponse());
        when (responseMock.readEntity(any(GenericType.class))).thenReturn(TestEntities.getNonEmptyResponseList());
        assertTrue(twitterApi.authenticate("aaa","bbb"));
        when (responseMock.getStatusInfo()).thenReturn(Response.Status.BAD_REQUEST);
        twitterApi.getTweets("userName",1);

    }

    @Test
    public void testLargeTweetCount() throws TweetFetchException {
        when (responseMock.readEntity(any(GenericType.class))).thenReturn(TestEntities.getNonEmptyResponseList());
        when (responseMock.readEntity(TwitterAuthorizationResponse.class)).thenReturn(TestEntities.getTestSuccessAuthResponse());
        assertTrue(twitterApi.authenticate("aaa","bbb"));
        assertTrue(twitterApi.getTweets("userName",10).size() == 1);
    }

    @Test
    public void testAuthWillFailWithoutArgsOrParameters() {
        assertFalse(twitterApi.authenticate());
    }




}
