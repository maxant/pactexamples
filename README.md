# What is this?

A playground for a JAX-RS Client and Pact examples.

It contains a server (app.js, a simple Node/Express application) which answers with static 
responses, as well as a number of consumer tests for ensuring that the consumer maps correctly
and generates the pact, as well as a single provider tests for executing those pacts against
the Node server.

## Starting the demo server

This server runs using Node. 

- `npm install`
- `npm start`

Now the server will be running on port 8091.

To see what the server does, see `app.js`.

## Pacts

This project contains a number of consumer tests:

- EventsConsumer1 - tests an array of objects, using no annotations or abstract classes from Pact
- EventsConsumer2 - ditto, using annotations
- EventsConsumer3 - ditto, using abstract class from Pact

These three tests create JSONs under `target/pacts`. These should be copied
into `src/test/resources/pacts`. They are used by `ProviderTest` to check
the server is adhering to the contract.

Other consumer tests also exist, to test more complex cases.

COPY THE PACTS MANUALLY FROM `target/pacts` to `src/test/resources/pacts`. Then you can run the `ProviderTest` which 
compares the pact to the running Node server.

## TODO 

- nothing right now...

## Notes

- Added JacksonConfig to META-INF/services/javax.ws.rs.ext.Providers so that Java 8 dates are nicely serialised.
- Added `log4j.category.au.com.dius.pact.consumer.UnfilteredMockProvider=DEBUG` to `log4j.properties`

## Ideas for tutorial

- Add `.body("{}")` into a GET request and use logging to get trainees to work out what is wrong.
- Change `app.js` and see what you have to change to make provider test fail
- Check Baards notes
- State
- See report under `target/pact/reports`
  - `.uponReceiving("...")` should be unique so that report doesn't get overwritten
- Naming conventions for tests?
- which of the three types of test should we use?

## Support

- https://groups.google.com/forum/#!msg/pact-support/6WDAeFSr1EU/jJPRGRenCgAJ
- https://github.com/DiUS/pact-jvm/issues
- Otherwise just Github which has examples
  - Be prepared to look at non-java solutions for ideas
  
