package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.junit.runner.RunWith;

@RunWith(PactRunner.class) // Say JUnit to run tests with custom Runner
@Provider("EventsProvider") // Set up name of tested provider
@PactFolder("pacts") // Point where to find pacts (See also section Pacts source in documentation)
@VerificationReports({"console", "markdown", "json"})
public class ProviderTest {

    @State("initialStateForEventsTest") // Method will be run before testing interactions that require "initialStateForEventsTest" state
    public void setState() {
        // Prepare service before interaction that require "default" state
        // ...
        System.out.println("Now service in default state");
    }

    @TestTarget // Annotation denotes Target that will be used for tests
    public final Target target = new HttpTarget("http", "localhost", 8091); // Out-of-the-box implementation of Target (for more information take a look at Test Target section)


}