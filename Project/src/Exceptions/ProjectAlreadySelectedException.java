package Exceptions;

public class ProjectAlreadySelectedException extends Exception
{
	String message;
	
	public ProjectAlreadySelectedException(String msg)
	{
		this.message = msg;
	}
}
