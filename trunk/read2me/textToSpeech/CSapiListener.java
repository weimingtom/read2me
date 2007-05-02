package textToSpeech;

/**
 * 
 * @author Stefan Estrada
 *
 * Listener used by CSapiControl to catch events
 */
public interface CSapiListener {
	
	public void started();
	
	public void paused();
	
	public void resumed();
	
	public void cancelled();
	
	public void finished();

}