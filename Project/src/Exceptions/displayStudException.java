/*Used in Student displayStud.java(backend) & projectPage.java(frontend) */

package Exceptions;

public class displayStudException extends Exception
{
    private String message;
    
    public displayStudException(String msg)
    {
    	this.message = msg;
    }
    
    public String toString()
    {
    	return message;
    }
}
