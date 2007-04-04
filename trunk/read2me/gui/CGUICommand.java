package gui;

import textToSpeech.*;
import java.util.*;

public class CGUICommand implements CGUICommandInterface{
	
	private final CPlayerInterface player;
	private String text;
	private String textToRead;
	private int currentIndex = 0;
	private int position;
	private int indexOfSentence = 0;
	private Vector<CSentence> endOfSentence;
	private boolean needUpdate = false;
	private boolean needToStop = false;
	private boolean endOfText = false;
	private CSpeechObject speech;
	private CGUIMain guiMain;
	private boolean isPlaying;
	private boolean isConverting = false;
	 
	/**
	 * Constructor for the command class if we use the FreeTTS player.
	 * @param _player Interface with the FreeTTs player that has been initialised in the main Initialisation class
	 */
	public CGUICommand(final CPlayerInterface _player)
	{
		player = _player;
		player.setListener(new CTGListener(this));
		endOfSentence = new Vector<CSentence>(50,1);
	}
	
	public void setGUIMain(CGUIMain _gui)
	{
		guiMain = _gui;
	}
	
	public boolean getNeedToStop()
	{
		return needToStop;
	}
	public void setNeedToStop()
	{
		needToStop = false;
	}
	
	public boolean getNeedUpdate()
	{
		return needUpdate;
	}
	public void setNeedUpdate()
	{
		needUpdate = false;
	}
	public void eventEndSpeak()
	{
		//nextSentence();
		if(indexOfSentence != endOfSentence.size()-1)
			indexOfSentence++;
		needUpdate = true;
		guiMain.updateListeners();
		//System.out.println("phrase a higllité: "+getSentence()[0]+ "  "+getSentence()[1]);
		updateSpeechObject();
		if(currentIndex != indexOfSentence)
		{
			//endOfText = true;
			//System.out.println("is going to play the next sentence");
			play(false);
		}
		else
		{

			needToStop = true;
			guiMain.updateListeners();
			isPlaying = false;
			stop();
		}
		currentIndex = indexOfSentence;
		
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
		if(indexOfSentence != 0)
			indexOfSentence--;
		if(isPlaying)
		{
			player.cancel();
			play(false);
		}
	}
	
	/**
	 * Goes to the next sentence and update the current position
	 */
	public void nextSentence(){
		
		if(indexOfSentence != endOfSentence.size()-1)
			indexOfSentence++;
		if(isPlaying)
		{
			player.cancel();
			play(false);
		}
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
		
			if(_isPlaying == false) // player is in pause mode and we go to play mode
			{
				if(player.isPaused())
				{
					player.resume();
				}
				else
				{ // just to this part when we first press play
					//CSpeechObject speech = CSpeechObject.createTextSpeech(text);
					updateSpeechObject();
					player.addSpeech(speech);
					player.play(speech);
				}
				isPlaying = true;
				return true;
			}  
			else  // player is in play mode and we pause it
			{
				player.pause();
				isPlaying = false;
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
        endOfText = false;
        return false;
	}
	
	/**
	 * Convert the text from the current position to the end into a MP3 file
	 */
	public void mp3(){
		CExportDialog exportDialog = new CExportDialog(text);
		exportDialog.createDialog(guiMain.s);
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

	private void parseText()
	{
		endOfSentence.clear();
		CSentence temp;
		int index1=0, index2=0;
		for(int i=0; i<text.length(); i++)
		{
			if(text.charAt(i)== '.' || text.charAt(i)== '?' || text.charAt(i)== '!')
			{
				index2 = i;
				temp = new CSentence(index1,index2);
				index1 = index2+1;
				endOfSentence.add(temp);
			}		
		}
		
		// no point has been found
		if(index1 == 0 && index2==0)
		{
			temp = new CSentence(0,text.length());
			endOfSentence.add(temp);
		}
		// a point is not at the end of the text
		if(index2 != text.length()-1 && index1 !=0 && index2 !=0)
		{
			if(index1 != text.length())
			{
				temp = new CSentence(index1,text.length());
				endOfSentence.add(temp);
			}
		}
		
		for(int i=0; i<endOfSentence.size(); i++)
		{
			System.out.println(endOfSentence.elementAt(i).begin+" end: "+endOfSentence.elementAt(i).end);
		}
	}
	
	
	/**
	 * Pass the entire text to the command function in order to be processed
	 */
	public void setText(String _text)
	{
		text = _text;
		parseText();
	}
	
	/**
	 * Set the position of the cursor in the text area
	 */
	public void setPosition(int _pos)
	{
		position = _pos;
		for(int i=0; i<endOfSentence.size(); i++)
		{
			if(position >= endOfSentence.elementAt(i).begin && position <= endOfSentence.elementAt(i).end)
				indexOfSentence = i;
		}
		
		//System.out.println("position of the curcor: "+_pos+ "  "+indexOfSentence );
	}
	
	/**
	 * @return the starting and the ending indices to highlight the sentence
	 */
	public int[] getSentence()
	{
		int[] temp = new int[2];
		temp[0] = endOfSentence.elementAt(indexOfSentence).begin;
		temp[1] = endOfSentence.elementAt(indexOfSentence).end;
		return temp;
	}
	
	private void updateSpeechObject()
	{
		speech = CSpeechObject.createTextSpeech(text.substring(getSentence()[0], getSentence()[1]));
	}
}
