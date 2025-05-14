//$Id$
package jdbc_webProject.Exceptions;

public class AlreadyExistsException extends Exception {
	
	String message;
	
	public AlreadyExistsException(String message) {
		super(message);
		this.message = message;
	}
	
	public String getDetails() {
		return message;
	}
}
