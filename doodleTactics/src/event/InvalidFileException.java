package event;

public class InvalidFileException extends Exception {
	public InvalidFileException(String invalidLine){
		super(invalidLine + " did not parse correctly");
	}
}
