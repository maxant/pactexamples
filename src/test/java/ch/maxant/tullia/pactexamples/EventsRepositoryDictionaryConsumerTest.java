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

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Another example of a consumer test.<br>
 * This one resembles {@link EventsRepository2ConsumerTest}.
 */
public class EventsRepositoryDictionaryConsumerTest {

    private static final Integer PORT = 8092;

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("EventsProvider", "localhost", PORT, this);

    @Pact(provider = "EventsProvider", consumer = "EventsConsumerDictionary")
    public PactFragment createFragment(PactDslWithProvider builder) {

        LoggerFactory.getLogger(this.getClass()).info("creating fragement...");

        DslPart body = new PactDslJsonBody()
                //key is dynamic (i.e. think dictionary or java map
                //see https://github.com/DiUS/pact-jvm/issues/313
                //see https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-consumer-junit
                .eachKeyLike("ant")
                  .stringType("title", "ant")
                  //we dont care about other attributes here. neither does pact :-)
                ;

        return builder
                .given("initialStateForEventsTest")
                .uponReceiving("a request to get events keyed by title")
                .path("/dictionary")
                .headers("Connection", "Keep-Alive", "Content-Length", "0", "Accept", "application/json", "User-Agent", "Apache-HttpClient/4.5.2 (Java/1.8.0_60)")
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
        Map<String, Event> events = new EventsRepository("http://localhost:" + PORT).getEventsMap();
        assertEquals(1, events.size());
        assertEquals("ant", events.get("ant").getTitle());
    }


}
