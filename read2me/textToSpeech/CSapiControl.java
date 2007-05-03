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

import javax.swing.DefaultComboBoxModel;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 
 * 
 * 
 * Controls MS SAPI through the Microsoft COM interface using the Jacob libraries.
 * Implements Play, Stop, Cancel, Pause, Change and get voices, and wav export.
 * The commands that control MS SAPI are implemented as too match the functions of JSAPI as 
 * much as possible.  MS SAPI does not actually have a Stop or Pause function, so instead the 
 * Skip function is used to simulate that function.  When Stop or Cancel is called, the currently
 * playing text is skipped by one sentence.  Since Read2Me sends a sentence at a time to the TTS
 * interface, this works the same as stopping or cancelling.  The difference b/w Stop and Cancel is that
 * Cancel sends an event telling Read2Me that it has finished the current sentence.  This is used
 * for navigation purposes.
 *
 */
public class CSapiControl extends Thread{
	
	private Dispatch msTTS;
	private Dispatch msFS;
	private int voiceIndex;
	private int volume;
	private int speed;
	private boolean isPaused;
	private String toSpeak;
	private CSapiListener sListener;
	private boolean speaking;
	private boolean cancelled;
	private boolean terminated ;
	private boolean threadSuspended;
	
	private DefaultComboBoxModel voiceList;
	
	/**
     * Constructs a default CSapiControl.
     */
	public CSapiControl(){
		voiceList = new DefaultComboBoxModel();
		msTTS = new Dispatch("SAPI.SpVoice");
		msFS = new Dispatch("SAPI.SpFileStream");
		voiceIndex = 0;
		volume = 100;
		speed = 0;
		isPaused = false;
		toSpeak = "";
		speaking = false;
		terminated = false;
		threadSuspended = true;
		cancelled = false;
	}
	
	/**
	 * Adds CSapiListener
	 * 
	 * @param listener Listener that catches CSapiControl Events
	 */
	public void addListener(CSapiListener listener){
		sListener = listener;
	}
	
	/**
	 * Returns name of voice on indexed list
	 * 
	 * @param index Position of voice in list (Determined at load time and
	 * 				depends on the number of MS SAPI voices the user has
	 * 				installed on the computer)
	 * @return Name of voice as String
	 */
	public String getVoiceName(int index){
		if(index > voiceList.getSize() || index < 0){
			System.out.println("ERROR: Invalid MS SAPI Voice Index");
			return "Invalid Voice";
		}
		else {
			MySapiVoice voice = (MySapiVoice) voiceList.getElementAt(index);
			return voice.getName();
		}
	}
	
	/**
	 * Returns total number of voices installed on the computer
	 * 
	 * @return Number of Voices as integer
	 */
	public int getTotalVoices(){
		return voiceList.getSize();
	}
	
	/**
	 * Sets the voice MS Sapi is to used.  If an out or range index is passed,
	 * and error is printed and the voice is not changed.
	 * 
	 * @param index Position of voice in the Voice List
	 */
	public void setVoice(int index){
		if(index > voiceList.getSize() || index < 0)
			System.out.println("ERROR: Invalid MS SAPI Voice Index, Voice not changed");
		else
			voiceIndex = index;
	}
	
	/**
	 * Returns the index of the voice MS Sapi is currently using.  Use <code>getVoiceName</code>
	 * to get the name of the voice as a String using the integer returned from this function
	 * 
	 * @return voiceIndex
	 */
	public int getCurrentVoice(){
		return voiceIndex;
	}
	
	/**
	 * Sets the volume being used by MS Sapi.  Uses a general range from 1 to 10
	 * used by Read2Me that is then converted to the range used by MS Sapi
	 * 
	 * @param vol Volume as float ranging from 0 to 10
	 * 
	 * @return Returns false if the volume value passed is out of range
	 */
	public boolean setVolume(float vol){
		if(vol < 0 ) {
			System.out.println("ERROR: Invalid MS SAPI Volume, Volume not changed");
			return false;
		}
		else if(vol > 10) {
			return false;
		}
		else {
			volume = Math.round(10*vol);
			return true;
		}
	}
	
	/**
	 * Returns current volume being used.
	 * 
	 * @return Current Volume as Float
	 */
	public float getVolume(){
		return volume/10;
	}
	
	/**
	 * Sets the speaking speed being used by MS Sapi.  Uses a general range from 10 to 400
	 * used by Read2Me that is then converted to the range used by MS Sapi
	 * 
	 * @param s Speaking speed as float ranging from 10 to 400
	 * 
	 * @return Returns false if the speaking speed value passed is out of range
	 */
	public boolean setSpeed(float s){
		if(s < 10) {
			return false;
		}
		else if(s > 400) {
			return false;
		}
		else {
			speed = Math.round(s/20-10);
			return true;
		}
	}
	
	/**
	 * Returns current speaking speed being used.
	 * 
	 * @return Current Speaking Speed as Float
	 */
	public float getSpeed(){
		return (speed*20+10);
	}
	
	/**
	 * Begins  speaking loops that terminates when MS Sapi finishes speaking.  Uses a polling method
	 * using the "Status" of the MS Sapi object to determine if it has finished.  Would be better to use
	 * events, but we were not able to get the COM events of MS Sapi to work with Java.
	 * 
	 * @param speech String of text to be spoken by MS Sapi
	 */
	public void play(String speech){
		String msSpeech = "<voice required=\"Name="+ getVoiceName(voiceIndex) + "\">" + "<volume level = \""+ volume
							+ "\"><rate absspeed =\""+ speed +"\">" + speech + "</rate></volume></voice>"; 
		Dispatch.call(msTTS, "Speak", msSpeech, new Variant(1));
		String done = "not set";
		sListener.started();
		while(!done.equals("1")) {
			try {
			if(isPaused){
				sleep(200);
			}
			else {
				sleep(200);
				Dispatch ISpeechVoiceStatus = Dispatch.get(msTTS, "Status").toDispatch();
				Variant SpeechRunState = Dispatch.get(ISpeechVoiceStatus, "RunningState");
				done = SpeechRunState.toString();
			}
			}catch (Exception e) {
				System.out.println("thread err");
				e.printStackTrace();
			}
		}
		//System.out.println("Exited Play Loop");
	}
	
	/**
	 * Begins thread if suspended and tells CSapiControl that it needs to enter the play loop.
	 * 
	 * @param speech String of text to be spoken by MS Sapi
	 */
	public synchronized void play2(String speech){
		System.out.println("Playing SAPI");
		toSpeak = speech;
		speaking = true;
		threadSuspended = false;
		notify();
	}
	
	/**
	 * Controls thread used by CSapiControl.  If text is not currently being spoken, then
	 * it enters a suspended state and is awoken again once new text to be spoken is passed.
	 */
	public void run(){
		while(!terminated){
			if(!speaking){
				//System.out.println("Going to Wait");
				try {
	                sleep(200);
	                synchronized(this) {
	                    while (threadSuspended)
	                        wait();
	                }
	            } catch (InterruptedException e){}
	         } else {
	        	 play(toSpeak);
	        	 if(speaking && !cancelled) {
	        		 sListener.finished();
	        	 }
	        	 cancelled = false;
	         }
		}
	}
	
	/**
	 * Paused text being spoken by MS Sapi
	 *
	 */
	public void pause(){
		isPaused = true;
		sListener.paused();
		Dispatch.call(msTTS, "Pause");
	}
	
	/**
	 * Resumes text that was being spoken by MS Sapi but was paused
	 *
	 */
	public void resumeSP(){
		isPaused = false;
		sListener.resumed();
		Dispatch.call(msTTS, "Resume");
	}
	
	/**
	 * Returns whether or not MS Sapi is currently paused.
	 *  
	 * @return pause state as boolean, false if it is not paused
	 */
	public boolean isPaused(){
		return isPaused;
	}
	
	/**
	 * Cancels currently being spoken text by calling the MS Sapi "Skip" command and
	 * thus ending the sentence.
	 *
	 */
	public synchronized void cancel(){
		isPaused = false;
		cancelled = true;
		Dispatch.call(msTTS, "Skip", "Sentence", new Variant(1));
		sListener.cancelled();
	}
	
	/**
	 * Stops currently being spoken text by calling the MS Sapi "Skip" command and
	 * thus ending the sentence.
	 *
	 */
	public synchronized void stopSP(){
		isPaused = false;
		speaking = false;
		threadSuspended = true;
		Dispatch.call(msTTS, "Skip", "Sentence", new Variant(1));
		sListener.cancelled();
	}
	
	/**
	 * Exports passed text to a WAV to the passed location
	 * 
	 * @param speech String of text to be converted to a WAV file
	 * @param target Location to write the WAV file
	 */
	public void exportToWAV(String speech, String target){
		Dispatch.call(msFS, "Open", target, new Variant(3), new Variant(true));
		Dispatch.putRef(msTTS, "AudioOutputStream", msFS);
		Dispatch.call(msTTS, "Speak", speech);
		Dispatch.call(msFS,"Close");
	}
	
	/**
	 * Intializes MS Sapi Control and retreives available voices.
	 *
	 */
	public void init(){
		try {
	    	
	    	Dispatch voices = Dispatch.call(msTTS, "GetVoices").toDispatch();
	    	
	    	Variant itemCount = Dispatch.get(voices, "Count");
	    	for(int i=0; i < itemCount.getInt(); i++) {
	    		Dispatch voice = Dispatch.call(voices, "Item", i).toDispatch();
	    		Variant voiceName = Dispatch.call(voice, "GetDescription");
	    		voiceList.addElement(new MySapiVoice(voiceName.toString()));
	    	}
	    	
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	/**
	 * Terminates Thread loop
	 *
	 */
	public void shutdown(){
		terminated = true;
	}

}

/**
 * 
 * 
 *
 * Object that stores name of MS Sapi voice.
 */
class MySapiVoice {

	private String name;
	
    public MySapiVoice(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
}
