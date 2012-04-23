package map;

/**
 * 
 * @author rroelke
 * an InvalidMapException is thrown whenever a map file is invalid and cannot be parsed into a map
 */
@SuppressWarnings("serial")
public class InvalidMapException extends Exception {
	String _error;
	public InvalidMapException(String reason){
		_error = reason;
	}
	
	public void printMessage(){
		System.out.println("Invalid Map: " + _error);
	}
	
	public String getMessage(){
		return _error;
	}
}
