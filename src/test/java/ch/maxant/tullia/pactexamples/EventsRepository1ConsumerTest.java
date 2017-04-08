package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.ConsumerPactTest;
import au.com.dius.pact.consumer.PactError;
import au.com.dius.pact.consumer.VerificationResult;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.PactFragment;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.junit.Assert.assertEquals;

/** An example of a consumer test. */
public class EventsRepository1ConsumerTest {

    @Test
    public void testPact() {
        DslPart body = PactDslJsonArray.arrayEachLike()
                .stringMatcher("date", "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\.[0-9][0-9][0-9]", "2015-12-25T12:00:00.000")
                .stringMatcher("title", ".*", "harry")
                .stringMatcher("location", ".*") //example of no default value given to pact
                .closeObject();

        PactFragment pactFragment = ConsumerPactBuilder
                .consumer("EventsConsumer1")
                .hasPactWith("EventsProvider")
                .given("initialStateForEventsTest")
                .uponReceiving("a request to get events using the fragment to run the consumer")
                .path("/all")
                .headers("Content-Type", MediaType.APPLICATION_JSON, "Accept", MediaType.APPLICATION_JSON) //content-type is what we send, accept is what we receive
                .method("POST")
                .body("{\"someField\": \"asdf\"}")
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json; charset=utf-8") //very important, otherwise resteasy/jackson cant deserialise
                .body(body)
                .toFragment();

        MockProviderConfig config = MockProviderConfig.createDefault();
        VerificationResult result = pactFragment.runConsumer(config, c -> {
            List<Event> events = new EventsRepository(c.url()).getEvents();
            assertEquals(1, events.size());
            assertEquals("harry", events.get(0).getTitle());
        });

        if (result instanceof PactError) {
            throw new RuntimeException(((PactError)result).error());
        }

        assertEquals(ConsumerPactTest.PACT_VERIFIED, result);
    }


}
