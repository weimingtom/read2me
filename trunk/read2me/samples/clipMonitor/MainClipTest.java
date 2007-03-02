package clipMonitor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainClipTest implements ClipListener {

	/**
	 * @param args
	 */
	
	// C# clipboard monitor
	//http://www.developer.com/net/csharp/article.php/3359891
	
	// control itunes dll from java
	// http://www.workingwith.me.uk/articles/java/itunes-com-with-java-and-swing
	
	// possibel VB solution to monitor clipboard
	// http://www.freevbcode.com/ShowCode.asp?ID=1306
	
	// Maybe have to use JNI to get access to dll
	// simeple example http://www.codeproject.com/useritems/jnisample.asp
	
	// use jnative to access user32 dll
		
	// extend the user 32 dll to handle clipboard 
	
	// tips to fix recursive WindowsProc
	// http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/windowing/windowprocedures/aboutwindowprocedures.asp
	
	Display display;
	Shell shell;
	ClipMonitor clipMonitor; 
	
	Text text;
	
	public static void main(String[] args) {
		MainClipTest clipTest = new MainClipTest();	
		clipTest.runMain();
	}
	
	
	public void runMain() {
		// TODO Auto-generated method stub
		clipMonitor = new ClipMonitor();
		createGUI();
		
		// ClipBoard stuff
		clipMonitor.addClipListener(this);
		clipMonitor.start();
		
		
		// connect user 32. Silly that it requires a handle to a window. App could also be without.
		//shell.handle;
		
		runGUI();

	}
	
	public void createGUI(){
		// simple GUI from 
		// http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/Textexamplesnippetsettheselectionibeam.htm
		
		display = new Display();
	    shell = new Shell(display);
	    text = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
	    text.setBounds(10, 10, 100, 100);
	    for (int i = 0; i < 16; i++) {
	      text.append("Line " + i + "\n");
	    }
	    shell.open();
	    text.setSelection(30);
	    //System.out.println(text.getCaretLocation());
	}
	
	public void runGUI() {
		
		shell.layout();
		shell.pack();
		//shell.setSize(500, 300);
		shell.open();
		
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();

	}


	public void clipEvent(ClipEvent event) {
		// Event triggered by ClipMonitor when data copied to clipboard 
		System.out.println("Made it back to main app");
		
		// access clipboard from example
		// http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTClipboardExample.htm
		
				
		
		if (display != null) {
			//nodesLB.setText(gui_string);
			display.asyncExec(new Runnable() {
				public void run() {
					// Update UI.
					//text.append("Clip event\n");
					try {
						Clipboard clipboard = new Clipboard(display);
						String data = (String) clipboard.getContents(TextTransfer.getInstance());
						//	System.out.println("Made it back to main app " + data);
						text.setText(data);
					} catch (Exception e) {
						System.out.println("NOT TEXT");
					}
				}
			});
		}
	}

}
