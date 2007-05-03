/*
This file is part of Read2Me!

Read2Me! is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Read2Me! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Read2Me!. If not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package textToSpeech;

import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;

/**
 * 
 * 
 *
 *Integrates FreeTTS and MS Sapi classes into one class using a unified interface.
 *Which one is used by Read2Me is determined by the currently set voice in Read2Me.
 *Some of the functions are only used by FreeTTS and not MS Sapi.
 */
public class CFullPlayer implements CPlayerInterface {
	
	private CPlayer freeTTSPlayer;
	private CSapiControl sapiPlayer;
	private int mode;
	//mode = 1 : freeTTS
	//mode = 2 : SAPI
	private DefaultComboBoxModel voiceList;
	
	/**
     * Constructs a default CPlayer.
     */
	public CFullPlayer(){
		
		freeTTSPlayer = new CPlayer();
		sapiPlayer = new CSapiControl();
		mode = 1;
		voiceList = new DefaultComboBoxModel();
		
	}
	
	/**
     * Creates the list of synthesizers for FreeTTS.
     * Initializes MS Sapi
     */
	public void createSynthesizers() {
		freeTTSPlayer.createSynthesizers();
		sapiPlayer.init();
		sapiPlayer.start();
	}
	
	/**
     * Sets speakable listener (Only used for FreeTTS)
     */
	public void setListener(SpeakableListener tgListener){
    	freeTTSPlayer.setListener(tgListener);
    }
	
	/**
	 * Sets SAPI Listener (Only used for MS Sapi)
	 */
	public void setSAPIListener(CSapiListener tsListener){
		sapiPlayer.addListener(tsListener);
	}
	
	/**
     * Performs text-to-speech on the given SpeechObject.
     *
     * @param speech the SpeechObject to perform text-to-speech
     */
	public void play(CSpeechObject speech) {
		if(mode == 1)
			freeTTSPlayer.play(speech);
		else {
			sapiPlayer.play2(speech.getText());
			//sapiPlayer.start();
		}
	}
	
	/**
     * Performs text-to-speech on the object at the given index of
     * the play list. (Only used for FreeTTS)
     *
     * @param index the index of the SpeechObject on the play list
     */
	public void play(int index) {
		freeTTSPlayer.play(index);
	}
	
	/**
     * Returns true if the player is paused.
     *
     * @return <code>true</code> if the player is paused,
     *         <code>false</code> otherwise
     */
	public synchronized boolean isPaused() {
		if(mode == 1)
			return freeTTSPlayer.isPaused();
		else
			return sapiPlayer.isPaused();
	}
	
	/**
     * Pauses the player.
     */
	public synchronized void pause() {
		if(mode == 1)
			freeTTSPlayer.pause();
		else
			sapiPlayer.pause();
	}
	
	/**
     * Resumes the player.
     */
	public synchronized void resume() {
		if(mode == 1)
			freeTTSPlayer.resume();
		else
			sapiPlayer.resumeSP();
	}
	
	/**
     * Stops the player if it is playing.
     */
	public synchronized void stop() {
		if(mode == 1)
			freeTTSPlayer.stop();
		else
			sapiPlayer.stopSP();
	}
	
	/**
     * Cancels the currently playing item.
     */
	public void cancel() {
		if(mode == 1)
			freeTTSPlayer.cancel();
		else
			sapiPlayer.cancel();
	}
	
	/**
     * Closes the CPlayer (Only used for FreeTTS)
     */
	public void close() {
		if(mode == 1)
			freeTTSPlayer.close();
	}
	
	/**
     * Sets the Synthesizer at the given index to use
     * Creates global voice list including voice from both FreeTTS and MS Sapi
     *
     * @param index index of the synthesizer in the list
     */
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
	
	/**
     * Sets the Voice at the given index to use.
     *
     * @param index the index of the voice in the list
     */
	public void setVoice(int index) {
		PlayerVoice pVoice = (PlayerVoice) voiceList.getElementAt(index);
		mode = pVoice.getMode();
		if(mode == 1)
			freeTTSPlayer.setVoice(index-sapiPlayer.getTotalVoices());
		else
			sapiPlayer.setVoice(index);
	}
	
	/**
     * Returns the volume.
     *
     * @return the volume, or -1 if unknown, or an error occurred (FreeTTS only)
     */
	public float getVolume() {
		if(mode == 1)
			return freeTTSPlayer.getVolume();
		else
			return sapiPlayer.getVolume();
	}
	
	/**
     * Sets the volume.
     *
     * @param volume set the volume of the synthesizer
     *
     * @return true if new volume is set; false otherwise
     */
	 public boolean setVolume(float volume) {
		 if(mode == 1)
			 return freeTTSPlayer.setVolume(volume);
		 else
			 return sapiPlayer.setVolume(volume);
	 }
	 
	 /**
	     * Returns the speaking speed.
	     *
	     * @return the speaking speed, or -1 if unknown or an error occurred (FreeTTS only)
	     */
	 public float getSpeakingSpeed() {
		 if(mode == 1)
			 return freeTTSPlayer.getSpeakingSpeed();
		 else
			 return sapiPlayer.getSpeed();
	 }
	 
	 /**
	     * Sets the speaking speed in the number of words per minute.
	     *
	     * @param wordsPerMin the speaking speed
	     *
	     * @return true if new speaking speed is set; false otherwise
	     */
	 public boolean setSpeakingSpeed(float wordsPerMin) {
		 if(mode == 1)
			 return freeTTSPlayer.setSpeakingSpeed(wordsPerMin);
		 else
			 return sapiPlayer.setSpeed(wordsPerMin);
	 }
	 
	 /**
	     * Sets the list of voices using the given Synthesizer mode description.
	     *
	     * @param modeDesc the synthesizer mode description
	     */
	 public void setVoiceList(SynthesizerModeDesc modeDesc) {
		 if(mode == 1)
			 freeTTSPlayer.setVoiceList(modeDesc);
	 }
	 
	 /**
	     * Returns the play list.(Only used by FreeTTS)
	     *
	     * @return the play list
	     */
	 public ListModel getPlayList() {
		 if(mode == 1)
			 return freeTTSPlayer.getPlayList();
		 else return null;
	 }
	 
	 /**
	     * Returns the list of voices of the current synthesizer
	     *
	     * @return the list of voices
	     */
	 public ListModel getVoiceList() {
		 return voiceList;
	 }
	 
	 /**
	     * Returns the list synthesizers.
	     *
	     * @return the synthesizer list
	     */
	 public ListModel getSynthesizerList() {
		 if(mode == 1)
			 return freeTTSPlayer.getSynthesizerList();
		 else return null;
	 }

	 /**
	     * Returns the SpeechObject at the given index of the play list. (Used only by FreeTTS)
	     *
	     * @param index the index of the SpeechObject on the play list
	     *
	     * @return the CSpeechObject
	     */
	 public Object getSpeechObjectAt(int index) {
		 if(mode == 1)
			 return freeTTSPlayer.getSpeechObjectAt(index);
		 else return null;
	 }
	 
	 /**
	     * Adds the given SpeechObject to the end of the play list. (Used only by FreeTTS)
	     *
	     * @param speech the SpeechObject to add
	     */
	 public void addSpeech(CSpeechObject speech) {
		 if(mode == 1)
			 freeTTSPlayer.addSpeech(speech);
	 }
	 
	 /**
	     * Removes the SpeechObject at the given position from the list (Used only by FreeTTS)
	     *
	     * @param index the index of the SpeechObject to remove
	     */
	 public void removeSpeechObjectAt(int index) {
		 if(mode == 1)
			 freeTTSPlayer.removeSpeechObjectAt(index);
	 }
	 
}
