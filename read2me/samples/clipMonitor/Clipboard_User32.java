package clipMonitor;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.util.User32;

public class Clipboard_User32 extends User32 {
	
	
	
	static public HWND GetActiveWindow() throws NativeException, IllegalAccessException {
		
		JNative GetActiveWindow = new JNative(DLL_NAME, "GetActiveWindow");
		GetActiveWindow.setRetVal(Type.INT);
		GetActiveWindow.invoke();
		HWND handle = new HWND(GetActiveWindow.getRetValAsInt());
		GetActiveWindow.dispose();
		return handle;
		
	}
	
	
	public static int GetWindowLong(HWND hwnd, int nIndex) throws NativeException, IllegalAccessException {
		
		JNative _GetWindowLong = new JNative(DLL_NAME, "GetWindowLongA");
		_GetWindowLong.setRetVal(Type.INT);
		
		_GetWindowLong.setParameter(0, Type.INT, hwnd.getValueAsString());
		_GetWindowLong.setParameter(1, Type.INT, nIndex+"");

		_GetWindowLong.invoke();
		//long result = Long.parseLong(_GetWindowLong.getRetVal());
		int result = _GetWindowLong.getRetValAsInt();
		_GetWindowLong.dispose();
		return result;
		
	}

	
	public static int ChangeClipboardChain(HWND hwnd, int nIndex) throws NativeException, IllegalAccessException {
		
		JNative _ChangeClipboardChain = new JNative(DLL_NAME, "ChangeClipboardChain");
		_ChangeClipboardChain.setRetVal(Type.INT); // should be BOOL
		
		_ChangeClipboardChain.setParameter(0, Type.INT, hwnd.getValueAsString());
		_ChangeClipboardChain.setParameter(1, Type.INT, nIndex+"");

		_ChangeClipboardChain.invoke();
		//long result = Long.parseLong(_GetWindowLong.getRetVal());
		int result = _ChangeClipboardChain.getRetValAsInt();
		_ChangeClipboardChain.dispose();
		return result;
		
	}
	
	//CallWindowProc(mPrevHandle, hwnd, Msg, wParam, lParam)
	public static int CallWindowProc(int prevWinProcAdd, 
			int hwnd, int uMsg, int wParam, int lParam) throws NativeException, IllegalAccessException {
		
		JNative _CallWindowProc = new JNative(DLL_NAME, "CallWindowProcA");
		_CallWindowProc.setRetVal(Type.INT);
		
		_CallWindowProc.setParameter(0, Type.INT, prevWinProcAdd+"");
		_CallWindowProc.setParameter(1, Type.INT, hwnd+"");
		_CallWindowProc.setParameter(2, Type.INT, uMsg+"");
		_CallWindowProc.setParameter(3, Type.INT, wParam+"");
		_CallWindowProc.setParameter(4, Type.INT, lParam+"");

		//System.out.print("invoke _CallWindowProc  ");
		_CallWindowProc.invoke();
		//System.out.println ("... return");
		int result = _CallWindowProc.getRetValAsInt();

		_CallWindowProc.dispose();
		return result;
		
	}


	
	//protected static extern int
    //SetClipboardViewer(int hWndNewViewer);

	public static int SetClipboardViewer(HWND hwnd) throws NativeException, IllegalAccessException {
		
		JNative _SetClipboardViewer = new JNative(DLL_NAME, "SetClipboardViewer");
		_SetClipboardViewer.setRetVal(Type.INT);
		
		_SetClipboardViewer.setParameter(0, Type.INT, hwnd.getValueAsString());

		_SetClipboardViewer.invoke();
		int result = _SetClipboardViewer.getRetValAsInt();
		_SetClipboardViewer.dispose();
		return result;
		
	}

//[DllImport("User32.dll", CharSet=CharSet.Auto)]
//public static extern bool
// ChangeClipboardChain(IntPtr hWndRemove,
//                      IntPtr hWndNewNext);

//[DllImport("user32.dll", CharSet=CharSet.Auto)]
//public static extern int SendMessage(IntPtr hwnd, int wMsg,
//                               IntPtr wParam,
//                               IntPtr lParam);
//...

	
}
