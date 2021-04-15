package dk.jarry.minecraftlog.server.control;

import dk.jarry.minecraftlog.MineCraft;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class MineCraftDeserializer extends JsonbDeserializer<MineCraft> {

    public MineCraftDeserializer(){
        // pass the class to the parent.
        super(MineCraft.class);
    }
    
}
