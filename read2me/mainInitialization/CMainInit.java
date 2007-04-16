package mainInitialization;

import textToSpeech.*;
import gui.CGUICommand;
import gui.CGUIMain;
import gui.CGUICommandInterface;
import javax.swing.ListModel;
import textToSpeech.PlayerVoice;

/**
 * This class is main entry for the Read2Me! program
 * All the initializations will take place in this class
 * The user will be redirected from here to the place he needs to go
 *
 */
public class CMainInit {
	

	public static void main(String[] a){
		System.out.println("Starting Read2Me!");
		//System.out.println(System.getProperty("os.name"));
		//System.out.println(System.getProperty("java.class.path"));
		//System.out.println(System.getProperty("java.library.path"));
		
		// Free TTS player
		final CPlayerInterface player;
		player = new CFullPlayer();   
		player.createSynthesizers();
		player.setSynthesizer(1);
		
		// Initialize the voices parameters: numbers and names
		ListModel voices = player.getVoiceList();
		int sizeVoices = voices.getSize();
		PlayerVoice playerVoice;
		String[] listVoices = new String[sizeVoices];
		for(int i=0; i<sizeVoices; i++)
		{
			playerVoice = (PlayerVoice)voices.getElementAt(i);
			listVoices[i] = playerVoice.getName();
		}
		
		// Initialize the command control for the GUI
		CGUICommandInterface guiControl = new CGUICommand(player);
		
		// Display the GUI
		@SuppressWarnings("unused")
		CGUIMain mainGUI = new CGUIMain(guiControl, listVoices );	

		//player.setListener(new CTGListener(mainGUI));

	}
}
