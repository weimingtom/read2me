package textToSpeech;

public interface CSapiListener {
	
	public void started();
	
	public void paused();
	
	public void resumed();
	
	public void cancelled();
	
	public void finished();

}