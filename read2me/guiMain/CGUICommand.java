package guiMain;

import textToSpeech.*;

public class CGUICommand implements CGUICommandInterface{
	
	final CPlayerInterface player;
	private String text;
	
	public CGUICommand()
	{
		
		player = new CPlayer();   
		player.createSynthesizers();
		player.setSynthesizer(1);
	}
	
	public void backParagraph()
	{
		System.out.println("back paragraph");
	}
	
	public void backSentence()
	{
		System.out.println("back sentence");
	}
	
	public void nextSentence(){
		System.out.println("back sentence");
	}
	
	public void nextParagraph(){
		System.out.println("next paragraph");
	}
	
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
	
	public boolean stop(){
		player.stop();
        player.resume();
        return false;
	}
	
	public void mp3(){
		System.out.println("MP3 Converter");
	}
	
	public void tip(){
		
	}
	
	public void volume(int _value){
		player.setVolume(_value);
	}
	
	public void speed(int _value){
		player.setSpeakingSpeed(_value);
	}

	public void setText(String _text)
	{
		text = _text;
	}
}
