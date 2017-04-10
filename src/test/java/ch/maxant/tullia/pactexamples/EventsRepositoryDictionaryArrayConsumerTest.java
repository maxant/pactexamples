package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Another example of a consumer test.<br>
 * This one resembles {@link EventsRepository2ConsumerTest}.
 */
public class EventsRepositoryDictionaryArrayConsumerTest {

    private static final Integer PORT = 8092;

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("EventsProvider", "localhost", PORT, this);

    @Pact(provider = "EventsProvider", consumer = "EventsConsumerDictionaryArray")
    public PactFragment createFragment(PactDslWithProvider builder) {

        LoggerFactory.getLogger(this.getClass()).info("creating fragement...");

        DslPart body = new PactDslJsonBody()
                //key is dynamic (i.e. think dictionary or java map
                //see https://github.com/DiUS/pact-jvm/issues/313
                //see https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-consumer-junit
                .eachKeyMappedToAnArrayLike("ant")
                  .stringType("title", "ant")
                  //we dont care about other attributes here. neither does pact :-)
                ;

        return builder
                .given("initialStateForEventsTest")
                .uponReceiving("a request to get events keyed by title")
                .path("/dictionaryArray")
                .headers("Accept", MediaType.APPLICATION_JSON) //content-type is what we send, accept is what we receive
                .method("GET")
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json; charset=utf-8") //very important, otherwise resteasy/jackson cant deserialise
                .body(body)
                .toFragment();
    }

    @Test
    @PactVerification(value = "EventsProvider")
    public void runTest() {
        Map<String, List<Event>> events = new EventsRepository("http://localhost:" + PORT).getEventsMapArray();
        assertEquals(1, events.size());
        assertEquals("ant", events.get("ant").get(0).getTitle());
    }


}
