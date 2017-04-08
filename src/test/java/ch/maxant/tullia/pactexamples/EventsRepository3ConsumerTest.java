package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.consumer.ConsumerPactTest;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/** Yet another example of a consumer test. <br>
 * This one uses the abstract {@link ConsumerPactTest} from Pact. */
public class EventsRepository3ConsumerTest extends ConsumerPactTest {

    @Override
    public PactFragment createFragment(PactDslWithProvider builder) {

        DslPart body = PactDslJsonArray.arrayEachLike()
                .stringMatcher("date", "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\.[0-9][0-9][0-9]", "2015-12-25T12:00:00.000")
                .stringMatcher("title", ".*", "harry")
                .stringMatcher("location", ".*") //example of no default value given to pact
                .closeObject();

        return builder
                .given("initialStateForEventsTest")
                .uponReceiving("a request to get events using the frameworks abstract class")
                .path("/all")
                .headers("Content-Type", MediaType.APPLICATION_JSON, "Accept", MediaType.APPLICATION_JSON) //content-type is what we send, accept is what we receive
                .method("POST")
                .body("{\"someField\": \"asdf\"}")
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json; charset=utf-8") //very important, otherwise resteasy/jackson cant deserialise
                .body(body)
                .toFragment();
    }

    @Override
    protected String providerName() {
        return "EventsProvider";
    }

    @Override
    protected String consumerName() {
        return "EventsConsumer3";
    }

    @Override
    protected void runTest(String url) throws IOException {
        List<Event> events = new EventsRepository(url).getEvents();
        assertEquals(1, events.size());
        assertEquals("harry", events.get(0).getTitle());
    }
}
