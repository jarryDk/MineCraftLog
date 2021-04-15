package dk.jarry.minecraftlog.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MineCraft {

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

	public MineCraft(String line) {

		Matcher match = TIME_PATTERN.matcher(line);
		while (match.find()) {
			this.time = match.group();
			this.time = this.time.substring(1, this.time.length() - 1);			
		}

		match = THREAD_PATTERN.matcher(line);
		while (match.find()) {
			this.thread = match.group();
			this.thread = this.thread.substring(2, this.thread.length() - 1);
		}

		match = MESSAGE_PATTERN.matcher(line);
		while (match.find()) {
			this.message = match.group();
			this.message = this.message.substring(3, this.message.length());
		}
	}

	@Override
	public String toString() {
		return "MineCraft [time=" + time + ", thread=" + thread + ", message=" + message + "]";
	}

	public static void main(String[] args) {
		MineCraft mineCraft = new MineCraft("[19:50:30] [Server thread/INFO]: /tell -> msg");
		System.out.println(mineCraft.toString());

		mineCraft = new MineCraft("[19:52:21] [Server thread/INFO]: jarry_dk[/127.0.0.1:36852] logged in with entity id 411 at (206.5, 68.0, -75.5)");
		System.out.println(mineCraft.toString());

		mineCraft = new MineCraft("[19:49:09] [Worker-Main-8/INFO]: Preparing spawn area: 0%");
		System.out.println(mineCraft.toString());

	}

}
