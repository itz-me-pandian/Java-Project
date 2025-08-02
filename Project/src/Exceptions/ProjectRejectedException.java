package Exceptions;

public class ProjectRejectedException extends Exception
{
	private String message;
	
	public ProjectRejectedException(String msg)
	{
		this.message = msg;
	}
	
	public String toString()
	{
		return message;
	}
}
