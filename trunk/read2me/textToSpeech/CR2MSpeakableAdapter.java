package textToSpeech;

import javax.speech.synthesis.SpeakableAdapter;
import javax.speech.synthesis.SpeakableEvent;

public class CR2MSpeakableAdapter extends SpeakableAdapter{
	
	public void wordStarted(SpeakableEvent e){
		System.out.println("word started " + e.getWordStart());
	}

}
