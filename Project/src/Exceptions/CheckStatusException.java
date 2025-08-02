package Exceptions;

public class CheckStatusException extends Exception
{
	public String message;
	   
	   public CheckStatusException(String msg)
	   {
		   this.message = msg;
	   }
}
