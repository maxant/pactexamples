package ch.maxant.tullia.pactexamples;

import au.com.dius.pact.provider.ProviderInfo;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(PactRunner.class) // Say JUnit to run tests with custom Runner
@Provider("events provider") // Set up name of tested provider
@PactFolder("pacts") // Point where to find pacts (See also section Pacts source in documentation)
@VerificationReports({"console", "markdown"})
public class ContractTest {

    private static ProviderInfo serviceProvider;

    @BeforeClass //Method will be run once: before whole contract test suite
    public static void setUpService() {
        //Run DB, create schema
        //Run service
        //...
    }

    @Before //Method will be run before each test of interaction
    public void before() {
        // Rest data
        // Mock dependent service responses
        // ...
    }

//    @State("default", "no-data") // Method will be run before testing interactions that require "default" or "no-data" state
    public void toDefaultState() {
        // Prepare service before interaction that require "default" state
        // ...
        System.out.println("Now service in default state");
    }

}