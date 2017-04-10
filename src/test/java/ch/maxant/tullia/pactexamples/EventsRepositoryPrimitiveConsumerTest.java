package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

/**
 * this one simply shows how to write a pact which reads a number from a microservice.
 */
public class EventsRepositoryPrimitiveConsumerTest {

    private static final Integer PORT = 8092;

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("EventsProvider", "localhost", PORT, this);

    @Pact(provider = "EventsProvider", consumer = "EventsConsumerPrimitive")
    public PactFragment createFragment(PactDslWithProvider builder) {

        DslPart body = PactDslJsonRootValue.integerType(3);

        return builder
            .given("initialStateForEventsTest")
            .uponReceiving("a request to get a number")
            .path("/primitive")
            .headers("Accept", MediaType.APPLICATION_JSON)
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
        int num = new EventsRepository("http://localhost:" + PORT).getPrimitive();
        assertEquals(3, num);
    }

}
