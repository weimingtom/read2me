package textToSpeech;

/**
 * 
 * @author Stefan Estrada
 *
 * Object used to store Voice properties including
 * voice name, mode (whether it is a FreeTTS or MS Sapi voice),
 * and index (where in the voice list it is placed).
 */
public class PlayerVoice {

	private String name;
	private int mode;
	private int index;
	
    public PlayerVoice(String name, int mode, int index) {
    	this.name = name;
    	this.mode = mode;
    	this.index = index;
    }
    
    public String getName() {
    	return name;
    }
    
    public int getIndex() {
    	return index;
    }
    
    public int getMode(){
    	return mode;
    }
}
