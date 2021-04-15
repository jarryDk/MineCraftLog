package dk.jarry.minecraftlog.collecting.control;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class SimpleLoggerTailProcessing implements TailProcessing {
	
	private final static Logger logger = Logger.getLogger(SimpleLoggerTailProcessing.class.getCanonicalName());

	@Override
	public void process(String line) {
		logger.info("Log line : " +line);		
	}

	@Override
	public void process(String filePath, String fileName, String line) {
		Path path = Paths.get(filePath, fileName);
		logger.fine("Modified file : " + path.toString());
		logger.info("Log line : " + line);		
	}

}
