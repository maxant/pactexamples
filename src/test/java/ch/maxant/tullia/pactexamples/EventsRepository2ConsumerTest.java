package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.junit.Assert.assertEquals;

/** Another example of a consumer test.<br>
 * This one uses annotions. */
public class EventsRepository2ConsumerTest {

    private static final Integer PORT = 8092;

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("EventsProvider", "localhost", PORT, this);

    @Pact(provider="EventsProvider", consumer="EventsConsumer2")
    public PactFragment createFragment(PactDslWithProvider builder) {

        DslPart body = PactDslJsonArray.arrayEachLike()
                .stringMatcher("date", "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\.[0-9][0-9][0-9]", "2015-12-25T12:00:00.000")
                .stringMatcher("title", ".*", "harry")
                .stringMatcher("location", ".*") //example of no default value given to pact
                .closeObject();

        return builder
                .given("initialStateForEventsTest")
                .uponReceiving("a request to get events using annotations")
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

    //TODO would be kinda cool if we didnt have to use a constant for the port, and if Pact could tell us the URL its using for its local server...
    @Test
    @PactVerification(value = "EventsProvider")
    public void runTest() {
        List<Event> events = new EventsRepository("http://localhost:" + PORT).getEvents();
        assertEquals(1, events.size());
        assertEquals("harry", events.get(0).getTitle());
    }


}
