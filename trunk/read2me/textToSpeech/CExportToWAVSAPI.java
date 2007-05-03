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
 * 
 * Uses CSapiControl Class to export text as WAV speech.
 *
 */
public class CExportToWAVSAPI extends Thread{
	
	private CSapiControl control;
	private PlayerVoice voice;
	private String text;
	private String target;
	
	/**
	 * Creates CSapiControl instance for exporting WAV file.
	 * 
	 * @param text String of text to be converted to a WAV file.
	 * @param target Location where the WAV file will be exported to
	 * @param voice Voice to be used for conversion
	 */
	public CExportToWAVSAPI(String text, String target, PlayerVoice voice){
		control = new CSapiControl();
		this.voice = voice;
		this.text = text;
		this.target = target;
	}
	
	/**
	 * Export WAV file without creating a new thread.
	 *
	 */
	public void exportToWAV(){
		control.setVoice(voice.getIndex());
		control.exportToWAV(text, target);
	}
	
	/**
	 * Export WAV file by creating a new thread.
	 */
	public void run(){
		control.setVoice(voice.getIndex());
		control.exportToWAV(text, target);
	}

}
