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

package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
/*import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.FontDialog;*/
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import java.util.Properties;
import java.io.*;

/**
 * This is the preference class.
 * Allows the customization of Read2Me!
 * Allows to change the windows color, the text color/font/size and the voice.
 * Allows to add specific word like abbreviations
 * and the way they should be pronounced 
 */
public class CPreferenceWindow {

	/** the text font*/
	private Font font;
	/** the font color*/
	private Color fontColor;
	/**main window: font*/
	private Font fontM;
	/** main window: text color background*/
	private Color colorMTextBkg;
	/**main window: color */
	private Color colorMWindow;
	/**main window text color*/
	private Color colorFont;
	/** index of the voice that has been selected */
	public int selected;
	/** index of the voice when we started the preference window */
	private int old_selected;


	/**the text background color label*/
	Label labelBkgColor;
	/**	the windows color label*/
	Label labelWinColor;
	/**the font label*/
	Label labelFont;
	/**the voice label*/
	Label labelVoice;
	/**the voice comboBox*/
	Combo voiceSel;
	/** the add abbreviation label*/
	Label labelAbbr;
	/** voices Array */
	String[] voicesArray;

	//final Shell prefWin;
	Shell prefWin;
	private Properties prop;
	private boolean fontSet=false;


	/**
	 * loads the user customization if there is one
	 */
	public CPreferenceWindow()
	{
		prop=new Properties();
		try {
			prop.load(new FileInputStream("user.prop"));
			selected = Integer.parseInt(prop.getProperty("voiceIndex"));
		} 
		catch (IOException e) {
			System.out.println("no voice found");
			selected = 0;
		}
	}


	/**
	 * @return the name of the selected voice
	 */
	public String getVoiceName()
	{
		return voicesArray[selected];
	}

	/**
	 * displays the preference window
	 * @param s the shell
	 * @param d the display
	 * @param textArea 
	 * @param volumeLabel 
	 * @param speedLabel
	 * @param editLabel
	 * @param voices
	 * @param Svolume the volume scale
	 * @param Sspeed the reading speed scale
	 * @param voice
	 */
	public void display(final Shell s, final Display d, final StyledText textArea, final Label volumeLabel, final Label speedLabel, final Label editLabel, final String[] voices, final Scale Svolume, final Scale Sspeed, final MenuItem voice){
		voicesArray = voices;
		old_selected = selected;
		prefWin = new Shell(s,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//prefWin = new Shell();
		prefWin.setSize(300, 370);
		prefWin.setText("Preferences");
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		layout.horizontalSpacing = 15;
		layout.marginLeft = 20;
		layout.marginTop = 20;
		layout.verticalSpacing = 30;
		
		prefWin.setLayout(layout);

		//the text background color label
		labelBkgColor = new Label(prefWin, SWT.PUSH);
		//labelBkgColor.setBounds(30, 30, 110, 15);
		labelBkgColor.setText("Text Background Color");
		
		//the text background color change button
		final Button bTextBkgColor = new Button(prefWin, SWT.PUSH);
		//bTextBkgColor.setBounds(180, 25, 70, 25);
		bTextBkgColor.setText(" Change ");
		bTextBkgColor.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(s, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
				cd.setText("Text Background Color");
				cd.setRGB(new RGB(255, 255, 255));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}
				labelBkgColor.setBackground(new Color(d, newColor));
				textArea.setBackground(new Color(d, newColor));
				//textArea.setBidiColoring(true);
			}
		});

		//the windows color label
		labelWinColor = new Label(prefWin, SWT.PUSH);
		//labelWinColor.setBounds(30, 80, 120, 15);
		labelWinColor.setText("Windows color");
		
//		 the windows color change color
		final Button bWindowsColor = new Button(prefWin, SWT.PUSH);
		//bWindowsColor.setBounds(180, 75, 70, 25);
		bWindowsColor.setText(" Change ");
		bWindowsColor.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(s, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
				cd.setText("Windows color");
				cd.setRGB(new RGB(255, 255, 255));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}
				//t.setBackground(new Color(d, newColor));
				s.setBackground(new Color(d, newColor));
				editLabel.setBackground(new Color(d, newColor));
				Svolume.setBackground(new Color(d, newColor));
				Sspeed.setBackground(new Color(d, newColor));
				prefWin.setBackground(new Color(d, newColor));
				labelWinColor.setBackground(new Color(d, newColor));
				labelFont.setBackground(new Color(d, newColor));
				labelVoice.setBackground(new Color(d, newColor));
				labelAbbr.setBackground(new Color(d, newColor));
				volumeLabel.setBackground(new Color(d, newColor));
				speedLabel.setBackground(new Color(d, newColor));
			}
		});

		//the font label
		labelFont = new Label(prefWin, SWT.PUSH);
		//labelFont.setBounds(30, 120, 120, 50);
		labelFont.setText("Font");
		
//		the font change button
		final Button bFont = new Button(prefWin, SWT.PUSH);
		//bFont.setBounds(180, 125, 70, 25);
		bFont.setText(" Change ");
		bFont.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FontDialog fontDialog = new FontDialog(s, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
				if(font != null)
					fontDialog.setFontList(font.getFontData());
				FontData fontData = fontDialog.open();
				if(fontData != null) {
					if(font != null)
						font.dispose();
					font = new Font(d, fontData);
					fontColor = new Color(d, fontDialog.getRGB());
					labelFont.setFont(font);
					textArea.setFont(font);
					labelFont.setForeground(fontColor);
					textArea.setForeground(fontColor);
					fontSet=true;
				}else{
					fontSet=false;
					System.out.println("Setting font action canceled.");
				}
			}
		});

		//the voice label
		labelVoice = new Label(prefWin, SWT.PUSH);
		//labelVoice.setBounds(30, 180, 120, 20);
		labelVoice.setText("Select voice: ");

//		the combo box to select the voices
		voiceSel = new Combo(prefWin, SWT.READ_ONLY);
		//voiceSel.setBounds(180, 180, 90, 40);
		voiceSel.setItems(voices);
		voiceSel.select(selected);
		voiceSel.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//sets the customization options to what they were before
				selected = voiceSel.getSelectionIndex();
				voice.setText("Current voice: "+voices[selected]);
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});
		
		//the add abbreviation label
		labelAbbr = new Label(prefWin, SWT.PUSH);
		//labelAbbr.setBounds(30, 230, 120, 20);
		labelAbbr.setText("Abbreviations: ");
		
		//the abbreviation manage button
		final Button bAbbr = new Button(prefWin, SWT.PUSH);
		//bAbbr.setBounds(180, 225, 70, 25);
		bAbbr.setText(" Manage ");
		bAbbr.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				File f = new File(".\\resources\\myWords.txt");
				if(f.exists())
					Program.launch(".\\resources\\myWords.txt");
				else
				{
					try
					{
						PrintWriter file = new PrintWriter(new BufferedWriter
				                  (new FileWriter(".\\resources\\myWords.txt")));
						file.println("#you can add here all the special words (i.e. abbreviations)");
						file.println("#and the way they should be pronounced");
						file.println("#use:");
						file.println("#original word = word you want it to be pronounced");
						file.println("");
						file.println("Mr. = Mister");
						file.println("Mme = Madame");
						file.println("Mrs. = Miss");
						file.println("U.S.A.=USA");
						file.close();
						Program.launch(".\\resources\\myWords.txt");
					}
					catch(Exception ex)
					{
						System.out.println("can't create file :(");
					}
					
					
				}
			}
		});

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		final Button savePrefButton = new Button(prefWin, SWT.PUSH);
		savePrefButton.setLayoutData(data);
		savePrefButton.setText("   Save   ");
		
		//savePrefButton.setBounds(90, 290, 55, 25);

		final Button cancelPrefButton = new Button(prefWin, SWT.PUSH);
		cancelPrefButton.setText("  Cancel  ");
		//cancelPrefButton.setBounds(155, 290, 55, 25);


		//gets the properties (Font, Color...) from the main window
		//and apply them to the preference window
		prefWin.setBackground(s.getBackground());
		labelBkgColor.setBackground(textArea.getBackground());
		labelWinColor.setBackground(s.getBackground());
		labelFont.setBackground(s.getBackground());
		labelFont.setFont(textArea.getFont());
		labelFont.setForeground(textArea.getForeground());
		labelVoice.setBackground(s.getBackground());
		labelAbbr.setBackground(s.getBackground());


		//save the main window customization options
		//useful if the user decides to cancel his choice
		fontM = textArea.getFont();
		colorMTextBkg = textArea.getBackground();
		colorMWindow = s.getBackground();
		colorFont = textArea.getForeground();

		prefWin.open();

		cancelPrefButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//sets the customization options to what they were before
				selected = old_selected;
				voice.setText("Current voice: "+voices[selected]);
				s.setBackground(colorMWindow);
				volumeLabel.setBackground(colorMWindow);
				speedLabel.setBackground(colorMWindow);
				editLabel.setBackground(colorMWindow);
				Svolume.setBackground(colorMWindow);
				Sspeed.setBackground(colorMWindow);
				textArea.setBackground(colorMTextBkg);
				textArea.setFont(fontM);
				textArea.setForeground(colorFont);
				
				prefWin.close();
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		savePrefButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				savePref();
				prefWin.close();
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});
	}

	/**
	 * saves the preferences to a file
	 * so when the user launches Read2Me!
	 * he automatically finds it as he left it
	 * (with the voice, the font, the color...
	 *
	 */
	public void savePref()
	{
		//saves the text background color
		prop.setProperty("textBkgColorR" , ""+labelBkgColor.getBackground().getRed());
		prop.setProperty("textBkgColorG" , ""+labelBkgColor.getBackground().getGreen());
		prop.setProperty("textBkgColorB" , ""+labelBkgColor.getBackground().getBlue());
		
//		saves the window color
		prop.setProperty("windowsColorR", ""+prefWin.getBackground().getRed());
		prop.setProperty("windowsColorG", ""+prefWin.getBackground().getGreen());
		prop.setProperty("windowsColorB", ""+prefWin.getBackground().getBlue());

		//saves the font/fontcolor/fonstyle/fontsize
		if(fontSet){
			prop.setProperty("fontName", ""+font.getFontData()[0].getName());
			prop.setProperty("fontHeight", ""+font.getFontData()[0].getHeight());
			prop.setProperty("fontStyle", ""+font.getFontData()[0].getStyle());

			prop.setProperty("fontColorR", ""+fontColor.getRed());
			prop.setProperty("fontColorG", ""+fontColor.getGreen());
			prop.setProperty("fontColorB", ""+fontColor.getBlue());
		}
		//saves the voice
		prop.setProperty("voiceIndex", ""+selected);


		// Write properties file.
		try {
			prop.store(new FileOutputStream("user.prop"), null);
		} catch (IOException e) {
		}

	}

}
