package guiMain;

import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;

public class CTGListener implements SpeakableListener {

    private CGUIMain GUIMain;
	
	public CTGListener(){}
    
    public CTGListener(CGUIMain GUIMain){
    	this.GUIMain = GUIMain;
    }
	
	private String formatEvent(SpeakableEvent event) {
        return event.paramString()+": "+event.getSource();        
    }
    
    public void markerReached(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableCancelled(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableEnded(SpeakableEvent event) {
        GUIMain.eventEndSpeak();
        System.out.println(formatEvent(event));
    }

    public void speakablePaused(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableResumed(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableStarted(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void topOfQueue(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void wordStarted(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }
}