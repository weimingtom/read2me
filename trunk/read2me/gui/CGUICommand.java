package gui;

import textToSpeech.*;
import java.util.*;
//import java.text.*;
import javax.swing.ListModel;

public class CGUICommand implements CGUICommandInterface{
	
	/** Interface to access the player's functions */
	private final CPlayerInterface player;
	/** Text that has been typed in the text area */
	private String text;
	/** Sentence number that is currently read (First sentence of the text is number 0 */
	private int currentIndex = 0;
	/** Paragraph number that is currently read (First paragraph of the text is number 0) */
	private int currentIndexParagraph = 0;
	/** position of the caret in the text area */
	private int position;
	/**  */
	private int indexOfSentence = 0;
	/** Vector containing for each sentence, the begining and the end position */
	private Vector<CSentence> listOfSentence;
	/** Vector containing for each paragraph, the begining and the end position */
	private Vector<CSentence> listOfParagraph;
	/** Variable used to let the GuiMain class to update the highlight of the text*/
	private boolean needUpdate = false;
	/** Variable used to let the GuiMain class to go in stop mod */
	private boolean needToStop = false;
	/** Object that contains the text that the player has to read */
	private CSpeechObject speech;
	/** Reference to the GuiMain class */
	private CGUIMain guiMain;
	/** to know if we are currently reading text or not */
	private boolean isPlaying;
	/** Object containing all the voices */
	private ListModel voices;
	/** Index of the voice that is currently used */
	private int voiceIndex;
		 
	/**
	 * Constructor that initialize the player object and the voice object
	 * @param _player Interface to the Text To Speech player
	 */
	public CGUICommand(final CPlayerInterface _player)
	{
		player = _player;
		player.setListener(new CTGListener(this));
		player.setSAPIListener(new CTSListener(this));
		listOfSentence = new Vector<CSentence>(50,1);
		listOfParagraph = new Vector<CSentence>(10,1);
	
		voices = player.getVoiceList();
		voiceIndex = 1;
		
		player.setVoice(voiceIndex);
	}
	
	/** reference the GuiMain class to the command class */
	public void setGUIMain(CGUIMain _gui)
	{
		guiMain = _gui;
	}
	
	/** return a variable that tells the MainGui if it needs to stop */
	public boolean getNeedToStop()
	{
		return needToStop;
	}
	
	/** The gui is in stop mod and so it reinitializes the variable */
	public void setNeedToStop()
	{
		needToStop = false;
	}
	
	/** tells the gui if it needs to update text area and images */
	public boolean getNeedUpdate()
	{
		return needUpdate;
	}
	/** the Gui updated the display so we reinitialize */
	public void setNeedUpdate()
	{
		needUpdate = false;
	}
	
	/** Function that gets called when a sentence has been finished to be read */
	public void eventEndSpeak()
	{
		currentIndex = indexOfSentence;
		if(indexOfSentence != listOfSentence.size()-1)
			indexOfSentence++;
		currentIndexParagraph = getParagraphNumber(indexOfSentence);
		needUpdate = true;
		guiMain.updateDisplay();
		//updateSpeechObject();
		if(currentIndex != indexOfSentence)
		{
			play(false);
		}
		else
		{   
			needToStop = true;
			guiMain.updateDisplay();
			isPlaying = false;
			stop();
		}
		// comes here only when it has finish to read everything
		currentIndex = indexOfSentence;
	}
	
	
	/**
	 * Goes one paragraph back
	 */
	public void backParagraph()
	{
		if(currentIndexParagraph != 0)
			currentIndexParagraph--;
		indexOfSentence = getSentenceNumber(currentIndexParagraph);
		if(isPlaying)
		{
			player.cancel();
			play(false);
		}
	}
	
	/**
	 * Goes one sentence back from the current position
	 * And update the current position
	 */
	public void backSentence()
	{
		if(indexOfSentence != 0)
			indexOfSentence--;
		currentIndexParagraph = getParagraphNumber(indexOfSentence);
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
		
		if(indexOfSentence != listOfSentence.size()-1)
			indexOfSentence++;
		currentIndexParagraph = getParagraphNumber(indexOfSentence);
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
		if(currentIndexParagraph != listOfParagraph.size()-1)
			currentIndexParagraph++;
		indexOfSentence = getSentenceNumber(currentIndexParagraph);
		if(isPlaying)
		{
			player.cancel();
			play(false);
		}
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
			{
				updateSpeechObject();   // set the new sentence
				player.addSpeech(speech);  // update the player
				player.play(speech);  // play the sentence
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

		isPlaying = false;
		player.resume();
		player.stop();
        //player.resume();
        currentIndex = 0;
        //endOfText = false;
        return false;
	}
	
	/**
	 * Convert the text from the current position to the end into a MP3 file
	 */
	public void mp3(){
		//playerVoice = (PlayerVoice)voices.getElementAt(voiceIndex);
		CExportDialog exportDialog = new CExportDialog(text, (PlayerVoice)voices.getElementAt(voiceIndex));
		exportDialog.createDialog(guiMain.s);
	}
	
	/**
	 * Set the volume of the player
	 */
	public void volume(int _value){
		player.setVolume(_value);
	}
	
	/** Set the reading speed of the player */
	public void speed(int _value)
	{
		player.setSpeakingSpeed(_value);
	}

	/** Parse the text in order to separate each sentences
	 * A sentence is defined when it ends with a . or ? or ! or ( or )
	 * Finally update an array where all the index for the begining and and of sentences are written
	 */
	private void parseText()
	{
		/*String str = text;
		BreakIterator brkit = BreakIterator.getSentenceInstance();
		//BreakIterator brkit = BreakIterator.getLineInstance();
        brkit.setText(str);

        // iterate across the string

        int start = brkit.first();
        int end = brkit.next();
        while (end != BreakIterator.DONE) {
            String sentence = str.substring(start, end);
            System.out.println(start + " " + sentence + " " + (end-1));
            start = end;
            end = brkit.next();
        }*/
        
		listOfSentence.clear();
		listOfParagraph.clear();
		CSentence temp;
		int index1=0, index2=0;
		int indexParag1=0, indexParag2=0;
		for(int i=0; i<text.length(); i++)
		{
			// update the sentence vector
			if(text.charAt(i)== '.' || text.charAt(i)== '?' || text.charAt(i)== '!' || text.charAt(i)== '(' || text.charAt(i)==')')
			{
				index2 = i;
				int test = index2 - index1;
				if(test >1 || (test == 1 && index1 == 0))
				{
					//if(i == text.length()-1 || (i != 0 && i!=1 && i != text.length()-1))// && text.charAt(i+1) == ' ' && text.charAt(i-2) !='.'))
					//{
						if(index1 == 0)
							temp = new CSentence(index1,index2);
						else
							temp = new CSentence(index1+1,index2);
						index1 = index2;
						listOfSentence.add(temp);
					//}			
				}
				index1=i;
			}
			// update the paragraph vector
			if(text.charAt(i) == '\n')
			{
				indexParag2 = i;
				temp = new CSentence(indexParag1,indexParag2);
				indexParag1 = indexParag2+1;
				listOfParagraph.add(temp);
			}
		}
		
		// no point has been found
		if(index1 == 0 && index2==0)
		{
			temp = new CSentence(0,text.length());
			listOfSentence.add(temp);
		}
		
		// a point is not at the end of the text
		if(index2 != text.length()-1 && index1 !=0 && index2 !=0)
		{
			if(index1 != text.length())
			{
				temp = new CSentence(index1+1,text.length());
				listOfSentence.add(temp);
			}
		}
		
		// If there are no '\n' character, the text is considered as a paragraph
		if(indexParag2 == 0 && indexParag1 == 0)
		{
			
			listOfParagraph.add(new CSentence(0, text.length()));
		}
		
		// Consider that the last character is a \n even if it doesn't exist
		if(listOfParagraph.elementAt(listOfParagraph.size()-1).end != text.length()-1  && indexParag2 != 0 && indexParag1 != 0)
		{
			if(indexParag1 != text.length())
				listOfParagraph.add(new CSentence(indexParag1, text.length()));
		}
		
		// Don't consider when there are several blank lines
		for(int i=0; i<listOfParagraph.size(); i++)
		{
			if(text.length()!=1 && listOfParagraph.elementAt(i).begin == listOfParagraph.elementAt(i).end-1)
				listOfParagraph.remove(i--);
		}
	}
	
	
	/**
	 * Set the text variable with the entire text from the Gui and parse it to update the sentence and
	 * paragraph arrays
	 */
	public void setText(String _text)
	{
		text = _text;
		parseText();
	}
	
	/**
	 * Set the position of the cursor in the text area
	 * and determine the sentence that needs to be read and also the paragraph we are in.
	 */
	public void setPosition(int _pos)
	{
		position = _pos;
		
		// we update the sentence index
		for(int i=0; i<listOfSentence.size(); i++)
		{
			if(position >= listOfSentence.elementAt(i).begin && position <= listOfSentence.elementAt(i).end)
				indexOfSentence = i;
			else if(i != listOfSentence.size()-1 && position > listOfSentence.elementAt(i).end && position < listOfSentence.elementAt(i+1).begin)
				indexOfSentence = i+1;
			else if(position < listOfSentence.elementAt(0).begin)
				indexOfSentence = 0;
		}
		currentIndex = indexOfSentence;
		
		// Then we update the Paragraph index
		currentIndexParagraph = getParagraphNumber(position);
		
		/*for(int i=0; i<endOfParagraph.size(); i++)
		{
			if(position >= endOfParagraph.elementAt(i).begin && position <= endOfParagraph.elementAt(i).end)
				currentIndexParagraph = i;
			else if(i != endOfParagraph.size()-1 && position > endOfParagraph.elementAt(i).end && position < endOfParagraph.elementAt(i+1).begin)
				currentIndexParagraph = i+1;
			else if(position < endOfParagraph.elementAt(0).begin)
				currentIndexParagraph = 0;
		}*/
		
		//System.out.println(currentIndexParagraph);
	}
	
	/**
	 * Function that tells us the sentence the caret is in
	 * @return the starting and the ending indices to highlight the sentence
	 * knowing the actual position of the cursor
	 */
	public int[] getSentence()
	{
		int[] temp = new int[2];
		temp[0] = listOfSentence.elementAt(indexOfSentence).begin;
		temp[1] = listOfSentence.elementAt(indexOfSentence).end;
		return temp;
	}
	
	/**
	 * Create a new speech object using the next sentence that is going to be read
	 * And set the voice for the player
	 */
	private void updateSpeechObject()
	{
		// set the text that needs to be read
		speech = CSpeechObject.createTextSpeech(text.substring(getSentence()[0], getSentence()[1]));
		
		// Part to deal with the 2 different players:
		// freetts (mode 1) requires a cancel but MS SAPI doesn't
		ListModel temp = player.getVoiceList();
		PlayerVoice temp2 = (PlayerVoice)temp.getElementAt(voiceIndex);
		if(temp2.getMode() == 1)
			player.cancel();
		
		// set the correct voice
		player.setVoice(voiceIndex);
	}
	
	/**
	 * Knowing the position of the cursor, 
	 * it returns the index of the begining of the first sentence of the paragraph
	 * @param _t position of the sursor
	 * @return integer the index of the first sentence of that paragraph
	 */
	private int getParagraphNumber(int _t)
	{
		for(int i=0; i<listOfParagraph.size(); i++)
		{
			if(_t >= listOfParagraph.elementAt(i).begin && _t <= listOfParagraph.elementAt(i).end)
				return i;
			else if(i != listOfParagraph.size()-1 && _t > listOfParagraph.elementAt(i).end && _t < listOfParagraph.elementAt(i+1).begin)
				return i+1;
			else if(_t < listOfParagraph.elementAt(0).begin)
				return 0;
		}
		return 0;
	}
	
	/**
	 * get the index of the first sentence of the paragraph given by the parameter t
	 * @param _t paragraph number
	 * @return the index of the first sentence
	 */
	private int getSentenceNumber(int _t )
	{
		int temp = listOfParagraph.elementAt(_t).begin;
		for(int i=0; i<listOfSentence.size(); i++)
		{
			if(temp >= listOfSentence.elementAt(i).begin && temp <= listOfSentence.elementAt(i).end)
				return i;
			else if(i != listOfSentence.size()-1 && temp > listOfSentence.elementAt(i).end && temp < listOfSentence.elementAt(i+1).begin)
				return i+1;
			else if(temp < listOfSentence.elementAt(0).begin)
				return 0;
		}
		return 0;
	}
	
	/** Set the voice index and so the player will load the right voice */
	public void setVoiceIndex(int _v)
	{
		voiceIndex = _v;
	}
}

