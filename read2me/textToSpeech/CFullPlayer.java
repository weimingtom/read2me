package textToSpeech;

import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;

public class CFullPlayer implements CPlayerInterface {
	
	private CPlayer freeTTSPlayer;
	private CSapiControl sapiPlayer;
	private int mode;
	//mode = 1 : freeTTS
	//mode = 2 : SAPI
	private DefaultComboBoxModel voiceList;
	
	public CFullPlayer(){
		
		freeTTSPlayer = new CPlayer();
		sapiPlayer = new CSapiControl();
		mode = 1;
		voiceList = new DefaultComboBoxModel();
		
	}
	
	/*public void setMode(int m){
		if(m == 1 || m == 2){
			mode = m;
		}
		else mode = 1;
	}
	
	public int getMode(){
		return mode;
	}*/
	
	public void createSynthesizers() {
		freeTTSPlayer.createSynthesizers();
		sapiPlayer.init();
		for(int i = 0; i < sapiPlayer.getTotalVoices(); i++){
			 PlayerVoice pVoice = new PlayerVoice(sapiPlayer.getVoiceName(i), 2);
			 voiceList.addElement(pVoice);
		 }
		ListModel freeTTSVoice = freeTTSPlayer.getVoiceList();
		for(int i = 0; i < freeTTSVoice.getSize(); i++){
			 MyVoice voiceObject = (MyVoice) freeTTSVoice.getElementAt(i);
			 PlayerVoice pVoice = new PlayerVoice(voiceObject.getName(), 1);
			 voiceList.addElement(pVoice);
		}
	}
	
	public void setListener(SpeakableListener tgListener){
    	freeTTSPlayer.setListener(tgListener);
    }
	
	public void play(CSpeechObject speech) {
		if(mode == 1)
			freeTTSPlayer.play(speech);
		else {
			sapiPlayer.play(speech.getText());
		}
	}
	
	public void play(int index) {
		freeTTSPlayer.play(index);
	}
	
	public synchronized boolean isPaused() {
		return freeTTSPlayer.isPaused();
	}
	
	public synchronized void pause() {
		if(mode == 1)
			freeTTSPlayer.pause();
		else
			sapiPlayer.pause();
	}
	
	public synchronized void resume() {
		if(mode == 1)
			freeTTSPlayer.resume();
		else
			sapiPlayer.resumeSP();
	}
	
	public synchronized void stop() {
		freeTTSPlayer.stop();
	}
	
	public void cancel() {
		freeTTSPlayer.cancel();
	}
	
	public void close() {
		freeTTSPlayer.close();
	}
	
	public void setSynthesizer(int index) {
		freeTTSPlayer.setSynthesizer(index);
	}
	
	public void setVoice(int index) {
		PlayerVoice pVoice = (PlayerVoice) voiceList.getElementAt(index);
		mode = pVoice.getMode();
		if(mode == 1)
			freeTTSPlayer.setVoice(index-sapiPlayer.getTotalVoices());
		else
			sapiPlayer.setVoice(index);
	}
	
	public float getVolume() {
		if(mode == 1)
			return freeTTSPlayer.getVolume();
		else
			return sapiPlayer.getVolume();
	}
	
	 public boolean setVolume(float volume) {
		 if(mode == 1)
			 return freeTTSPlayer.setVolume(volume);
		 else
			 return sapiPlayer.setVolume(volume);
	 }
	 
	 public float getSpeakingSpeed() {
		 if(mode == 1)
			 return freeTTSPlayer.getSpeakingSpeed();
		 else
			 return sapiPlayer.getSpeed();
	 }
	 
	 public boolean setSpeakingSpeed(float wordsPerMin) {
		 if(mode == 1)
			 return freeTTSPlayer.setSpeakingSpeed(wordsPerMin);
		 else
			 return sapiPlayer.setSpeed(wordsPerMin);
	 }
	 
	 public void setVoiceList(SynthesizerModeDesc modeDesc) {
		 freeTTSPlayer.setVoiceList(modeDesc);
	 }
	 
	 public ListModel getPlayList() {
		 return freeTTSPlayer.getPlayList();
	 }
	 
	 public ListModel getVoiceList() {
		 /*for(int i = 0; i < sapiPlayer.getTotalVoices(); i++){
			 PlayerVoice pVoice = new PlayerVoice(sapiPlayer.getVoiceName(i), 2);
			 voiceList.addElement(pVoice);
		 }
		 ListModel freeTTSVoice = freeTTSPlayer.getVoiceList();
		 for(int i = 0; i < freeTTSVoice.getSize(); i++){
			 MyVoice voiceObject = (MyVoice) freeTTSVoice.getElementAt(i);
			 PlayerVoice pVoice = new PlayerVoice(voiceObject.getName(), 1);
			 voiceList.addElement(pVoice);
		 }*/
		 return voiceList;
	 }
	 
	 public ListModel getSynthesizerList() {
		 return freeTTSPlayer.getSynthesizerList();
	 }

	 public Object getSpeechObjectAt(int index) {
		 return freeTTSPlayer.getSpeechObjectAt(index);
	 }
	 
	 public void addSpeech(CSpeechObject speech) {
		 freeTTSPlayer.addSpeech(speech);
	 }
	 
	 public void removeSpeechObjectAt(int index) {
		 freeTTSPlayer.removeSpeechObjectAt(index);
	 }
	 
}
/*
class PlayerVoice {

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
}*/