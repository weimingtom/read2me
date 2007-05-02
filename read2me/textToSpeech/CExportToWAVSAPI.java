package textToSpeech;

/**
 * @author Stefan Estrada
 * 
 * Uses CSapiControl Class to export text as WAV speech.
 *
 */
public class CExportToWAVSAPI extends Thread{
	
	private CSapiControl control;
	private PlayerVoice voice;
	private String text;
	private String target;
	
	/**
	 * Creates CSapiControl instance for exporting WAV file.
	 * 
	 * @param text String of text to be converted to a WAV file.
	 * @param target Location where the WAV file will be exported to
	 * @param voice Voice to be used for conversion
	 */
	public CExportToWAVSAPI(String text, String target, PlayerVoice voice){
		control = new CSapiControl();
		this.voice = voice;
		this.text = text;
		this.target = target;
	}
	
	/**
	 * Export WAV file without creating a new thread.
	 *
	 */
	public void exportToWAV(){
		control.setVoice(voice.getIndex());
		control.exportToWAV(text, target);
	}
	
	/**
	 * Export WAV file by creating a new thread.
	 */
	public void run(){
		control.setVoice(voice.getIndex());
		control.exportToWAV(text, target);
	}

}
