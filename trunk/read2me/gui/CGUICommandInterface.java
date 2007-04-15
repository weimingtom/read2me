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
	 * Displays the help windows to the user.
	 * It's a HTML pages that contains all the shortcuts
	 *
	 */
	public void tip();
	
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
	
	
	public void setGUIMain(CGUIMain _gui);
	
	public boolean getNeedToStop();
	public void setNeedToStop();
	public void setVoiceIndex(int _v);

}
