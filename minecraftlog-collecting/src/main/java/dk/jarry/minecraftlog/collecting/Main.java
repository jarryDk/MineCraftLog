package dk.jarry.minecraftlog.collecting;

import java.io.IOException;

import dk.jarry.minecraftlog.collecting.control.KafkaTailProcessing;
import dk.jarry.minecraftlog.collecting.control.LogTail;

public class Main {

	public static void main(String[] args) {

		// monitored directory
		String filePath = "/opt/minecraft/server/1.16.5/logs";

		// file to monitor
		String fileName = "latest.log";
        
		LogTail logTail = new LogTail(filePath, fileName);

		KafkaTailProcessing kafkaTailProcessing = new KafkaTailProcessing(KafkaTailProcessing.TOPIC_NAME, true);

		try {
            logTail.tail(kafkaTailProcessing);
        } catch (IOException | InterruptedException e) {            
            e.printStackTrace();
        }
		
	}
    
}
