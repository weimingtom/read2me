package gui;

import textToSpeech.*;

public class CTSListener implements CSapiListener{
	
	private CGUICommand GUIMain;
	
	public CTSListener(CGUICommand _GUIMain){
		GUIMain = _GUIMain;
	}
	
	public void started(){
		System.out.println("Started MS SAPI Sentence");
	}
	
	public void paused(){
		System.out.println("MS SAPI Paused");
	}
	
	public void resumed(){
		System.out.println("MS SAPI Resumed");
	}
	
	public void cancelled(){
		System.out.println("Cancelled MS SAPI Sentence");
	}
	
	public void finished(){
		System.out.println("Finished MS SAPI Sentence");
		GUIMain.eventEndSpeak();
	}

}
