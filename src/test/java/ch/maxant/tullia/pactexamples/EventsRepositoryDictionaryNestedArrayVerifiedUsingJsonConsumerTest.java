package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * to get around https://github.com/DiUS/pact-jvm/issues/401, lets just verify using the cut down json that we really need.
 * means that the test is somewhat data dependent, but it still checks the structure!
 */
public class EventsRepositoryDictionaryNestedArrayVerifiedUsingJsonConsumerTest {

    private static final Integer PORT = 8092;

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("EventsProvider", "localhost", PORT, this);

    @Pact(provider = "EventsProvider", consumer = "EventsConsumerDictionaryNestedArrayVerifiedUsingJson")
    public PactFragment createFragment(PactDslWithProvider builder) {

        //this json only contains the fields that we are really interested in!!
        String json = "{\n" +
                "        \"events\": {\n" +
                "            \"tant\": [\n" +
                "                {\n" +
                "                    \"title\": \"ant\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n"; //TODO would be nicer to load this from a file :-)

        return builder
            .given("initialStateForEventsTest")
            .uponReceiving("a request to get events keyed by title")
            .path("/dictionaryNestedArray")
            .headers("Accept", MediaType.APPLICATION_JSON) //content-type is what we send, accept is what we receive
            .method("GET")
            .willRespondWith()
            .status(200)
            .matchHeader("Content-Type", "application/json; charset=utf-8") //very important, otherwise resteasy/jackson cant deserialise
            .body(json)
            .toFragment();
    }

    @Test
    @PactVerification(value = "EventsProvider")
    public void runTest() {
        Map<String, Map<String, List<Event>>> events = new EventsRepository("http://localhost:" + PORT).getEventsMapNestedArray();
        assertEquals(1, events.size());
        assertEquals("ant", events.get("events").get("tant").get(0).getTitle());
    }

}
