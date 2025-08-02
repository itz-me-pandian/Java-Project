package Exceptions;

public class LoginException extends Exception
{
    public String message;
    
    public LoginException(String msg)
    {
    	this.message = msg;
    }
}
