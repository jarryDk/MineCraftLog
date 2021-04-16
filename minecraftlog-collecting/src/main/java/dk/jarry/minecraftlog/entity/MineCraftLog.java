package dk.jarry.minecraftlog.entity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MineCraftLog {

	private final static String TIME_REG = "\\[[0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\]";
	private final static String THREAD_REG = "\\s\\[.+\\]:";
	private final static String MESSAGE_REG = "\\]:.*";

	private final static Pattern TIME_PATTERN = Pattern.compile(TIME_REG);
	private final static Pattern THREAD_PATTERN = Pattern.compile(THREAD_REG);
	private final static Pattern MESSAGE_PATTERN = Pattern.compile(MESSAGE_REG);

	@JsonProperty("time")
	private String time;

	@JsonProperty("thread")
	private String thread;

	@JsonProperty("message")
	private String message;
	
	@JsonProperty("collectingDate")
	private String collectingDate;
	
	@JsonProperty("collectingTimeStamp")
	private String collectingTimeStamp;
	
	public MineCraftLog(String line) {

		Matcher match = TIME_PATTERN.matcher(line);
		if(match.find()) {
			this.time = match.group();
			this.time = this.time.substring(1, this.time.length() - 1);			
		}

		match = THREAD_PATTERN.matcher(line);
		if (match.find()) {
			this.thread = match.group();
			this.thread = this.thread.substring(2, this.thread.length() - 1);
		}

		match = MESSAGE_PATTERN.matcher(line);
		if (match.find()) {
			this.message = match.group();
			this.message = this.message.substring(3, this.message.length());
		}
		
		ZonedDateTime now = ZonedDateTime.now();
		collectingDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
		collectingTimeStamp = now.format(DateTimeFormatter.ISO_INSTANT);
				
	}

	@Override
	public String toString() {
		return "MineCraft [time=" + time + ", thread=" + thread + ", message=" + message + "]";
	}

	public static void main(String[] args) {
		MineCraftLog mineCraftLog = new MineCraftLog("[19:50:30] [Server thread/INFO]: /tell -> msg");
		System.out.println(mineCraftLog.toString());

		mineCraftLog = new MineCraftLog("[19:52:21] [Server thread/INFO]: jarry_dk[/127.0.0.1:36852] logged in with entity id 411 at (206.5, 68.0, -75.5)");
		System.out.println(mineCraftLog.toString());

		mineCraftLog = new MineCraftLog("[19:49:09] [Worker-Main-8/INFO]: Preparing spawn area: 0%");
		System.out.println(mineCraftLog.toString());

	}

}
