package dk.jarry.minecraftlog.collecting;

import java.io.IOException;
import java.util.logging.Logger;

import dk.jarry.minecraftlog.collecting.control.KafkaTailProcessing;
import dk.jarry.minecraftlog.collecting.control.LogTail;

public class Main {
	
	private final static Logger logger = Logger.getLogger(Main.class.getCanonicalName());

	// MineCraft server log folder
	private static String filePath = "/opt/minecraft/server/1.16.5/logs";

	// Name of the file to collect from
	private static String fileName = "latest.log";

	// The topic on Kafka data is send to
	public static String topicName = "mineCraftLog";
	
	public static void main(String[] args) {
				
		if(System.getenv("filePath") != null) {
			filePath = System.getenv("filePath");
			logger.info("Use env filePath : " + filePath);
		} else {
			logger.info("Use default filePath : " + filePath);
		}
		
		if(System.getenv("fileName") != null) {
			fileName = System.getenv("fileName");
			logger.info("Use env fileName : " + fileName);
		}else {		
			logger.info("Use default fileName : " + fileName);
		}
		if(System.getenv("topicName") != null) {
			topicName = System.getenv("topicName");
			logger.info("Use env topicName : " + topicName);
		}else {
			logger.info("Use default topicName : " + topicName);
		}
		
		LogTail logTail = new LogTail(filePath, fileName);

		KafkaTailProcessing kafkaTailProcessing = new KafkaTailProcessing(topicName, true);

		try {
            logTail.tail(kafkaTailProcessing);
        } catch (IOException | InterruptedException e) {            
            e.printStackTrace();
        }
		
	}
    
}
