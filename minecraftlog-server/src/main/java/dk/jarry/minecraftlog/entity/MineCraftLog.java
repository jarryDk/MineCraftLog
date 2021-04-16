package dk.jarry.minecraftlog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MineCraftLog {

    public String time;
	public String thread;
	public String message;

	public String collectingDate;	
	public String collectingTimeStamp;

	@Override
	public String toString() {
		return "MineCraft [time=" + time + ", thread=" + thread + ", message=" + message + "]";
	}
    
}
