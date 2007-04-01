package gui;

import textToSpeech.*;

public class CGUICommand implements CGUICommandInterface{
	
	private final CPlayerInterface player;
	private String text;
	private int currentIndex = 0;
	 
	/**
	 * Constructor for the command class if we use the FreeTTS player.
	 * @param _player Interface with the FreeTTs player that has been initialised in the main Initialisation class
	 */
	public CGUICommand(final CPlayerInterface _player)
	{
		player = _player;
		/*player = new CPlayer();   
		player.createSynthesizers();
		player.setSynthesizer(1);*/
	}
	
	/**
	 * Goes one paragraph back
	 */
	public void backParagraph()
	{
		System.out.println("back paragraph");
		
	}
	
	/**
	 * Goes one sentence back from the current position
	 * And update the current position
	 */
	public void backSentence()
	{
		System.out.println("back sentence");
		
		int oldIndex = currentIndex;
		String previous_text = text.substring(0, oldIndex);
		
		for(int i=previous_text.length()-1; i>=0; i--)
		{
			if(previous_text.charAt(i)== '.')
			{
				text = text.substring(i+1);
				currentIndex = i;
				break;
			}
			if(i == 0)
				currentIndex = 0;
		}
			
		System.out.println("Sentence back: "+text+ " current:: "+currentIndex);
	}
	
	/**
	 * Goes to the next sentence and update the current position
	 */
	public void nextSentence(){
		System.out.println("next sentence");
		
		int length = text.length();
		int oldIndex = currentIndex;
		String old_text = text.substring(oldIndex+1);
		boolean end = false;
		
		for(int i=currentIndex+1; i<length; i++)
		{
			if(text.charAt(i)== '.')
			{
				text = text.substring(i+1);
				currentIndex = i;
				break;
			}
			if(i == length-1)
				end = true;
		}
		//System.out.println("oldindex: "+oldIndex+ " current: "+currentIndex);
		if(currentIndex+1 == length || end==true)
		{
			text = old_text;
			currentIndex = oldIndex;
		}
			
		System.out.println("Sentence: "+text+ " current:: "+currentIndex);
	}
	
	/**
	 * Goes to the next paragraph
	 */
	public void nextParagraph(){
		System.out.println("next paragraph");
	}
	
	/**
	 * Play the text from the current position of the cursor to the end of the text
	 */
	public boolean play(boolean _isPlaying){
		if(_isPlaying == false) // player is in play mode
        {
        	//String text = getText();
    		//System.out.println(text);
    		if(player.isPaused())
        	{
        		player.resume();
        	}
        	else
        	{ // just to this part when we first play play
        		CSpeechObject speech = CSpeechObject.createTextSpeech(text);
        		player.addSpeech(speech);
        		player.play(speech);
        	}
        	return true;
        }  
        else  // player is in pause
        {
        	player.pause();
    		
        	return false;
        }
	}
	
	/**
	 * Stop playing the text and set the cursor to the beginning of the text
	 */
	public boolean stop(){
		player.stop();
        player.resume();
        currentIndex = 0;
        return false;
	}
	
	/**
	 * Convert the text from the current position to the end into a MP3 file
	 */
	public void mp3(){
		//System.out.println("MP3 Converter");
		CExportDialog exportDialog = new CExportDialog(text);
		exportDialog.createDialog();
	}
	
	
	public void tip(){
		
	}
	
	/**
	 * Set the volume of the player
	 */
	public void volume(int _value){
		player.setVolume(_value);
	}
	
	/**
	 * Set the reading speed of the text
	 */
	public void speed(int _value){
		player.setSpeakingSpeed(_value);
	}

	/**
	 * Pass the entire text to the command function in order to be processed
	 */
	public void setText(String _text)
	{
		text = _text;
	}
}
