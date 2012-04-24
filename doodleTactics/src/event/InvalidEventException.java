package event;

public class InvalidEventException extends Exception {
	public InvalidEventException(String invalidLine){
		super("\"" + invalidLine + "\" did not parse correctly");
	}
	
	public InvalidEventException(String invalidChar, String invalidLine){
		super("Character " + invalidChar + " not found (line: " + invalidLine + ")");
	}
}
