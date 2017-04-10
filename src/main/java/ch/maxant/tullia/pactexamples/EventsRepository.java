package ch.maxant.tullia.pactexamples;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class EventsRepository {

    private final String baseUrl;

    public EventsRepository(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Event> getEvents() {

        try {
            Client client = ClientBuilder.newClient().register(LoggingFilter.class);

            Response response = client.target(baseUrl + "/all").request(MediaType.APPLICATION_JSON_TYPE).post(
                    Entity.entity(new EventRequest("asdf"), MediaType.APPLICATION_JSON_TYPE), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Event> events = response.readEntity(new GenericType<List<Event>>() {
                });
                return events;
            } else {
                throw new RuntimeException("failed to get events. status code was " + response.getStatus());
            }
        } catch (WebApplicationException e) {
            throw e; //TODO handle correctly
        }
    }

    public Map<String, Event> getEventsMap() {

        try {
            Client client = ClientBuilder.newClient().register(LoggingFilter.class);

            Response response = client.target(baseUrl + "/dictionary").request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, Event> events = response.readEntity(new GenericType<Map<String, Event>>() {
                });
                return events;
            } else {
                throw new RuntimeException("failed to get events as dictionary. status code was " + response.getStatus());
            }
        } catch (WebApplicationException e) {
            throw e; //TODO handle correctly
        }
    }

    public Map<String, List<Event>> getEventsMapArray() {

        try {
            Client client = ClientBuilder.newClient().register(LoggingFilter.class);

            Response response =
                    client.target(baseUrl + "/dictionaryArray").request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, List<Event>> events = response.readEntity(new GenericType<Map<String, List<Event>>>() {
                });
                return events;
            } else {
                throw new RuntimeException("failed to get events as dictionary. status code was " + response.getStatus());
            }
        } catch (WebApplicationException e) {
            throw e; //TODO handle correctly
        }
    }

    public  Map<String, Map<String, List<Event>>> getEventsMapNestedArray() {

        try {
            Client client = ClientBuilder.newClient().register(LoggingFilter.class);

            Response response =
                    client.target(baseUrl + "/dictionaryNestedArray").request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, Map<String, List<Event>>> events = response.readEntity(new GenericType<Map<String, Map<String, List<Event>>>>() {
                });
                return events;
            } else {
                throw new RuntimeException("failed to get events as dictionary. status code was " + response.getStatus());
            }
        } catch (WebApplicationException e) {
            throw e; //TODO handle correctly
        }
    }

    public static void main(String[] args) {
        System.out.println(new EventsRepository("http://localhost:8091").getEvents());
    }
}
