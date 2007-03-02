package clipMonitor;

import java.util.EventListener;


public interface ClipListener extends EventListener{

	public abstract void clipEvent(ClipEvent event);
	
}
