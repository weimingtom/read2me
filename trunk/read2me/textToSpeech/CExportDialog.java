package textToSpeech;

//import java.net.URL;
import java.io.File;

/*
 * FileDialog example snippet: prompt for a file name (to save)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class CExportDialog {

	private String textToConvert;
	private PlayerVoice voice;

	public CExportDialog(){
		textToConvert="text";
	}

	public CExportDialog(String text, PlayerVoice voice){
		textToConvert=text;
		this.voice = voice;
	}

	public void setText(String text){
		textToConvert=text;
	}

	public void createDialog(final Shell _s) {
		
		//display = Display.getCurrent();
		//Shell shell = new Shell(display);

		//shell = shell.getShell();
		
		FileDialog dialog = new FileDialog(_s, SWT.SAVE);
		dialog.setFilterNames(new String[] { "Mp3 file", "Wav file" });
		dialog.setFilterExtensions(new String[] { "*.mp3", "*.wav" }); // Windows
		// wild
		// cards

		dialog.setFilterPath("c:\\"); // Windows path
		dialog.setFileName("mySpeech");
		System.out.println("Save to: " + dialog.open());
		
		
		//System.out.println("got here");
		String fileName = dialog.getFileName();
		String path = dialog.getFilterPath();
		
		if( fileName.endsWith("mp3") ){
			
			System.out.println("InstallationDirectory: " +System.getProperty("user.dir"));
			String encodePath = System.getProperty("user.dir")+ "\\temp.wav";
			
			//fileName = fileName.replace(".mp3", ".wav");
			path = path +"\\"+ fileName;
			System.out.println("filename: "+path);
			//CExportToWAV createWAV = new CExportToWAV("change default text to mp3 textToConvert",path);
			CFullExportToWAV createWAV = new CFullExportToWAV(textToConvert,encodePath, voice);
			
			Mp3Encoder encoder = new Mp3Encoder(encodePath,"16");
			
			//try
			//{
				createWAV.exportToWAV();
				encoder.start();
			//}
			//catch(java.io.IOException e)
			//{
			//	System.out.println("IOException caught: " + e);
			//}
			//catch(java.lang.InterruptedException e)
			//{
			//	System.out.println("InterruptedException caught: " + e);
			//}
			
			
			//moves the generated mp3 file to the location chosen by the user
			encodePath = encodePath.replace(".wav", ".mp3");
			
			System.out.println("encodepath :"+encodePath);
			System.out.println("new path :"+path);
			
		    // File (or directory) with old name
		    File file1 = new File(encodePath);
		    
		    // File (or directory) with new name
		    File dir = new File(path);
		    
		    // Rename file (or directory)
		    boolean success = file1.renameTo(new File(dir, file1.getName()));
		    if (!success) {
		        // File was not successfully renamed
		    	System.out.println("move failed");
		    }
		    
		    
			//forces the garbage collector to run
			/*System.gc();
			File temp = new File(path);
			temp.delete();*/
			
			/*File myFile = new File("c:\\mySpeech.wav");
			//myFile.deleteOnExit()
			try {
				//boolean deletionSuccess = 
				myFile.delete();
					//myFile.deleteOnExit();
				//if (!deletionSuccess) {
					//System.out.println("delete failed");
				//}
			}
			catch (SecurityException e) {
				System.out.println("Caught security exception trying to delete file " + myFile.toString());
			}*/
		}
		else{
			path = path +"\\"+ fileName;
			System.out.println("filename: "+path);
			CFullExportToWAV createWAV = new CFullExportToWAV(textToConvert,path, voice);
			createWAV.start();
			
		}
		
	}
}