package ch.maxant.tullia.pactexamples;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class EventsRepository {

    public EventsRepository(){
    }

    public List<Event> getEvents(){

        try{
            Client client = ClientBuilder.newClient();

            Response response = client
                    .target("http://localhost:8091/all")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(new EventRequest("asdf"), MediaType.APPLICATION_JSON_TYPE), Response.class);
            if(response.getStatus() == Response.Status.OK.getStatusCode()){
                List<Event> events = response.readEntity(new GenericType<List<Event>>(){});
                return events;
            } else{
                throw new RuntimeException("failed to get events. status code was " + response.getStatus());
            }
        }catch(WebApplicationException e){
            throw e; //TODO handle correctly
        }
    }

    public static void main(String[] args) {
        System.out.println(new EventsRepository().getEvents());
    }
}
