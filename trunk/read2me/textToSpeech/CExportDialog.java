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
//import java.lang.Thread;

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

		//dialog.setFilterPath("c:\\"); // Windows path
		dialog.setFilterPath(System.getProperty("user.dir")); // Windows path
		dialog.setFileName("mySpeech");
		System.out.println("Save to: " + dialog.open());


		String fileName = dialog.getFileName();
		String path = dialog.getFilterPath();

		if( fileName.endsWith("mp3") ){

			System.out.println("InstallationDirectory: " +System.getProperty("user.dir"));
			//String encodePath = System.getProperty("user.dir")+ "\\temp.wav";
			

			fileName = fileName.replace(".mp3", ".wav");
			path = path +"\\"+ fileName;
			System.out.println("filename: "+path);
			//CExportToWAV createWAV = new CExportToWAV("change default text to mp3 textToConvert",path);

			/*
			//saves the audio files in the install directory. Useful for future move of file
			CFullExportToWAV createWAV = new CFullExportToWAV(textToConvert,encodePath, voice);
			System.out.println("Wav path: "+encodePath);
			Mp3Encoder encoder = new Mp3Encoder(encodePath,"16");
			*/
			

			CFullExportToWAV createWAV = new CFullExportToWAV(textToConvert,path, voice);
			System.out.println("Wav path: "+path);

			Mp3Encoder encoder = new Mp3Encoder(path,"16");
			
			createWAV.exportToWAV();

			//encoder.start();
			encoder.beginMP3Export();

/*
			//moves the generated mp3 file to the location chosen by the user
			encodePath = encodePath.replace(".wav", ".mp3");
			path = path.replace(".wav", ".mp3");
			System.out.println("encodepath: "+encodePath);
			System.out.println("new path: "+path);

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
*/


			/*File myFile = new File(encodePath);
			//myFile.deleteOnExit()
			try {
				//boolean deletionSuccess = myFile.delete();
				myFile.delete();
					//myFile.deleteOnExit();
				//if (!deletionSuccess) {
					//System.out.println("delete failed");
				//}
			}
			catch (Exception e) {
				System.out.println("Caught exception trying to delete file " +
						e.toString());
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