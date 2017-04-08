# What is this?

A playground for a JAX-RS Client and Pact examples.

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

## TODO 

- create tests for "dictionaries", ie objects with dynamic fieldnames.

## Notes

- Added JacksonConfig to META-INF/services/javax.ws.rs.ext.Providers so that Java 8 dates are nicely serialised.
- Added `log4j.category.au.com.dius.pact.consumer.UnfilteredMockProvider=DEBUG` to `log4j.properties`

## Ideas for tutorial

- Add `.body("{}")` into a GET request and use logging to get trainees to work out what is wrong.
