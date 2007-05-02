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

package gui;

/**
 * Defines the model for interacting between the GUI and the player (FreeTTS or MS SAPI)
 * Each button on the GUI calls the fonction that is associated with it 
 * 
 */
public interface CGUICommandInterface {
	 
	/**
	 * Performs an update of the text that will be read:
	 * Moves the cursor one paragraph back and start reading.
	 * If there is no previous paragraph, it goes to the beginning of the current one.
	 *
	 */
	public void backParagraph();
	
	/**
	 * Performs an update of the text that will be read.
	 * Moves the cursor one sentence back and start reading
	 * If there is no sentence back, it goes to the beginning of the current sentence
	 */
	public void backSentence();
	
	/**
	 * Performs an update of the text that will be read.
	 * Moves the cursor one sentence further and start reading
	 * If there is no next sentence, it does not do anything
	 */
	public void nextSentence();
	
	/**
	 * Performs an update of the text that will be read.
	 * Moves the cursor one paragraph further and start reading
	 * If there is no next paragraph, it does not do anything
	 */
	public void nextParagraph();
	
	/**
	 * Perfoms the reading or the pausing of the text depending if we are reading or if we are in pause.
	 * 
	 * @param _isPlaying boolean that says if the text is currently playing or not.
	 * @return if the text is currently playing or not
	 */
	public boolean play(boolean _isPlaying);
	
	/**
	 * Performs a stop of the text-to-speeh engine.
	 * @return false
	 */
	public boolean stop();
	
	/**
	 * Converts the current text that is read to a MP3 file using the setting defined by the user or the default ones
	 *
	 */
	public void mp3();
	
	/**
	 * Sets the volume of the player to a specified value
	 * @param _value  Volume value
	 */
	public void volume(int _value);
	
	/**
	 * Sets the reading speed of the player to a specified value
	 * @param _value  Speed value
	 */
	public void speed(int _value);
	
	/**
	 * Updates the text that needs to be read
	 * @param _text Text that needs to be read
	 */
	public void setText(String _text);
	
	/**
	 * Updates the guiControl with the current position of the cursor in the text area
	 * @param _pos Position of the cursor in the text
	 */
	public void setPosition(int _pos);
	
	/**
	 * Get the starting and ending indices of the sentence which is read
	 * @return the start and the end indice of the current sentence
	 */
	public int[] getSentence();
	
	/**
	 * The GUI will update the selection according to a parameter
	 * @return a boolean to make the GUI know if it needs to update the selection or not
	 */
	public boolean getNeedUpdate();
	
	/**
	 * The GUI calls this function when he just updated the text
	 */
	public void setNeedUpdate();
	
	/** To make the guiMain and the GuiCommand class interact with each other */
	public void setGUIMain(CGUIMain _gui);
	
	/** Allows the guiMain to know if it has to go in stop mode */
	public boolean getNeedToStop();
	
	/** reinitialise the variable in the GuiCommand class */
	public void setNeedToStop();
	
	/** Specify the right voice to the GuiCommand class */
	public void setVoiceIndex(int _v);

}
