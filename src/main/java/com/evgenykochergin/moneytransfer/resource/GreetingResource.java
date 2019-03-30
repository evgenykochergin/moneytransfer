package com.evgenykochergin.moneytransfer.resource;

import lombok.Value;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.LongAdder;

@Path("/jackson")
public class GreetingResource {

    private final LongAdder adder = new LongAdder();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{who}")
    public Greeting sayGreeting(@PathParam("who") String name) {
        adder.increment();
        return new Greeting(name, adder.intValue());
    }

    @Value
    private static class Greeting {
        private final String name;
        private final int count;
    }
}