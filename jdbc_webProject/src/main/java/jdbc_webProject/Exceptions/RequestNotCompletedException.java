//$Id$
package jdbc_webProject.Exceptions;

public class RequestNotCompletedException extends Exception {
	
	String details;
	
	public RequestNotCompletedException(String message) {
		super(message);
	}
	
	public RequestNotCompletedException(String message, String details) {
		super(message);
		this.details = details;
	}
	
	public String getDetails() {
		return details;
	}
}
