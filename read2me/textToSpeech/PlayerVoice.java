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

/**
 * 
 * @author Stefan Estrada
 *
 * Object used to store Voice properties including
 * voice name, mode (whether it is a FreeTTS or MS Sapi voice),
 * and index (where in the voice list it is placed).
 */
public class PlayerVoice {

	private String name;
	private int mode;
	private int index;
	
    public PlayerVoice(String name, int mode, int index) {
    	this.name = name;
    	this.mode = mode;
    	this.index = index;
    }
    
    public String getName() {
    	return name;
    }
    
    public int getIndex() {
    	return index;
    }
    
    public int getMode(){
    	return mode;
    }
}
