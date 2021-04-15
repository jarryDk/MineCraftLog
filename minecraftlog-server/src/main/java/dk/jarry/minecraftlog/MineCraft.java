package dk.jarry.minecraftlog;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MineCraft {

    public String time;
	public String thread;
	public String message;

	@Override
	public String toString() {
		return "MineCraft [time=" + time + ", thread=" + thread + ", message=" + message + "]";
	}
    
}
