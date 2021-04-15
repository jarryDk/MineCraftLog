package dk.jarry.minecraftlog.server.boundary;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import dk.jarry.minecraftlog.MineCraft;

@Traced
@Path("/logs")
public class MineCraftResource {

    @Inject
    @Channel("minecraft-stream") 
    Publisher<MineCraft> mineCraft; 

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @SseElementType("text/plain") 
    public Publisher<MineCraft> stream() { 
        return mineCraft;
    }

}