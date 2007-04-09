package textToSpeech;

import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.NullAudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import com.sun.speech.freetts.audio.RawFileAudioPlayer;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.InputMode;
import com.sun.speech.freetts.VoiceManager;

import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

/**
 * Creates a thread that synthesizes text passed in as a parameter and creates
 * a WAV file from the synthesized text.
 */
public class CExportToWAV extends Thread{

    private Voice voice;
    private static AudioPlayer audioPlayer = null;
    private String audioFile = null;
    private InputMode inputMode = InputMode.TEXT;
    private String text;
    private String target;
            
    /**
     * Constructs a default object with the kevin16 voice.
     */
    public CExportToWAV(String text, String target) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("kevin16");
        
        this.text = text;
        this.target = target;
    }

    /**
     * Creates an object with the given Voice.
     *
     * @param voice the voice to use
     */
    public CExportToWAV(String text, String target, Voice voice) {
    	this.voice = voice;
    	
    	this.text = text;
        this.target = target;
    }

    /**
     * Starts a Synthesizer by loading the void and creating
     * a new AudioPlayer.
     */
    public void startup() {
	voice.allocate();
    if (audioFile != null) {
	AudioFileFormat.Type type = getAudioType(audioFile);
		if (type != null) { 
		    audioPlayer = new
			SingleFileAudioPlayer(getBasename(audioFile), type);
		} else {
		    try {
			audioPlayer = new RawFileAudioPlayer(audioFile);
		    } catch (IOException ioe) {
			System.out.println("Can't open " + audioFile +
				" " + ioe);
		    }
		}
    }

	if (audioPlayer == null) {
	    audioPlayer = new NullAudioPlayer();
	} 
        
	voice.setAudioPlayer(audioPlayer);
    }


    /**
     * Returns the audio type based upon the extension of the given
     * file
     *
     * @param file the file of interest
     * 
     * @return the audio type of the file or null if it is a
     *     non-supported type
     */
    private AudioFileFormat.Type getAudioType(String file) {
	AudioFileFormat.Type[] types =
	    AudioSystem.getAudioFileTypes();
       String extension = getExtension(file);

	for (int i = 0; i < types.length; i++) {
	    if (types[i].getExtension().equals(extension)) {
		return types[i];
	    }
	}
	return null;
    }


    /**
     * Given a filename returns the extension for the file
     *
     * @param path the path to extract the extension from
     * 
     * @return the extension or <code>null</code> if none
     */
    private static String getExtension(String path) {
	int index = path.lastIndexOf(".");
	if (index == -1) {
	    return null;
	} else {
	    return path.substring(index + 1);
	}
    }

    /**
     * Given a filename returns the basename for the file
     *
     * @param path the path to extract the basename from
     * 
     * @return the basename of the file
     */
    private static String getBasename(String path) {
	int index = path.lastIndexOf(".");
	if (index == -1) {
	    return path;
	} else {
	    return path.substring(0, index);
	}
    }

    /**
     * Shuts down synthesizer by closing the AudioPlayer
     * and voice.
     */
    public void shutdown() {
	audioPlayer.close();
	voice.deallocate();
    }

    
    /**
     * Converts the given text to speech based using processing 
     * options currently set.
     *
     * @param text the text to speak
     *
     * @return true if the utterance was played properly
     */
    public boolean textToSpeech(String text) {
	return voice.speak(text);
    }


    /**
     * Converts the given text to speech based using processing
     * options currently set.
     *
     * @param text the text to speak
     *
     * @return true if the utterance was played properly
     */
    private boolean batchTextToSpeech(String text) {
	boolean ok;
	voice.startBatch();
	ok = textToSpeech(text);
	voice.endBatch();
	return ok;
    }


    /**
     * Returns the voice being used.
     *
     * @return the voice being used
     */
    protected Voice getVoice() {
	return voice;
    }





    /**
     * Sets the input mode.
     *
     * @param inputMode the input mode
     */
    public void setInputMode(InputMode inputMode) {
	this.inputMode = inputMode;
    }

    
    /**
     * Returns the InputMode.
     *
     * @return the input mode
     *
     * @see #setInputMode
     */
    public InputMode getInputMode() {
	return this.inputMode;
    }

    /**
     * Sets the audio file .
     *
     * @param audioFile the audioFile 
     */
    public void setAudioFile(String audioFile) {
	this.audioFile = audioFile;
    }

    /**
     * Calls all functions necessary to export the synthesized text as a WAV file 
     */
    public void exportToWAV() throws java.io.IOException, java.lang.InterruptedException{

    	setAudioFile(target);
    	startup();
    	setInputMode(InputMode.TEXT);
    	//text = "this is a test to see if this works";
    	batchTextToSpeech(text);
    	shutdown();
    	
    }
    
    /**
     * Starts thread
     */
    public void run(){
		try
		{
			exportToWAV();
		}
		catch(java.io.IOException e)
		{
			System.out.println("IOException caught: " + e);
		}
		catch(java.lang.InterruptedException e)
		{
			System.out.println("InterruptedException caught: " + e);
		}
	}

}