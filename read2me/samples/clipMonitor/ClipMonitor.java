package samples.clipMonitor;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.HANDLE;
import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;
import org.xvolks.jnative.util.WindowProc;

public class ClipMonitor implements WindowProc {

	private HWND hwnd;
	private int prevWinProcAdd;
	private int nextClipboardViewer;
	
	// A hidden window that captures Clipboard events
	private Display display;
	private Shell shell;
	
	MyHiddenGuiThread gui;
	
	static final int WM_DRAWCLIPBOARD = 0x308;
	static final int WM_CHANGECBCHAIN = 0x030D;
	static final int GWL_WNDPROC = -4;
	
	
	public void start(){
		
		

		gui = new MyHiddenGuiThread();
		System.out.println("Start Hidden GUI");
		gui.start();
	}
	
	public void stop(){
		// have to control the GUI thread and stop it
		// Have to undo the Windows Clipboard hook
		// Should be called anytime read2Me stops. 
		
		// try this, might be wrong
		
	}
	
	public void createGUI(){
		// simple GUI from 
		// http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/Textexamplesnippetsettheselectionibeam.htm
		
		display = new Display();
	    shell = new Shell(display);
	    Text text = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
	    text.setBounds(10, 10, 100, 100);
	    for (int i = 0; i < 16; i++) {
	      text.append("invisible window " + i + "\n");
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
		//shell.setVisible(false);
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();

	}

	
	
	public void setup(int handle){
		hwnd = new HWND(handle);
		try { 
			System.out.println("setup prev winProc add");
			prevWinProcAdd = Clipboard_User32.GetWindowLong(hwnd, GWL_WNDPROC);
			
			System.out.println("setup, reg new win Proc");
			JNative.registerWindowProc(hwnd, this);
			nextClipboardViewer = Clipboard_User32.SetClipboardViewer(hwnd);
		} catch (Exception e) {
			System.out.println("clipMoniotr:setup Jnative problem");
			e.printStackTrace();
		}
	}

	public void shutDown() {
		
		try {
			Clipboard_User32.ChangeClipboardChain(hwnd, nextClipboardViewer);
		} catch (Exception e) {
			System.out.println("Shutdown problem");
			e.printStackTrace();
		}

	}
	
	// avoid recursive loop with GUI update
	private int count = 0;
	private boolean isStarted = false;
	private ArrayList queue = new ArrayList();
	
	public int windowProc(int winProchwnd, int uMsg, int wParam, int lParam) {
		
		// TODO Auto-generated method stub
		switch(uMsg) {
		
		case WM_DRAWCLIPBOARD:
				System.out.println("Caught clipboard DRAW event!!");
				fireClipEvent();
				break;
		case WM_CHANGECBCHAIN:
			// something
			System.out.println("Caught clipboard CHANGE event!!");

			if (wParam == nextClipboardViewer)
				nextClipboardViewer = lParam;
			else {

				try {
					Clipboard_User32.SendMessage(new HWND(nextClipboardViewer),
							new UINT(uMsg), new WPARAM(wParam), new LPARAM(
									lParam));
				} catch (Exception e) {
					System.out.println("SendMessage error");
					e.printStackTrace();
				}
			}

			break;
		default:
			// nothing
		}
		
		// Continue with the window event handler
		int result =0;
		try {
			
			if (isStarted == true) {
				System.out.println("yield"); 
				Thread.yield();
			}
			
			if (isStarted == false) {
			
			isStarted = true;
			result = Clipboard_User32.CallWindowProc(prevWinProcAdd, winProchwnd, uMsg, wParam, lParam);
			System.out.println("result " + result);
			
			// empty the queue. 
			for(int i =0; i < queue.size(); i++){
				WindowsEvent we = (WindowsEvent)queue.remove(0);
				result = Clipboard_User32.CallWindowProc(prevWinProcAdd, we.winProchwnd, we.uMsg, we.wParam, we.lParam);
				System.out.println("\t" + i + " Q result " + result);
			}
				
			
			isStarted = false;
				
//			count ++;
//			if (count < 3) {
//				System.out.println("count " + count);
//				
//			}
//			count--;
			} else {
				// queue request
				System.out.println("____Queue event " + uMsg);
				queue.add(new WindowsEvent(winProchwnd, uMsg, wParam, lParam));
				result = 0; // should be something but do not know what. Could sutom for paint event
			}
		} catch (Exception e) {
				System.out.println("windowProc: Jnative problem");
				e.printStackTrace();
		}
		return result;
	}
	
	Vector _listeners;
	
	public synchronized void addClipListener(ClipListener l)
    {
        if (_listeners == null)
        {
            _listeners = new Vector();
        }
        
        if (!_listeners.contains(l))
        {
            _listeners.addElement(l);
        }
    }
    
    public synchronized void removeClipListener(ClipListener l)
    {
        if (_listeners == null)
        {
            _listeners = new Vector();
        }
        
        if (_listeners.contains(l))
        {
            _listeners.removeElement(l);
        }
    }
	
    private Vector getListeners(Vector listen){
		Vector listeners;
        synchronized(this)
        {
            listeners = (Vector)listen.clone();
        }
        return listeners;
	}
	
    public void fireClipEvent() {
		System.out.println("ClipMonitor: clipEvent: ");
		if (_listeners != null)
        {
            System.out.println("Event!! updateClearDiag");
            Vector listeners = getListeners(_listeners);
            ClipEvent e = new ClipEvent(this); // Update info 
            for (int i = listeners.size() - 1; i >= 0; --i)
            {
            	System.out.println("ClipMonitor: fire event");
                ((ClipListener)listeners.elementAt(i)).clipEvent(e);
            }
        }
	}
    
	class WindowsEvent {
		public int winProchwnd;
		public int uMsg;
		public int wParam;
		public int lParam;
		
		public WindowsEvent(int wPh, int uM, int wP, int lP){
			winProchwnd = wPh;
			uMsg = uM;
			wParam = wP;
			lParam = lP;
		}
	}
	
	class MyHiddenGuiThread extends Thread {

		public void run() {
			createGUI();
			
			// ClipBoard stuff: connect handler
			setup(shell.handle);
			
			runGUI();
		}
	}

// Extend Jnative user32 to include clipboard functions


}







