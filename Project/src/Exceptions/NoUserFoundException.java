package Exceptions;

public class NoUserFoundException extends Exception
{
   String message;
   
   public NoUserFoundException(String msg)
   {
	   this.message = msg;
   }
}
