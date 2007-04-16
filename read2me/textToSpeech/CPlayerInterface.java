package textToSpeech;
import javax.swing.ListModel;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.SynthesizerModeDesc;

/**
 * Defines the data model used for playing text using the FreeTTS synthesizer.
 * Defines methods for controlling volume and speaking speed.
 * Also gives you information such as the list of synthesizers available, 
 * list of voices, etc... that a user interface can use to manipulate
 * the speech output.
 */
public interface CPlayerInterface {

    /**
     * Performs text-to-speech on the given SpeechObject.
     *
     * @param CSpeechObject the SpeechObject to perform text-to-speech
     */
    public void play(CSpeechObject CSpeechObject);


    /**
     * Performs text-to-speech on the object at the given index of
     * the play list.
     *
     * @param index the index of the SpeechObject on the play list
     */
    public void play(int index);


    /**
     * Returns true if the player is paused.
     *
     * @return <code>true</code> if the player is paused,
     *         <code>false</code> otherwise
     */
    public boolean isPaused();

    
    /**
     * Pauses the player.
     */
    public void pause();
    

    /**
     * Resumes the player.
     */
    public void resume();
        

    /**
     * Stops the player if it is playing.
     */
    public void stop();


    /**
     * Cancels the currently playing item.
     */
    public void cancel();

    /**
     * Creates the list of synthesizers.
     */
    public void createSynthesizers();

    /**
     * Sets speakable listener
     */
    public void setListener(SpeakableListener tgListener);
    
    public void setSAPIListener(CSapiListener tsListener);

    /**
     * Sets the Synthesizer at the given index to use
     *
     * @param index index of the synthesizer in the list
     */
    public void setSynthesizer(int index);

    
    /**
     * Sets the Voice at the given index to use.
     *
     * @param index the index of the voice in the list
     */
    public void setVoice(int index);

        
    /**
     * Sets the list of voices using the given Synthesizer mode description.
     *
     * @param modeDesc the synthesizer mode description
     */
    public void setVoiceList(SynthesizerModeDesc modeDesc);


    /**
     * Returns the volume.
     *
     * @return the volume, or -1 if unknown, or an error occurred
     */
    public float getVolume();

    
    /**
     * Sets the volume.
     *
     * @param volume set the volume of the synthesizer
     *
     * @return true if new volume is set; false otherwise
     */
    public boolean setVolume(float volume);


    /**
     * Returns the speaking speed.
     *
     * @return the speaking speed, or -1 if unknown or an error occurred
     */
    public float getSpeakingSpeed();

    
    /**
     * Sets the speaking speed in the number of words per minute.
     *
     * @param wordsPerMin the speaking speed
     *
     * @return true if new speaking speed is set; false otherwise
     */
    public boolean setSpeakingSpeed(float wordsPerMin);

         
    /**
     * Returns the play list.
     *
     * @return the play list
     */
    public ListModel getPlayList();


    /**
     * Returns the list of voices of the current synthesizer
     *
     * @return the list of voices
     */
    public ListModel getVoiceList();

    
    /**
     * Returns the list synthesizers.
     *
     * @return the synthesizer list
     */
    public ListModel getSynthesizerList();
    

    /**
     * Returns the SpeechObject at the given index of the play list.
     *
     * @param index the index of the SpeechObject on the play list
     *
     * @return the CSpeechObject
     */
    public Object getSpeechObjectAt(int index);


    /**
     * Adds the given SpeechObject to the end of the play list.
     *
     * @param CSpeechObject the SpeechObject to add
     */
    public void addSpeech(CSpeechObject CSpeechObject);


    /**
     * Removes the SpeechObject at the given position from the list
     *
     * @param index the index of the SpeechObject to remove
     */
    public void removeSpeechObjectAt(int index);


    /**
     * Closes the CPlayer
     */
    public void close();
}
