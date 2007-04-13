package textToSpeech;

public class PlayerVoice {

	private String name;
	private int mode;
	
    public PlayerVoice(String name, int mode) {
    	this.name = name;
    	this.mode = mode;
    }
    
    public String getName() {
    	return name;
    }
    
    public int getMode(){
    	return mode;
    }
}
