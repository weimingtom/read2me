package sapiInterface;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.InvocationProxy;
import com.jacob.com.JacobObject;
import com.jacob.com.Variant;

public class SapiControl {

	ReadEvent re;
	
	public void start(){
		
		// good com discussion
		// http://www.codeguru.com/cpp/com-tech/atl/article.php/c3573/
		
		// not needed ComThread.InitMTA();
		
		//ActiveXComponent msTTS = new ActiveXComponent("SAPI.SpVoice");
		Dispatch msTTS = new Dispatch("SAPI.SpVoice");

	    //Object o_msTTS = msTTS.getObject();
	    try {
	    	
	    	// check EventInterests (used more to disable events)
	    	//Dispatch.put(msTTS, "EventInterests", 32);
	    	
	    	Variant voiceEvent = Dispatch.get(msTTS, "EventInterests"); 
	    	System.out.println("EventInterests " + voiceEvent);
	    	
	    	Dispatch voices = Dispatch.call(msTTS, "GetVoices").toDispatch();
	    	
	    	Variant itemCount = Dispatch.get(voices, "Count");
	    	for(int i=0; i < itemCount.getInt(); i++) {
	    		Dispatch voice = Dispatch.call(voices, "Item", i).toDispatch();
	    		Variant voiceName = Dispatch.call(voice, "GetDescription");
	    		System.out.println("\t" + i + " "+ voiceName + " " + itemCount);
	    	}
	    	
	    	
	    	//	    	 instantiate an event target object
	        re = new ReadEvent();
	        // hook it up to the sControl source
	        DispatchEvents de = new DispatchEvents(msTTS, re);
	        if (de == null)
	        	System.out.println("DID NOT create DispatchEvents");
	        else 
	        	System.out.println("DispatchEvents OK");
	        
        // run an expression from the command line

	    	
	    	//Object spkCmd = Dispatch.get(msTTS,"Speak").toDispatch();
	    	System.out.println("next " + JacobObject.getBuildVersion());
//	    	Variant i_spkCmd = Dispatch.invoke(msTTS, "Speak", Dispatch.Method,
//                    new Object[] {"Tell me about your day. And now."},
//                    new int[1]);
	    	Dispatch.call(msTTS, "Speak", "Tell me about your day.", new Variant(1));
	    	System.out.println("then");        
            
	    	
	    	// async call. Need to wait around for speak to read aloud
	    	
	    	for(int i=0; i < 15; i++) {
				try {
				Thread.sleep(200);
				System.out.println(".");
				}catch (Exception e) {
					System.out.println("thread err");
					e.printStackTrace();
				}
			}
	    	
	    	
	    	
//	      System.out.println("version="+xl.getProperty("Version"));
//	      System.out.println("version="+Dispatch.get((Dispatch) xlo, "Version"));
//	      xl.setProperty("Visible", new Variant(true));
//	      Object workbooks = xl.getProperty("Workbooks").toDispatch();
//	      Object workbook = Dispatch.get((Dispatch) workbooks,"Add").toDispatch();
//	      Object sheet = Dispatch.get(workbook,"ActiveSheet").toDispatch();
//	      Object a1 = Dispatch.invoke(sheet, "Range", Dispatch.Get,
//	                                  new Object[] {"A1"},
//	                                  new int[1]).toDispatch();
//	      Object a2 = Dispatch.invoke(sheet, "Range", Dispatch.Get,
//	                                  new Object[] {"A2"},
//	                                  new int[1]).toDispatch();
//	      Dispatch.put(a1, "Value", "123.456");
//	      Dispatch.put(a2, "Formula", "=A1*2");
//	      System.out.println("a1 from excel:"+Dispatch.get(a1, "Value"));
//	      System.out.println("a2 from excel:"+Dispatch.get(a2, "Value"));
//	      Variant f = new Variant(false);
//	      Dispatch.call(workbook, "Close", f);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    
	    // not needed ComThread.Release();
		
	}
	
	// older Jacob version uses: extends InvocationProxy
	
	public class ReadEvent  {
		
		public void StartStream (Variant[] args) {
			System.out.println("start stream...");
		}
		
		
		public void EndStream(Variant[] args) {
			System.out.println("EndStream...");
		}
		
		
		public void VoiceChange(Variant[] args) {
			System.out.println("VoiceChange...");
		}
		
		public void Bookmark(Variant[] args) {
			System.out.println("Bookmark...");
		}
		
		public void Phoneme(Variant[] args) {
			System.out.println("Phoneme...");
		}
		
		public void Viseme(Variant[] args) {
			System.out.println("Viseme...");
		}
		
		public void AudioLevel(Variant[] args) {
			System.out.println("AudioLevel...");
		}
		
		public void EnginePrivate(Variant[] args) {
			System.out.println("EnginePrivate...");
		}
				
		public void Word(Variant[] args) {
			System.out.println("word (5)...");
		}
		
		public void Sentence(Variant[] args) {
			System.out.println("sentence (2)...");
		}
		
//		// from the Jacob Excel Event unit test (method used in previous versions)
//		public Variant invoke(String methodName, Variant targetParameter[]) {
//			System.out.println("Received event from Windows program" + methodName);
//			return null;
//		}
		
	}
	
}
