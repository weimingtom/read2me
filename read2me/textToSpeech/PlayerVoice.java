package textToSpeech;

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
