package mainInitialization;

import textToSpeech.*;
import gui.*;

/**
 * This class is main entry for the Read2Me! program
 * All the initializations will take place in this class
 * The user will be redirected from here to the place he needs to go
 *
 */
public class CMainInit {
	

	public static void main(String[] a){
		System.out.println("Starting Read2Me!");
		System.out.println(System.getProperty("os.name"));
		
		
		// Free TTS player
		final CPlayerInterface player;
		player = new CPlayer();   
		player.createSynthesizers();
		player.setSynthesizer(1);
		
		// Initialize the command control for the GUI
		CGUICommandInterface guiControl = new CGUICommand(player);
		
		// Display the GUI
		@SuppressWarnings("unused")
		CGUIMain mainGUI = new CGUIMain(guiControl);	

		//player.setListener(new CTGListener(mainGUI));

	}
}
