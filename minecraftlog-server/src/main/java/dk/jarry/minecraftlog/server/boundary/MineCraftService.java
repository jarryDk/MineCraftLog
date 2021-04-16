package dk.jarry.minecraftlog.server.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import dk.jarry.minecraftlog.entity.MineCraftLog;
import dk.jarry.minecraftlog.entity.MineCraftUserLoginStatus;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@Traced
@ApplicationScoped
public class MineCraftService {
    
    @Incoming("minecraftlog-in")
    @Outgoing("minecraft-log-stream")                             
    @Broadcast
    public MineCraftLog processLog(MineCraftLog mineCraftLog){    	
    	System.out.println(mineCraftLog);    	
    	if(mineCraftLog.message.contains(" joined the game") ||
    			mineCraftLog.message.contains(" left the game")) {
    		processUserLoginStatus(mineCraftLog);
    	}    	        
        return mineCraftLog;
    }
    
    @Inject 
    @Channel("minecraft-user-login-status-out") 
    Emitter<MineCraftUserLoginStatus> mineCraftUserLoginStatusEmitter;
      
    public void processUserLoginStatus(MineCraftLog mineCraftLog){
    	
    	System.out.println("MineCraftUserLoginStatus - - based on " + mineCraftLog);
    	
    	MineCraftUserLoginStatus mineCraftUserLoginStatus = new MineCraftUserLoginStatus(mineCraftLog);
    	if(mineCraftUserLoginStatus.name != null) {
    		mineCraftUserLoginStatusEmitter.send(mineCraftUserLoginStatus);
    	}
    	    	
    }
       
}
