package dk.jarry.minecraftlog.server.boundary;

import io.smallrye.reactive.messaging.annotations.Broadcast;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import dk.jarry.minecraftlog.MineCraft;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import javax.enterprise.context.ApplicationScoped;

@Traced
@ApplicationScoped
public class MineCraftService {
    
    @Incoming("minecraft-in")
    @Outgoing("minecraft-stream")                             
    @Broadcast
    public MineCraft process(MineCraft mineCraft){
        System.out.println(mineCraft);
        return mineCraft;
    }

}
