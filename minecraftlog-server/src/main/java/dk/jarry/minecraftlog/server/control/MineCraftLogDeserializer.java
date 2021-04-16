package dk.jarry.minecraftlog.server.control;

import dk.jarry.minecraftlog.entity.MineCraftLog;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class MineCraftLogDeserializer extends JsonbDeserializer<MineCraftLog> {

    public MineCraftLogDeserializer(){
        // pass the class to the parent.
        super(MineCraftLog.class);
    }
    
}
