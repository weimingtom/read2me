package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.graphics.FontData;
import java.util.Properties;
import java.io.*;

public class CPreferenceWindow {

	private Font font;
	private Color fontColor;

	/**main window: font*/
	private Font fontM;
	/** main window: text color background*/
	private Color colorMTextBkg;
	/**main window: color */
	private Color colorMWindow;
	/**main window text color*/
	private Color colorFont;

	public int selected;


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

	//final Shell prefWin;
	Shell prefWin;
	private Properties prop;
	private boolean fontSet=false;


	//public CPreferenceWindow(final Shell s, final Display d, final StyledText textArea, final Label volumeLabel, final Label speedLabel, String[] voices){

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

	public void display(final Shell s, final Display d, final StyledText textArea, final Label volumeLabel, final Label speedLabel, String[] voices){

		prefWin = new Shell(s,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//prefWin = new Shell();
		prefWin.setSize(300, 300);
		prefWin.setText("Preferences");

		//the text background color label
		labelBkgColor = new Label(prefWin, SWT.PUSH);
		labelBkgColor.setBounds(30, 30, 110, 15);
		labelBkgColor.setText("Text Background Color");

		//the windows color label
		labelWinColor = new Label(prefWin, SWT.PUSH);
		labelWinColor.setBounds(30, 80, 120, 15);
		labelWinColor.setText("Windows color");

		//the font label
		labelFont = new Label(prefWin, SWT.PUSH);
		labelFont.setBounds(30, 120, 120, 50);
		labelFont.setText("Font");

		//the voice label
		labelVoice = new Label(prefWin, SWT.PUSH);
		labelVoice.setBounds(30, 180, 120, 20);
		labelVoice.setText("Select voice: ");


		//the combo box to select the voices
		voiceSel = new Combo(prefWin, SWT.READ_ONLY);
		voiceSel.setBounds(180, 180, 90, 40);
		voiceSel.setItems(voices);
		voiceSel.select(selected);
		voiceSel.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//sets the customization options to what they were before
				selected = voiceSel.getSelectionIndex();
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});


//		the text background color change button
		final Button bTextBkgColor = new Button(prefWin, SWT.PUSH);
		bTextBkgColor.setBounds(180, 25, 70, 25);
		bTextBkgColor.setText("Change");
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

//		the windows color change color
		final Button bWindowsColor = new Button(prefWin, SWT.PUSH);
		bWindowsColor.setBounds(180, 75, 70, 25);
		bWindowsColor.setText("Change");
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
				prefWin.setBackground(new Color(d, newColor));
				labelWinColor.setBackground(new Color(d, newColor));
				labelFont.setBackground(new Color(d, newColor));
				labelVoice.setBackground(new Color(d, newColor));
				volumeLabel.setBackground(new Color(d, newColor));
				speedLabel.setBackground(new Color(d, newColor));
			}
		});

		//the font change button
		final Button bFont = new Button(prefWin, SWT.PUSH);
		bFont.setBounds(180, 125, 70, 25);
		bFont.setText("Change");
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


		final Button savePrefButton = new Button(prefWin, SWT.PUSH);
		savePrefButton.setText("Save");
		savePrefButton.setBounds(90, 220, 55, 25);

		final Button cancelPrefButton = new Button(prefWin, SWT.PUSH);
		cancelPrefButton.setText("Cancel");
		cancelPrefButton.setBounds(155, 220, 55, 25);


		//gets the properties (Font, Color...) from the main window
		//and apply them to the preference window
		prefWin.setBackground(s.getBackground());
		labelBkgColor.setBackground(textArea.getBackground());
		labelWinColor.setBackground(s.getBackground());
		labelFont.setBackground(s.getBackground());
		labelFont.setFont(textArea.getFont());
		labelFont.setForeground(textArea.getForeground());
		labelVoice.setBackground(s.getBackground());

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
				s.setBackground(colorMWindow);
				volumeLabel.setBackground(colorMWindow);
				speedLabel.setBackground(colorMWindow);
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

	public void savePref()
	{

		prop.setProperty("textBkgColorR" , ""+labelBkgColor.getBackground().getRed());
		prop.setProperty("textBkgColorG" , ""+labelBkgColor.getBackground().getGreen());
		prop.setProperty("textBkgColorB" , ""+labelBkgColor.getBackground().getBlue());

		prop.setProperty("windowsColorR", ""+prefWin.getBackground().getRed());
		prop.setProperty("windowsColorG", ""+prefWin.getBackground().getGreen());
		prop.setProperty("windowsColorB", ""+prefWin.getBackground().getBlue());

		if(fontSet){
			prop.setProperty("fontName", ""+font.getFontData()[0].getName());
			prop.setProperty("fontHeight", ""+font.getFontData()[0].getHeight());
			prop.setProperty("fontStyle", ""+font.getFontData()[0].getStyle());

			prop.setProperty("fontColorR", ""+fontColor.getRed());
			prop.setProperty("fontColorG", ""+fontColor.getGreen());
			prop.setProperty("fontColorB", ""+fontColor.getBlue());
		}

		prop.setProperty("voiceIndex", ""+selected);


		// Write properties file.
		try {
			prop.store(new FileOutputStream("user.prop"), null);
		} catch (IOException e) {
		}

	}

}
