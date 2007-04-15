package gui;

import textToSpeech.*;
import java.util.*;
import java.text.*;
import javax.swing.ListModel;

public class CGUICommand implements CGUICommandInterface{
	
	private final CPlayerInterface player;
	private String text;
	//private String textToRead;
	private int currentIndex = 0;
	private int currentIndexParagraph = 0;
	private int position;
	private int indexOfSentence = 0;
	private Vector<CSentence> endOfSentence;
	private Vector<CSentence> endOfParagraph;
	private boolean needUpdate = false;
	private boolean needToStop = false;
	//private boolean endOfText = false;
	private CSpeechObject speech;
	private CGUIMain guiMain;
	private boolean isPlaying;
	private ListModel voices;
	private int voiceIndex;
	private PlayerVoice playerVoice;
	//private boolean isConverting = false;
		 
	/**
	 * Constructor for the command class if we use the FreeTTS player.
	 * @param _player Interface with the FreeTTs player that has been initialised in the main Initialisation class
	 */
	public CGUICommand(final CPlayerInterface _player)
	{
		player = _player;
		player.setListener(new CTGListener(this));
		endOfSentence = new Vector<CSentence>(50,1);
		endOfParagraph = new Vector<CSentence>(10,1);
		
		
		
		//voices = player.getVoiceList();
		voiceIndex = 1;
		//voices.getSize();
		//playerVoice = (PlayerVoice)voices.getElementAt(voiceIndex);
		//playerVoice.getName();
		
		player.setVoice(voiceIndex);
		//player.setVoice(0);
		
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
		currentIndex = indexOfSentence;
		if(indexOfSentence != endOfSentence.size()-1)
			indexOfSentence++;
		currentIndexParagraph = getParagraphNumber(indexOfSentence);
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
		// comes here only when it has finish to read everything
		currentIndex = indexOfSentence;
		
	}
	
	
	/**
	 * Goes one paragraph back
	 */
	public void backParagraph()
	{
		System.out.println("back paragraph");
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
		
		if(indexOfSentence != endOfSentence.size()-1)
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
		//System.out.println("next paragraph");
		if(currentIndexParagraph != endOfParagraph.size()-1)
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

		isPlaying = false;
		
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
        
		endOfSentence.clear();
		endOfParagraph.clear();
		CSentence temp;
		int index1=0, index2=0;
		int indexParag1=0, indexParag2=0;
		for(int i=0; i<text.length(); i++)
		{
			if(text.charAt(i)== '.' || text.charAt(i)== '?' || text.charAt(i)== '!')
			{
				index2 = i;
				int test = index2 - index1;
				if(test >1)
				{
					if(i == text.length()-1 || (i != 0 && i!=1 && i != text.length()-1))// && text.charAt(i+1) == ' ' && text.charAt(i-2) !='.'))
					{
						temp = new CSentence(index1,index2);
						index1 = index2+1;
						endOfSentence.add(temp);
					}			
				}
				else
					index1=i;
			}
			if(text.charAt(i) == '\n')
			{
				indexParag2 = i;
				temp = new CSentence(indexParag1,indexParag2);
				indexParag1 = indexParag2+1;
				endOfParagraph.add(temp);
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
		
		// If there are no '\n' character, the text is considered as a paragraph
		if(indexParag2 == 0 && indexParag1 == 0)
		{
			endOfParagraph.add(new CSentence(0, text.length()));
		}
		
		// Consider that the last character is a \n even if it doesn't exist
		if(endOfParagraph.elementAt(endOfParagraph.size()-1).end != text.length()-1  && indexParag2 != 0 && indexParag1 != 0)
		{
			if(indexParag1 != text.length())
				endOfParagraph.add(new CSentence(indexParag1, text.length()));
		}
		
		// Don't consider when there are several blank lines
		for(int i=0; i<endOfParagraph.size(); i++)
		{
			if(endOfParagraph.elementAt(i).begin == endOfParagraph.elementAt(i).end-1)
				endOfParagraph.remove(i--);
		}
		
		
		
		/*for(int i=0; i<endOfSentence.size(); i++)
		{
			System.out.println(endOfSentence.elementAt(i).begin+" end: "+endOfSentence.elementAt(i).end);
		}
		for(int i=0; i<endOfParagraph.size(); i++)
		{
			System.out.println(endOfParagraph.elementAt(i).begin+" end: "+endOfParagraph.elementAt(i).end);
		}*/
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
		
		// we update the sentence index
		for(int i=0; i<endOfSentence.size(); i++)
		{
			if(position >= endOfSentence.elementAt(i).begin && position <= endOfSentence.elementAt(i).end)
				indexOfSentence = i;
			else if(i != endOfSentence.size()-1 && position > endOfSentence.elementAt(i).end && position < endOfSentence.elementAt(i+1).begin)
				indexOfSentence = i+1;
			else if(position < endOfSentence.elementAt(0).begin)
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
		player.cancel();
		player.setVoice(voiceIndex);
	}
	
	private int getParagraphNumber(int _t)
	{
		for(int i=0; i<endOfParagraph.size(); i++)
		{
			if(_t >= endOfParagraph.elementAt(i).begin && _t <= endOfParagraph.elementAt(i).end)
				return i;
			else if(i != endOfParagraph.size()-1 && _t > endOfParagraph.elementAt(i).end && _t < endOfParagraph.elementAt(i+1).begin)
				return i+1;
			else if(_t < endOfParagraph.elementAt(0).begin)
				return 0;
		}
		return 0;
	}
	
	private int getSentenceNumber(int _t )
	{
		int temp = endOfParagraph.elementAt(_t).begin;
		for(int i=0; i<endOfSentence.size(); i++)
		{
			if(temp >= endOfSentence.elementAt(i).begin && temp <= endOfSentence.elementAt(i).end)
				return i;
			else if(i != endOfSentence.size()-1 && temp > endOfSentence.elementAt(i).end && temp < endOfSentence.elementAt(i+1).begin)
				return i+1;
			else if(temp < endOfSentence.elementAt(0).begin)
				return 0;
		}
		return 0;
	}
	
	public void setVoiceIndex(int _v)
	{
		voiceIndex = _v;
		System.out.println("guicommand: "+ voiceIndex);
	}
}

