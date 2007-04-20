package textToSpeech;

import javax.swing.DefaultComboBoxModel;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.InvocationProxy;
import com.jacob.com.JacobObject;
import com.jacob.com.Variant;

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
	private boolean stopped;
	private boolean cancelled;
	private boolean terminated ;
	private boolean threadSuspended;
	
	private DefaultComboBoxModel voiceList;
	
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
		stopped = false;
		cancelled = false;
	}
	
	public void addListener(CSapiListener listener){
		sListener = listener;
	}
	
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
	
	public int getTotalVoices(){
		return voiceList.getSize();
	}
	
	public void setVoice(int index){
		if(index > voiceList.getSize() || index < 0)
			System.out.println("ERROR: Invalid MS SAPI Voice Index, Voice not changed");
		else
			voiceIndex = index;
	}
	
	public int getCurrentVoice(){
		return voiceIndex;
	}
	
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
	
	public float getVolume(){
		return volume/10;
	}
	
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
	
	public float getSpeed(){
		return (speed*20+10);
	}
	
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
		if(cancelled) stopped = true;
		//System.out.println("Exited Play Loop");
		//sListener.finished();
	}
	
	public synchronized void play2(String speech){
		System.out.println("Playing SAPI");
		toSpeak = speech;
		speaking = true;
		threadSuspended = false;
		notify();
	}
	
	public void run(){
		while(!terminated){
			if(speaking){
				speaking = false;
				play(toSpeak);
				if(!stopped){
					sListener.finished();
				}
				stopped = false;
				threadSuspended = true;
			} else if(!cancelled){
				cancelled = true;
				//System.out.println("Going to Wait");
				try {
	                sleep(200);
	                synchronized(this) {
	                    while (threadSuspended)
	                        wait();
	                }
	            } catch (InterruptedException e){
	            }
			}
		}
	}
	
	public void pause(){
		isPaused = true;
		sListener.paused();
		Dispatch.call(msTTS, "Pause");
	}
	
	public void resumeSP(){
		isPaused = false;
		sListener.resumed();
		Dispatch.call(msTTS, "Resume");
	}
	
	public boolean isPaused(){
		return isPaused;
	}
	
	public synchronized void cancel(){
		isPaused = false;
		if(speaking) {
			cancelled = true;
			stopped = true;
		}
		Dispatch.call(msTTS, "Skip", "Sentence", new Variant(1));
		sListener.cancelled();
		//notify();
	}
	
	public synchronized void stopSP(){
		isPaused = false;
		if(speaking) stopped = true;
		Dispatch.call(msTTS, "Skip", "Sentence", new Variant(1));
		sListener.cancelled();
		//notify();
	}
	
	public void exportToWAV(String speech, String target){
		Dispatch.call(msFS, "Open", target, new Variant(3), new Variant(true));
		Dispatch.putRef(msTTS, "AudioOutputStream", msFS);
		Dispatch.call(msTTS, "Speak", speech);
		Dispatch.call(msFS,"Close");
	}
	
	public void init(){
		try {
	    	
	    	Dispatch voices = Dispatch.call(msTTS, "GetVoices").toDispatch();
	    	
	    	Variant itemCount = Dispatch.get(voices, "Count");
	    	for(int i=0; i < itemCount.getInt(); i++) {
	    		Dispatch voice = Dispatch.call(voices, "Item", i).toDispatch();
	    		Variant voiceName = Dispatch.call(voice, "GetDescription");
	    		voiceList.addElement(new MySapiVoice(voiceName.toString()));
	    	}
	    	
	    	/*for(int i = 0; i < voiceList.getSize(); i++){
	    		MySapiVoice voice = (MySapiVoice) voiceList.getElementAt(i);
	    		System.out.println(voice.getName());
	    	}*/
	    	
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public void shutdown(){
		terminated = true;
	}

}

class MySapiVoice {

	private String name;
	
    public MySapiVoice(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
}
