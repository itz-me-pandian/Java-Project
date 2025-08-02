/*Exception used in viewTask.java & viewTaskUI.java*/

package Exceptions;

public class ViewTaskException extends Exception
{
	public String message;
	   
	   public ViewTaskException(String msg)
	   {
		   this.message = msg;
	   }
	   
	   public String toString()
	   {
		   return message;
	   }
}
