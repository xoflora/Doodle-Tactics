package event;

public class InvalidFileException extends Exception {
	public InvalidFileException(String invalidLine){
		super("\"" + invalidLine + "\" did not parse correctly");
	}
	
	public InvalidFileException(String invalidChar, String invalidLine){
		super("Character " + invalidChar + " not found (line: " + invalidLine + ")");
	}
}
