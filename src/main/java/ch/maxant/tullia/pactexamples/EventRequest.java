package ch.maxant.tullia.pactexamples;

public class EventRequest {
    private String someField;

    //for jackson
    private EventRequest(){}

    public EventRequest(String someField) {
        this.someField = someField;
    }

    public String getSomeField() {
        return someField;
    }
}
