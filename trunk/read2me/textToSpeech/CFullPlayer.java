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
	
	public void createSynthesizers() {
		freeTTSPlayer.createSynthesizers();
		sapiPlayer.init();
		sapiPlayer.start();
	}
	
	public void setListener(SpeakableListener tgListener){
    	freeTTSPlayer.setListener(tgListener);
    }
	
	public void setSAPIListener(CSapiListener tsListener){
		sapiPlayer.addListener(tsListener);
	}
	
	public void play(CSpeechObject speech) {
		if(mode == 1)
			freeTTSPlayer.play(speech);
		else {
			sapiPlayer.play2(speech.getText());
			//sapiPlayer.start();
		}
	}
	
	public void play(int index) {
		freeTTSPlayer.play(index);
	}
	
	public synchronized boolean isPaused() {
		if(mode == 1)
			return freeTTSPlayer.isPaused();
		else
			return sapiPlayer.isPaused();
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
		if(mode == 1)
			freeTTSPlayer.stop();
		else
			sapiPlayer.stopSP();
	}
	
	public void cancel() {
		if(mode == 1)
			freeTTSPlayer.cancel();
		else
			sapiPlayer.cancel();
	}
	
	public void close() {
		if(mode == 1)
			freeTTSPlayer.close();
	}
	
	public void setSynthesizer(int index) {
		freeTTSPlayer.setSynthesizer(index);
		int totalVoices = sapiPlayer.getTotalVoices();
		for(int i = 0; i <  totalVoices; i++){
			if(!sapiPlayer.getVoiceName(i).equals("Sample TTS Voice")){
				PlayerVoice pVoice = new PlayerVoice(sapiPlayer.getVoiceName(i), 2, i);
				voiceList.addElement(pVoice);
			} else {
				i--;
				totalVoices--;
			}
		 }
		ListModel freeTTSVoice = freeTTSPlayer.getVoiceList();
		for(int i = 0; i < freeTTSVoice.getSize(); i++){
			 MyVoice voiceObject = (MyVoice) freeTTSVoice.getElementAt(i);
			 PlayerVoice pVoice = new PlayerVoice(voiceObject.getName(), 1, i+sapiPlayer.getTotalVoices());
			 voiceList.addElement(pVoice);
		}
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
		 if(mode == 1)
			 freeTTSPlayer.setVoiceList(modeDesc);
	 }
	 
	 public ListModel getPlayList() {
		 if(mode == 1)
			 return freeTTSPlayer.getPlayList();
		 else return null;
	 }
	 
	 public ListModel getVoiceList() {
		 return voiceList;
	 }
	 
	 public ListModel getSynthesizerList() {
		 if(mode == 1)
			 return freeTTSPlayer.getSynthesizerList();
		 else return null;
	 }

	 public Object getSpeechObjectAt(int index) {
		 if(mode == 1)
			 return freeTTSPlayer.getSpeechObjectAt(index);
		 else return null;
	 }
	 
	 public void addSpeech(CSpeechObject speech) {
		 if(mode == 1)
			 freeTTSPlayer.addSpeech(speech);
	 }
	 
	 public void removeSpeechObjectAt(int index) {
		 if(mode == 1)
			 freeTTSPlayer.removeSpeechObjectAt(index);
	 }
	 
}
/*
/*class PlayerVoice {

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