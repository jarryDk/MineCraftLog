package dk.jarry.minecraftlog.collecting.control;

public interface TailProcessing {
    
	void process(String line);
    
	void process(String filePath, String fileName,String line);
    
}
