package gui;
import org.eclipse.swt.program.Program;
//import org.eclipse.swt.graphics.*;

public class CHTML {
	private String Html = "<html> <head>"
		+ " <title> Tips for Read 2 me </title> "
		+ " </head> "
		+ " <body>"
		+ "<h1 align='center'>Tips for Read2me</h1>"
		+ "<a href='test.html'>pouet</a>"
		+ "</body></html>";
	
	public CHTML()
	{
		 
	}
	
	public String getHTML()
	{
		return Html;
	}
	
	public void viewTips()
	{
		Program.launch(".\\images\\tips.html");	
	}

}
