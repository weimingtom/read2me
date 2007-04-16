package textToSpeech;

public class CFullExportToWAV {

	private CExportToWAVSAPI wavSapi;
	private CExportToWAV wavFreeTTS;
	private PlayerVoice voice;
	
	CFullExportToWAV(String text, String target, PlayerVoice voice){
		this.voice = voice;
		if(voice.getMode() == 1)
			wavFreeTTS = new CExportToWAV(text, target, voice);
		else if(voice.getMode() == 2)
			wavSapi = new CExportToWAVSAPI(text, target, voice);
		else
			System.out.println("ERROR Exporting WAV: Incorrect Mode");
	}
	
	public void exportToWAV(){
		System.out.println("Mode: " + voice.getMode());
		if(voice.getMode() == 1) {
			wavFreeTTS.start();
			System.out.println("Exporting FreeTTS WAV File");
		}
		else if(voice.getMode() == 2){
			wavSapi.start();
		}
		else
			System.out.println("ERROR Exporting WAV: Incorrect Mode");
	}
	
	public void start(){
		exportToWAV();
	}
}
