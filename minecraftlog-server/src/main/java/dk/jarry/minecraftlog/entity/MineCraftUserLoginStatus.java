package dk.jarry.minecraftlog.entity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MineCraftUserLoginStatus {
	
	private final static String JOIN_THE_GAME_REG = "^.*(?=( joined the game))";
	private final static String LEFT_THE_GAME_REG = "^.*(?=( left the game))";
	
	private final static Pattern JOIN_THE_GAME_PATTERN = Pattern.compile(JOIN_THE_GAME_REG);
	private final static Pattern LEFT_THE_GAME_PATTERN = Pattern.compile(LEFT_THE_GAME_REG);

	public String name;
	public String status;
	
	public String collectingDate;	
	public String collectingTimeStamp;
	
	public MineCraftUserLoginStatus(MineCraftLog mineCraftLog) {
	
		String message = mineCraftLog.message;
		if(message != null) {	
		
			if(mineCraftLog.message.contains(" joined the game")) {
				Matcher match = JOIN_THE_GAME_PATTERN.matcher(message);
				if(match.find()) {
					this.name = match.group();			
				}
				this.status="joined the game";
			}
			
			if(mineCraftLog.message.contains(" left the game")) {
				Matcher match = LEFT_THE_GAME_PATTERN.matcher(message);
				if(match.find()) {
					this.name = match.group();			
				}
				this.status="left the game";
			}
			
			ZonedDateTime now = ZonedDateTime.now();
			collectingDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
			collectingTimeStamp = now.format(DateTimeFormatter.ISO_INSTANT);
		
		}
		
	}
		
	@Override
	public String toString() {
		return "MineCraftUserLoginStatus [name=" + name + ", status=" + status + "]";
	}



	public static void main(String[] args) {
		MineCraftLog mineCraftLog = new MineCraftLog();
		
		mineCraftLog.message = "jarry_dk joined the game";		
		MineCraftUserLoginStatus m = new MineCraftUserLoginStatus(mineCraftLog);
		System.out.println(m.toString());
		
		mineCraftLog.message = "jarry_dk left the game";		
		m = new MineCraftUserLoginStatus(mineCraftLog);
		System.out.println(m.toString());
		
		
		
		
	}
	
}
