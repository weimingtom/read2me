/*
This file is part of Read2Me!

Read2Me! is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Read2Me! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Read2Me!. If not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package mainInitialization;

import textToSpeech.*;
import gui.CGUICommand;
import gui.CGUIMain;
import gui.CGUICommandInterface;
import javax.swing.ListModel;
import textToSpeech.PlayerVoice;

/**
 * This class is the main entry for the Read2Me! program
 * All the initializations take place in this class
 * The user will be redirected from here to the place he needs to go
 *
 */
public class CMainInit {
	

	public static void main(String[] a){
		System.out.println("Starting Read2Me!");
		//System.out.println(System.getProperty("os.name"));
		
		// Text To Speech players (Free TTS / MS SAPI) initialization
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
	}
}
