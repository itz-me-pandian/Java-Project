/*This is used in Registration class of Student and Faculty*/

package Exceptions;

public class RegistrationFailedException extends Exception
{
   public String message;
   
   public RegistrationFailedException(String msg)
   {
	   this.message = msg;
   }
}
