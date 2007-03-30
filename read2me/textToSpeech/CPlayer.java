package textToSpeech;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.Engine;
import javax.speech.EngineException;
import javax.speech.EngineList;
import javax.speech.synthesis.SpeakableAdapter;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.Voice;
import javax.speech.synthesis.SpeakableListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 * Implements the text-to-speech data model of the CPlayer application, using
 * JSAPI. It should work with any JSAPI implementation.
 */
public class CPlayer implements CPlayerInterface {

    private Synthesizer synthesizer;
    private SpeakableListener speakListener;
    private boolean paused = false;
    private boolean stopped = false;
    private boolean playingFile = false;
    private DefaultListModel playList;
    private DefaultComboBoxModel synthesizerList;
    private DefaultComboBoxModel voiceList;
    private float volume = -1;
    private static boolean debug = false;
    private Set loadedSynthesizers;

    
    /**
     * Constructs a default CPlayer.
     */
    public CPlayer() {
	playList = new DefaultListModel();
	synthesizerList = new DefaultComboBoxModel();
	voiceList = new DefaultComboBoxModel();
	loadedSynthesizers = new HashSet();
    }
    

    /**
     * Creates a FreeTTS synthesizer.
     */
    public void createSynthesizers() {
	try {
	    EngineList list = Central.availableSynthesizers(null); 
	    Enumeration e = list.elements();

	    while (e.hasMoreElements()) {
		MySynthesizerModeDesc myModeDesc =
		    new MySynthesizerModeDesc
		    ((SynthesizerModeDesc) e.nextElement(), this);
		debugPrint(myModeDesc.getEngineName() + " " +
			   myModeDesc.getLocale() + " " +
			   myModeDesc.getModeName() + " " +
			   myModeDesc.getRunning());
		synthesizerList.addElement(myModeDesc);
	    }
	    
	    if (synthesizerList.getSize() > 0) {
		setSynthesizer(0);
	    } else {
		System.err.println(noSynthesizerMessage());
	    }
            if (synthesizer == null) {
                System.err.println("CPlayer: Can't find synthesizer");
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //speakListener = new CR2MListener();
    }
    
    public void setListener(SpeakableListener tgListener){
    	speakListener = tgListener;
    }

    /**
     * Returns a "no synthesizer" message, and asks 
     * the user to check if the "speech.properties" file is
     * at user.home or java.home/lib.
     *
     * @return a no synthesizer message
     */
    static private String noSynthesizerMessage() {
        String message =
            "No synthesizer created.  This may be the result of any\n" +
            "number of problems.  It's typically due to a missing\n" +
            "\"speech.properties\" file that should be at either of\n" +
            "these locations: \n\n";
        message += "user.home    : " + System.getProperty("user.home") + "\n";
        message += "java.home/lib: " + System.getProperty("java.home") +
	    File.separator + "lib\n\n" +
            "Another cause of this problem might be corrupt or missing\n" +
            "voice jar files in the freetts lib directory.  This problem\n" +
            "also sometimes arises when the freetts.jar file is corrupt\n" +
            "or missing.  Sorry about that.  Please check for these\n" +
            "various conditions and then try again.\n";
        return message;
    }
	    
    /**
     * Performs TTS on the given SpeechObject.
     *
     * @param speech the CSpeechObject to play
     */
    public void play(CSpeechObject speech) {
	if (speech != null) {
	    if (speech.getType() == CSpeechType.TEXT) {
		play(speech.getText());
	    } else if (speech.getType() == CSpeechType.JSML) {
		playJSML(speech.getText());
	    } else if (speech.getType() == CSpeechType.TEXT_FILE ||
		       speech.getType() == CSpeechType.JSML_FILE) {
		playFile(speech.getFile(), speech.getType());
	    } 
	}
    }


    /**
     * Performs TTS on the object at the given index of the play list.
     *
     * @param index index of the object in the playlist to play
     */
    public void play(int index) {
	if (0 <= index && index < playList.getSize()) {
	    CSpeechObject speech = (CSpeechObject) playList.getElementAt(index);
	    if (speech != null) {
		play(speech);
	    }
	}
    }


    /**
     * Performs text-to-speech on the given text.
     *
     * @param text the text to perform TTS
     */
    private void play(String text) {
    	if(speakListener == null)
    		speakListener = new CR2MListener();
    	synthesizer.speakPlainText(text, speakListener);
    }
    

    /**
     * Performs text-to-speech on the given JSML text.
     *
     * @param text the text to perform TTS
     */
    private void playJSML(String jsmlText) {
	try {
		if(speakListener == null)
    		speakListener = new CR2MListener();
		synthesizer.speak(jsmlText, speakListener);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }


    /**
     * Plays the text in the given File.
     *
     * @param file the File to play
     * @param type the file type
     */
    private void playFile(File file, CSpeechType type) {
	try {
	    FileInputStream fileStream = new FileInputStream(file);
	    playInputStream(fileStream, type);
	} catch (FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	}
    }
	

    /**
     * Plays the text in the given File.
     *
     * @param file the File to play
     * @param type the file type
     */
    private void playInputStream(InputStream inStream, CSpeechType type) {
	playingFile = true;
	if (inStream != null) {
	    try {
		BufferedReader reader = new BufferedReader
		    (new InputStreamReader(inStream));
		String line = "";
		if (type == CSpeechType.TEXT_FILE) {
		    while (!isStopped() && 
			   (line = reader.readLine()) != null) {
			if (line.length() > 0) {
			    play(line);
			}
		    }
		} else if (type == CSpeechType.JSML_FILE) {
		    String fileText = "";
		    while ((line = reader.readLine()) != null) {
			fileText += line;
		    }
		    if (fileText != null && fileText.length() > 0) {
			playJSML(fileText);
		    }
		}
		stopped = false;
	    } catch (IOException ioe) {
		ioe.printStackTrace();
	    }
	}
	playingFile = false;
    }
	

    /**
     * Returns true if the player is paused.
     *
     * @return true if the player is paused, false otherwise
     */
    public synchronized boolean isPaused() {
	return paused;
    }
    

    /**
     * Pauses the player.
     */
    public synchronized void pause() {
	paused = true;
	synthesizer.pause();
    }
        

    /**
     * Resumes the player.
     */
    public synchronized void resume() {
	paused = false;
	try {
	    synthesizer.resume();
	} catch (AudioException ae) {
	    ae.printStackTrace();
	}	
    }
            

    /**
     * Stops the player if it is playing.
     */
    public synchronized void stop() {
	if (playingFile) {
	    stopped = true;
	}
	synthesizer.cancelAll();
    }


    /**
     * Cancels the currently playing item.
     */
    public void cancel() {
	synthesizer.cancel();
    }

    /**
     * Close this object
     */
    public void close() {
	for (Iterator i = loadedSynthesizers.iterator(); i.hasNext();) {
	    Synthesizer synth = (Synthesizer) i.next();
	    try {
		synth.deallocate();
	    } catch (EngineException ee) {
		System.out.println("Trouble closing the synthesizer: " + ee);
	    }
	}
    }
    
    /**
     * Returns true if the CPlayer is currently being stopped.
     *
     * @return true if the CPlayer is currently being stopped; false otherwise
     */    
    private synchronized boolean isStopped() {
	return stopped;
    }


    /**
     * Sets the Synthesizer at the given index to use
     *
     * @param index index of the synthesizer in the list
     */
    public void setSynthesizer(int index) {
	MySynthesizerModeDesc myModeDesc = (MySynthesizerModeDesc)
	    synthesizerList.getElementAt(index);
	if (myModeDesc != null) {
	    synthesizer = myModeDesc.getSynthesizer();
	    if (synthesizer == null) {
		synthesizer = myModeDesc.createSynthesizer();
		if (synthesizer == null) {
		    debugPrint("still null");
		} else {
		    debugPrint("created");
		}
	    } else {
		debugPrint("not null");
	    }
	    if (myModeDesc.isSynthesizerLoaded()) {
		setVoiceList(myModeDesc);
	    } else {
		myModeDesc.loadSynthesizer();
	    }

	    loadedSynthesizers.add(synthesizer);
            synthesizerList.setSelectedItem(myModeDesc);
	}
    }
    

    /**
     * Sets the Voice at the given to use.
     *
     * @param index the index of the voice
     */
    public void setVoice(int index) {
	try {
	    Voice voice = (Voice) voiceList.getElementAt(index);
	    if (voice != null) {
		float oldVolume = getVolume();
		float oldSpeakingSpeed = getSpeakingSpeed();
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
		synthesizer.getSynthesizerProperties().setVoice(voice);
		setVolume(oldVolume);
		setSpeakingSpeed(oldSpeakingSpeed);
                voiceList.setSelectedItem(voice);
	    }
	} catch (PropertyVetoException pve) {
	    pve.printStackTrace();
	} catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
    }
    

    /**
     * Returns the volume, in the range of 0 to 10.
     *
     * @return the volume, or -1 if unknown, or an error occurred
     */
    public float getVolume() {
	try {
	    float adjustedVolume =
		synthesizer.getSynthesizerProperties().getVolume();
	    if (adjustedVolume < 0.5) {
		volume = 0;
	    } else {
		volume = (float) ((adjustedVolume - 0.5) * 20);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} 
	return volume;
    }
    

    /**
     * Sets the volume, in the range of 0 to 10.
     *
     * @param volume the new volume
     *
     * @return true if new volume is set; false otherwise
     */
    public boolean setVolume(float volume) {
	try {
	    float adjustedVolume = (float) (volume/20 + 0.5);
	    if (synthesizer != null) {
		synthesizer.getSynthesizerProperties().setVolume
		    (adjustedVolume);
		this.volume = volume;
		return true;
	    } else {
		this.volume = volume;
		return false;
	    }
	} catch (PropertyVetoException pve) {
	    try {
		synthesizer.getSynthesizerProperties().setVolume(this.volume);
	    } catch (PropertyVetoException pe) {
		pe.printStackTrace();
	    }
	    return false;
	}
    }
                

    /**
     * Returns the speaking speed.
     *
     * @return the speaking speed, or -1 if unknown or an error occurred
     */
    public float getSpeakingSpeed() {
	if (synthesizer != null) {
	    return synthesizer.getSynthesizerProperties().getSpeakingRate();
	} else {
	    return -1;
	}
    }
	

    /**
     * Sets the speaking speed in terms of words per minute.
     *
     * @param wordsPerMin the new speaking speed
     *
     * @return the speaking speed, or -1 if unknown or an error occurred
     */
    public boolean setSpeakingSpeed(float wordsPerMin) {
	float oldSpeed = getSpeakingSpeed();
	SynthesizerProperties properties =
	    synthesizer.getSynthesizerProperties();
	try {
	    properties.setSpeakingRate(wordsPerMin);
	    return true;
	} catch (PropertyVetoException pve) {
	    try {
		properties.setSpeakingRate(oldSpeed);
	    } catch (PropertyVetoException pe) {
		pe.printStackTrace();
	    }
	    return false;
	}
    }	
         

    /**
     * Sets the list of voices using the given Synthesizer mode description.
     *
     * @param modeDesc the synthesizer mode description
     */
    public void setVoiceList(SynthesizerModeDesc modeDesc) {
	Voice[] voices = modeDesc.getVoices();
	voiceList.removeAllElements();
	for (int i = 0; i < voices.length; i++) {
	    voiceList.addElement(new MyVoice(voices[i].getName(),
					     voices[i].getGender(),
					     voices[i].getAge(),
					     voices[i].getStyle()));
	}
    }


    /**
     * Returns the play list.
     *
     * @return the play list
     */
    public ListModel getPlayList() {
	return playList;
    }


    /**
     * Returns the list of voices.
     *
     * @return the list of voices
     */
    public ListModel getVoiceList() {
	return voiceList;
    }


    /**
     * Returns the list synthesizers
     *
     * @return the synthesizer list
     */
    public ListModel getSynthesizerList() {
	return synthesizerList;
    }

    
    /**
     * Returns the SpeechObject at the given index of the play list.
     *
     * @return CSpeechObject
     */
    public Object getSpeechObjectAt(int index) {
	return null;
    }
    

    /**
     * Adds the given SpeechObject to the end of the play list.
     *
     * @param speech the CSpeechObject object to add
     */
    public void addSpeech(CSpeechObject speech) {
	playList.addElement(speech);
    }


    /**
     * Removes the speech at the given position from the list
     *
     * @param index the index of the CSpeechObject to remove
     */
    public void removeSpeechObjectAt(int index) {
	if (index < playList.getSize()) {
	    playList.removeElementAt(index);
	}
    }


    /**
     * Prints debug statements.
     *
     * @param statement debug statements
     */
    public static void debugPrint(String statement) {
	if (debug) {
	    System.out.println(statement);
	}
    }
}


/**
 * A Voice that implements the toString() method so that
 * it returns the name of the person who owns this Voice.
 */
class MyVoice extends Voice {


    /**
     * Constructor provided with voice name, gender, age and style.
     *
     * @param name the name of the person who owns this Voice
     * @param gender the gender of the person
     * @param age the age of the person
     * @param style the style of the person
     */
    public MyVoice(String name, int gender, int age, String style) {
	super(name, gender, age, style);
    }


    /**
     * Returns the name of the person who owns this Voice.
     *
     * @param String the name of the person
     */
    public String toString() {
	return getName();
    }
}


/**
 * A SynthesizerModeDesc that implements the <code>toString()</code>
 * method so that it returns the name of the synthesizer.
 */
class MySynthesizerModeDesc extends SynthesizerModeDesc {

    private CPlayerInterface playerInterface = null;
    private Synthesizer synthesizer = null;
    private boolean synthesizerLoaded = false;
    

    /**
     * Constructs a MySynthesizerModeDesc with the attributes from
     * the given SynthesizerModeDesc.
     *
     * @param modeDesc the SynthesizerModeDesc to get attributes from
     */
    public MySynthesizerModeDesc(SynthesizerModeDesc modeDesc,
				 CPlayerInterface playerInterface) {
	super(modeDesc.getEngineName(), modeDesc.getModeName(),
	      modeDesc.getLocale(), modeDesc.getRunning(), 
	      modeDesc.getVoices());
	this.playerInterface = playerInterface;
    }
    
    
    /**
     * Returns true if the synthesizer is already loaded.
     *
     * @return true if the synthesizer is already loaded
     */
    public synchronized boolean isSynthesizerLoaded() {
	if (synthesizer == null) {
	    return false;
	}
	return ((synthesizer.getEngineState() & Engine.ALLOCATED) != 0);
    }
    
    
    /**
     * Returns a Synthesizer that fits the description of this
     * MySynthesizerModeDesc. If the synthesize was never loaded,
     * it is loaded in a separate thread.
     *
     * @return a Synthesizer
     */
    public synchronized Synthesizer getSynthesizer() {
	debugPrint("MyModeDesc.getSynthesizer(): " + getEngineName());
	return synthesizer;
    }


    /**
     * Creates the Synthesizer.
     *
     * @return the created Synthesizer
     */
    public Synthesizer createSynthesizer() {
	try {
	    debugPrint("Creating " + getEngineName() + "...");
	    synthesizer = Central.createSynthesizer(this);
	    
	    if (synthesizer == null) {
		System.out.println("Central created null synthesizer");
	    } else {
		synthesizer.allocate();
		synthesizer.resume();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} 
	return synthesizer;
    }

    

    /**
     * Allocates the synthesizer if it has never been allocated. This
     * method should be called after method createSynthesizer().
     * It spawns a new thread to allocate the synthesizer.
     */
    public Synthesizer loadSynthesizer() {
	try {
	    if (!synthesizerLoaded) {
		debugPrint("Loading " + getEngineName() + "...");
		synthesizerLoaded = true;
		SynthesizerLoader loader = new SynthesizerLoader
		    (synthesizer, this);
		loader.start();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return synthesizer;
    }
    
    /**
     * Returns the CPlayerInterface.
     *
     * @return the CPlayerInterface
     */
    public CPlayerInterface getPlayerInterface() {
	return playerInterface;
    }

    
    /**
     * Returns the name of the Synthesizer.
     *
     * @return the name of the Synthesizer
     */
    public String toString() {
	return getEngineName();
    }

    
    /**
     * Prints debug statements.
     *
     * @param statement debug statements
     */
    private void debugPrint(String statement) {
	CPlayer.debugPrint(statement);
    }
}


/**
 * A Thread that loads the Synthesizer.
 */
class SynthesizerLoader extends Thread {
    private Synthesizer synthesizer;
    private MySynthesizerModeDesc modeDesc;
    private CPlayerInterface playerInterface;
   
    
    /**
     * Constructs a SynthesizerLoaded which loads the given Synthesizer.
     *
     * @param synthesizer the Synthesizer to load
     * @param modeDesc the MySynthesizerModeDesc from which we can retrieve
     *    the CPlayerInterface
     */
    public SynthesizerLoader(Synthesizer synthesizer,
			     MySynthesizerModeDesc modeDesc) {
	this.synthesizer = synthesizer;
	this.modeDesc = modeDesc;
	this.playerInterface = modeDesc.getPlayerInterface();
    }
    

    /**
     * Implements the run() method of the Thread class.
     */
    public void run() {
	try {
	    System.out.println("allocating...");
	    synthesizer.allocate();
	    System.out.println("...allocated");
	    synthesizer.resume();
	    System.out.println("...resume");
	    playerInterface.setVoiceList(modeDesc);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
