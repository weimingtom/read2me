package textToSpeech;

/**
 * 
 * @author Stefan Estrada
 * 
 * Combines the WAV exporting functionality of both FreeTTS and MS Sapi into
 * one class that determines which one to use based on the voice currently
 * being used by Read2Me.
 *
 */
public class CFullExportToWAV {

	private CExportToWAVSAPI wavSapi;
	private CExportToWAV wavFreeTTS;
	private PlayerVoice voice;
	
	/**
	 * Creates CSapiControl or CExportToWAV (FreeTTS) instance for exporting WAV file.
	 * 
	 * @param text String of text to be converted to a WAV file.
	 * @param target Location where the WAV file will be exported to
	 * @param voice Voice to be used for conversion
	 */
	CFullExportToWAV(String text, String target, PlayerVoice voice){
		this.voice = voice;
		if(voice.getMode() == 1)
			wavFreeTTS = new CExportToWAV(text, target, voice);
		else if(voice.getMode() == 2)
			wavSapi = new CExportToWAVSAPI(text, target, voice);
		else
			System.out.println("ERROR Exporting WAV: Incorrect Mode");
	}
	
	/**
	 * Export WAV file without creating a new thread.
	 *
	 */
	public void exportToWAV(){
		System.out.println("Mode: " + voice.getMode());
		if(voice.getMode() == 1) {
			try{
				wavFreeTTS.exportToWAV();
			}
			catch(java.io.IOException e)
			{
				System.out.println("IOException caught: " + e);
			}
			catch(java.lang.InterruptedException e)
			{
				System.out.println("InterruptedException caught: " + e);
			}
			System.out.println("Exporting FreeTTS WAV File");
		}
		else if(voice.getMode() == 2){
			wavSapi.exportToWAV();
		}
		else
			System.out.println("ERROR Exporting WAV: Incorrect Mode");
	}
	
	/**
	 * Export WAV file by creating a new thread.
	 */
	public void start(){
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
	
	public void shutdown(){
		wavFreeTTS.shutdown();
	}
}
