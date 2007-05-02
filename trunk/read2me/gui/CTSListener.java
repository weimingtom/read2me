/*
This file is part of Read2Me!

Read2Me! is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Read2Me!is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Read2Me!. If not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package gui;

import textToSpeech.*;

public class CTSListener implements CSapiListener{
	
	private CGUICommand GUIMain;
	
	public CTSListener(CGUICommand _GUIMain){
		GUIMain = _GUIMain;
	}
	
	public void started(){
		System.out.println("Started MS SAPI Sentence");
	}
	
	public void paused(){
		System.out.println("MS SAPI Paused");
	}
	
	public void resumed(){
		System.out.println("MS SAPI Resumed");
	}
	
	public void cancelled(){
		System.out.println("Cancelled MS SAPI Sentence");
	}
	
	public void finished(){
		System.out.println("Finished MS SAPI Sentence");
		GUIMain.eventEndSpeak();
	}

}
