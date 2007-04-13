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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.graphics.FontData;

public class CPreferenceWindow {

	Font font;
	Color fontColor;
	
	/**main window: font*/
	Font fontM;
	/** main window: text color background*/
	Color colorMTextBkg;
	/**main window: color */
	Color colorMWindow;
	/**main window text color*/
	Color colorFont;
	
	public CPreferenceWindow(final Shell s, final Display d, final StyledText textArea, final Label volumeLabel, final Label speedLabel){
		final Shell prefWin = new Shell(s,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		prefWin.setSize(300, 300);
		prefWin.setText("Preferences");

		//the text background color label
		final Label labelBkgColor = new Label(prefWin, SWT.PUSH);
		labelBkgColor.setBounds(30, 30, 110, 15);
		labelBkgColor.setText("Text Background Color");

		//the windows color label
		final Label labelWinColor = new Label(prefWin, SWT.PUSH);
		labelWinColor.setBounds(30, 90, 120, 15);
		labelWinColor.setText("Windows color");

		//the font label
		final Label labelFont = new Label(prefWin, SWT.PUSH);
		labelFont.setBounds(30, 145, 120, 40);
		labelFont.setText("Font");

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
		bWindowsColor.setBounds(180, 85, 70, 25);
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
				volumeLabel.setBackground(new Color(d, newColor));
				speedLabel.setBackground(new Color(d, newColor));
			}
		});

		//the font change button
		final Button bFont = new Button(prefWin, SWT.PUSH);
		bFont.setBounds(180, 145, 70, 25);
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
				}else{
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
				prefWin.close();
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});
	}

}
