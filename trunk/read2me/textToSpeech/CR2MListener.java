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

import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;

/**
 * 
 * 
 * 
 *Default Listener used by CPlayer when one is not set.
 */
public class CR2MListener implements SpeakableListener {

    private String formatEvent(SpeakableEvent event) {
        return event.paramString()+": "+event.getSource();        
    }
    
    public void markerReached(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableCancelled(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableEnded(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakablePaused(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableResumed(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void speakableStarted(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void topOfQueue(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }

    public void wordStarted(SpeakableEvent event) {
        System.out.println(formatEvent(event));
    }
}
