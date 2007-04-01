package textToSpeech;

/*
 * FileDialog example snippet: prompt for a file name (to save)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class CExportDialog {

	private String textToConvert;

	public CExportDialog(){
		textToConvert="text";
	}

	public CExportDialog(String text){
		textToConvert=text;
	}

	public void setText(String text){
		textToConvert=text;
	}

	public void createDialog() {
		//Display display = new Display();
		Display display=Display.getCurrent();
		Shell shell = new Shell(display);
		shell = shell.getShell();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog
		.setFilterNames(new String[] { "Mp3 file", "Wav file" });
		dialog.setFilterExtensions(new String[] { "*.mp3", "*.wav" }); // Windows
		// wild
		// cards

		dialog.setFilterPath("c:\\"); // Windows path
		dialog.setFileName("mySpeech");
		System.out.println("Save to: " + dialog.open());
		
		
		System.out.println("got here");
		String fileName = dialog.getFileName();
		String path = dialog.getFilterPath();
		
		if( fileName.endsWith("mp3") ){
			
			fileName = fileName.replace(".mp3", ".wav");
			path = path +"\\"+ fileName;
			System.out.println("filename: "+path);
			CExportToWAV createWAV = new CExportToWAV("change default text to mp3 textToConvert",path);
			//CExportToWAV createWAV = new CExportToWAV(textToConvert,path);
			
			Mp3Encoder encoder = new Mp3Encoder(path,"16");
			
			try
			{
				createWAV.exportToWAV();
				encoder.start();
			}
			catch(java.io.IOException e)
			{
				System.out.println("IOException caught: " + e);
			}
			catch(java.lang.InterruptedException e)
			{
				System.out.println("InterruptedException caught: " + e);
			}
		}
		else{
			path = path +"\\"+ fileName;
			System.out.println("filename: "+path);
			CExportToWAV createWAV = new CExportToWAV("change default text to mp3 textToConvert",path);
			createWAV.start();
		}
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}
}