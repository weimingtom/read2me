package sapiInterface;

public class MainSapiTest {

	static public void startThread() {
		class MyGuiThread extends Thread {
			
			public void run() {
				SapiControl sapi = new SapiControl();
				sapi.start();
			}
		}

		MyGuiThread gui = new MyGuiThread();
		System.out.println("Start main sapi thread");
		gui.start();
		
		for(int i=0; i < 15; i++) {
			try {
			Thread.sleep(200);
			System.out.println("tick,");
			}catch (Exception e) {
				System.out.println("thread err");
				e.printStackTrace();
			}
		}
		
	}
	
	static void startSimple() {
		SapiControl sapi = new SapiControl();
		sapi.start();
	}
	
	public static void main(String[] args) {
		
		System.out.println("Start Sapi");
		
		startSimple();
		
		System.out.println("End happy");
		
		
	}
}
