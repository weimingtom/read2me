package textToSpeech;

public class CExportToWAVSAPI extends Thread{
	
	private CSapiControl control;
	private PlayerVoice voice;
	private String text;
	private String target;
	
	public CExportToWAVSAPI(String text, String target, PlayerVoice voice){
		control = new CSapiControl();
		this.voice = voice;
		this.text = text;
		this.target = target;
	}
	
	public void run(){
		control.setVoice(voice.getIndex());
		control.exportToWAV(text, target);
	}

}
