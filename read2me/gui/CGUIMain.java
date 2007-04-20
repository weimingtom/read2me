package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
//import java.awt.Font;
import java.util.Properties;
import java.io.*;
//import org.eclipse.swt.graphics.Color;
//import textToSpeech.*; 


public class CGUIMain {
	private static boolean isPlaying = false;
	public static Shell s;
	public static Display d;
	private static StyledText textArea;
	private final Button Bplay;
	private Button BbackParagraph;
	private Button Bback;
	private Button Bnext;
	private Button BnextParagraph;
	private Button Bstop;
	private Button Bclear;
	private CGUICommandInterface guiControl;
	private final Image Iplay; 
	private final Image Ipause;
	private static CToolbar toolbar;
	private Label volumeLabel;
	private Label speedLabel;
	private Label editLabel;
	private static int voiceIndex =0;
	private Properties prop;


	// ------------------ Main ------------------------------
	public CGUIMain(final CGUICommandInterface _guiControl, String[] voices){
		guiControl = _guiControl;
		/*
		final CPlayerInterface player = new CPlayer();   
		player.createSynthesizers();
		player.setSynthesizer(1);
		 */
		//final CGUICommandInterface guiControl = new CGUICommand();

		d = new Display();
		s = new Shell(d);



		GridLayout layout = new GridLayout();
		layout.numColumns = 13;
		layout.makeColumnsEqualWidth = false;
		layout.horizontalSpacing = 0;
		layout.marginTop = 5;
		layout.marginLeft = 2;
		layout.marginRight = 2;

		s.setLayout(layout);
		s.setSize(660,450);

		//s.setBackground(d.getSystemColor(SWT.COLOR_BLUE));
		s.setMinimumSize(660, 450);
		s.setText("Read2Me!");

		Iplay = new Image(d, "./Images/Play.png");
		Ipause = new Image(d, "./Images/Pause.png");
		final Image Istop = new Image(d, "./Images/Stop.png");
		final Image Iback = new Image(d, "./Images/Back.png");
		final Image IbackParagraph = new Image(d, "./Images/BackParagraph.png");
		final Image Inext = new Image(d, "./Images/Next.png");
		final Image InextParagraph = new Image(d, "./Images/NextParagraph.png");
		final Image Ivolume = new Image(d, "./Images/Sound.png");
		final Image Ispeed = new Image(d, "./Images/speedFull.png");
		final Image Imp3 = new Image(d, "./Images/Export.png");
		final Image Itip = new Image(d, "./Images/tip.png");
		final Image Iedit = new Image(d,"./Images/Pencil.png");
		final Image Ixedit = new Image(d,"./Images/PencilX.png");
		final Image Iclear = new Image(d,"./Images/Clear.png");

		// Back paragraph button
		GridData data = new GridData(SWT.CENTER);
		BbackParagraph = new Button(s, SWT.PUSH);
		BbackParagraph.setImage(IbackParagraph);
		BbackParagraph.setLayoutData(data);
		BbackParagraph.setToolTipText("One paragraph back");

		// One sentence back Button
		data = new GridData(SWT.CENTER);
		Bback = new Button(s, SWT.PUSH);
		Bback.setImage(Iback);
		Bback.setLayoutData(data);
		Bback.setToolTipText("One sentence back");

		// Next sentence Button
		data = new GridData(SWT.CENTER);
		Bnext = new Button(s, SWT.PUSH);
		Bnext.setImage(Inext);
		Bnext.setLayoutData(data);
		Bnext.setToolTipText("Next sentence");

		// Next Paragraph Button
		data = new GridData(SWT.CENTER);
		BnextParagraph = new Button(s, SWT.PUSH);
		BnextParagraph.setImage(InextParagraph);
		BnextParagraph.setLayoutData(data);
		BnextParagraph.setToolTipText("Next paragraph");

		// Separator
		Button BnotShown = new Button(s, SWT.PUSH);
		BnotShown.setText("Not shown");
		BnotShown.setVisible(false);
		BnotShown.setLayoutData(data);

		// Play pause Button
		data = new GridData(SWT.CENTER);
		Bplay = new Button(s, SWT.PUSH);
		Bplay.setImage(Iplay);
		Bplay.setLayoutData(data);
		Bplay.setToolTipText("Play / Pause button");


		// Stop Button
		data = new GridData(SWT.CENTER);
		Bstop = new Button(s, SWT.PUSH);
		Bstop.setImage(Istop);
		Bstop.setLayoutData(data);
		Bstop.setToolTipText("Stop button");
		
		//Edit Icon

		// Edit or Non Edit label
		data = new GridData(SWT.CENTER);
		editLabel = new Label(s, SWT.PUSH);
		editLabel.setImage(Iedit);
		editLabel.setLayoutData(data);
		editLabel.setToolTipText("you can edit the text Area :)");
		
		// Separator
		Button BnotShown2 = new Button(s, SWT.PUSH);
		BnotShown2.setText("Not shown");
		BnotShown2.setVisible(false);
		BnotShown2.setLayoutData(data);

		// Clear Button
		data = new GridData(SWT.CENTER);
		Button Bclear = new Button(s, SWT.PUSH);
		Bclear.setImage(Iclear);
		Bclear.setLayoutData(data);
		Bclear.setToolTipText("Clear the text Area");
		
		// MP3 Button
		data = new GridData(SWT.CENTER);
		Button Bmp3 = new Button(s, SWT.PUSH);
		Bmp3.setImage(Imp3);
		Bmp3.setLayoutData(data);
		Bmp3.setToolTipText("Convert to mp3");

		// Separator
		data = new GridData(SWT.CENTER);
		Button BnotShown3 = new Button(s, SWT.PUSH);
		BnotShown3.setText("Not shown");
		BnotShown3.setVisible(false);
		BnotShown3.setLayoutData(data);

		// tips Buttton
		data = new GridData(SWT.CENTER);
		Button Btip = new Button(s, SWT.PUSH);
		Btip.setImage(Itip);
		Btip.setLayoutData(data);
		Btip.setToolTipText("Help");

		// end of first row


		// Volume label
		data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		data.verticalIndent = 10;
		data.horizontalSpan = 1;
		volumeLabel = new Label(s, SWT.BEGINNING);
		volumeLabel.setImage(Ivolume);
		Ivolume.setBackground(s.getBackground());
		//volumeLabel.setForeground(s.getBackground());
		volumeLabel.setLayoutData(data);
		volumeLabel.setToolTipText("Volume");

		
		// text area
		data = new GridData(GridData.FILL_BOTH);
		data.verticalIndent = 3;
		data.verticalSpan = 3;
		data.horizontalSpan = 11;
		data.grabExcessHorizontalSpace = true;
		textArea = new StyledText(s, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		textArea.setWordWrap(true);
		textArea.setLayoutData(data);

		// Speed Label
		data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		data.horizontalIndent = 5;
		data.verticalIndent = 10;
		data.horizontalSpan = 1;
		speedLabel = new Label(s, SWT.BEGINNING);
		speedLabel.setImage(Ispeed);
		speedLabel.setLayoutData(data);
		speedLabel.setToolTipText("Reading speed");

		// Left Slider
		data = new GridData(SWT.CENTER | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
		data.verticalIndent = 10;
		data.horizontalIndent = -2;
		data.horizontalSpan = 1;
		final Slider Svolume = new Slider(s, SWT.VERTICAL);
		Svolume.setMaximum(13);
		Svolume.setMinimum(0);
		Svolume.setIncrement(1);
		Svolume.setPageIncrement(5);
		Svolume.setThumb(3);  // dimension of the thing
		Svolume.setToolTipText("Adjust the volume");
		Svolume.setSelection(Svolume.getMaximum() - 10 + Svolume.getMinimum() - Svolume.getThumb());
		guiControl.volume(10);
		Svolume.setLayoutData(data);

		// Right Slider
		data = new GridData(SWT.CENTER /*| GridData.HORIZONTAL_ALIGN_CENTER*/ | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
		data.horizontalIndent = 17;
		data.verticalIndent = 10;
		data.horizontalSpan = 1;
		final Slider Sspeed = new Slider(s, SWT.VERTICAL);
		Sspeed.setMaximum(43);
		Sspeed.setMinimum(1);
		Sspeed.setIncrement(1);
		Sspeed.setPageIncrement(5);
		Sspeed.setThumb(3);
		Sspeed.setToolTipText("Adjust the reading speed");
		Sspeed.setSelection(Sspeed.getMaximum() - 15 + Sspeed.getMinimum() - Sspeed.getThumb());
		guiControl.speed(150);
		Sspeed.setLayoutData(data);

		// Left Value
		data = new GridData(SWT.CENTER | GridData.HORIZONTAL_ALIGN_CENTER );
		data.horizontalSpan = 1;
		data.verticalAlignment = SWT.BEGINNING;
		final Text volumeValue = new Text(s, SWT.BORDER | SWT.SINGLE);
		volumeValue.setEditable(false);
		int tempVolume = Svolume.getMaximum() - Svolume.getSelection() + Svolume.getMinimum() - Svolume.getThumb();
		volumeValue.setText(""+ tempVolume);
		volumeValue.setLayoutData(data);

		/*
		//not shown button
		data = new GridData(SWT.CENTER);
		data.horizontalSpan = 9;
		Button BnotShown4 = new Button(s, SWT.PUSH);
		BnotShown4.setText("Not shown");
		BnotShown4.setVisible(false);
		BnotShown4.setLayoutData(data);
*/
		// Right Value
		data = new GridData(GridData.CENTER | GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 1;
		final Text speedValue = new Text(s, SWT.BORDER | SWT.SINGLE);
		speedValue.setEditable(false);
		int tempSpeed = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum() - Sspeed.getThumb();
		speedValue.setText(""+tempSpeed);
		speedValue.setLayoutData(data);

		//get the user customization (window color...)
		getUserProperties();

		toolbar = new CToolbar(s,d, textArea, volumeLabel, speedLabel, editLabel, voices);
		
		// LISTENERS

		// link between the 2 classes
		guiControl.setGUIMain(this);

		// Listener Back Paragraph
		BbackParagraph.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				//guiControl.setText(getText());
				updateGUIControl(guiControl);
				guiControl.backParagraph();
				highlight(guiControl);
				System.out.println("backparagraph");
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		}); 
		// Listener back sentence
		Bback.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				//guiControl.setText(getText());
				updateGUIControl(guiControl);
				guiControl.backSentence();
				highlight(guiControl);

			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Listener next sentence
		Bnext.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				//guiControl.setText(getText());
				updateGUIControl(guiControl);
				guiControl.nextSentence();
				highlight(guiControl);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Listener next paragraph
		BnextParagraph.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				//updateGUIControl(guiControl);
				updateGUIControl(guiControl);
				guiControl.nextParagraph();
				highlight(guiControl);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Listener Play/pause button
		Bplay.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {

				if(textArea.getText().length() != 0)
				{
					updateGUIControl(guiControl);
					editLabel.setImage(Ixedit);
					editLabel.setToolTipText("You can't edit - Press stop to edit");
					isPlaying = guiControl.play(isPlaying);
					if(isPlaying == true)
					{
						Bplay.setImage(Ipause);
						textArea.setEditable(false);	
					}
					else
					{
						Bplay.setImage(Iplay);
						textArea.setEditable(false);
					}

					highlight(guiControl);
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Listener Stop button
		Bstop.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				//textArea.setText("Stop playing");
				updateGUIControl(guiControl);
				isPlaying = guiControl.stop();
				editLabel.setImage(Iedit);
				editLabel.setToolTipText("You can edit the textArea :)");
				Bplay.setImage(Iplay);
				textArea.setEditable(true);
				//isPlaying = false;
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		// listener clear button
		Bclear.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				textArea.setText("");
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		// Listener mp3 button
		Bmp3.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				guiControl.mp3();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Listener Tip button
		Btip.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final Shell shellBro = new Shell(d);
				shellBro.setLayout(new FillLayout());
				shellBro.setSize(500, 700);
				shellBro.setText("Tips and Tricks");
				shellBro.setLocation(600, 100);
				String curDir = System.getProperty("user.dir");
				String folder = "\\images\\tips.html";
				curDir= curDir+folder;

				final Browser browser = new Browser(shellBro,SWT.NONE);

				//browser.setUrl(curDir);
				browser.setUrl("www.com");
				shellBro.open();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		// Listener Volume slider
		Svolume.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int VValue = Svolume.getMaximum() - Svolume.getSelection() + Svolume.getMinimum() - Svolume.getThumb();
				volumeValue.setText("" +VValue);
				//player.setVolume(VValue);
				guiControl.volume(VValue);
			}
		});
		// Listener Speed Slider
		Sspeed.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int SValue = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum() - Sspeed.getThumb();
				speedValue.setText("" +SValue);
				//player.setSpeakingSpeed(SValue*10);
				guiControl.speed(SValue*10);
			}
		});

		s.open();
		while(!s.isDisposed()){
			// check if we need to update the selection
			if(guiControl.getNeedUpdate())
			{
				highlight(guiControl);
				guiControl.setNeedUpdate();
			}
			if(guiControl.getNeedToStop())
			{
				isPlaying = false;
				Bplay.setImage(Iplay);
				editLabel.setImage(Iedit);
				editLabel.setToolTipText("You can edit the textArea :)");
				textArea.setEditable(true);
				guiControl.setNeedToStop();
			}

			if(!d.readAndDispatch())
				d.sleep();
		}
		//player.stop();

		guiControl.stop();

		d.dispose();
		System.exit(0);
	}

	private static String modifyText(String _t)
	{
		_t = _t.replace("U.S.A.", "United State Of America");
		_t = _t.replace("u.s.a.", "usa");
		_t = _t.replace("Dr.", "Doctor");
		_t = _t.replace("dr.", "drive");
		_t = _t.replace("C.N.N.", "CNN");
		_t = _t.replace("Pr.", "Professor");
		_t = _t.replace("U.S.", "US");
		_t = _t.replace("u.s.", "US");
		
		
		System.out.println(_t);
		return _t;
	}
	
	private static void updateGUIControl(final CGUICommandInterface guiControl)
	{
		int t = textArea.getCaretOffset();
		String text = modifyText(textArea.getText());
		textArea.setText(text);
		guiControl.setText(text);
		guiControl.setPosition(t);
		//if(toolbar.getIndexVoice() == -1)
			//guiControl.setVoiceIndex(voiceIndex);
		//else
			guiControl.setVoiceIndex(toolbar.getIndexVoice());
		//if(textArea.getSelectionText() != "")
		//guiControl.setText(textArea.getSelectionText());
	}

	private static void highlight(final CGUICommandInterface guiControl)
	{
		textArea.setCaretOffset(guiControl.getSentence()[0]);
		textArea.setSelection(guiControl.getSentence()[0],guiControl.getSentence()[1]);

	}

	public void updateListeners()
	{
		d.wake();
	}

	private void getUserProperties(){
		prop = new Properties();
		//Read properties file.
		try {
			prop.load(new FileInputStream("user.prop"));
			s.setBackground(new Color(d, new RGB(  Integer.parseInt(prop.getProperty("windowsColorR")) , Integer.parseInt(prop.getProperty("windowsColorG")) , Integer.parseInt(prop.getProperty("windowsColorB")) )));
			volumeLabel.setBackground(s.getBackground());
			speedLabel.setBackground(s.getBackground());
			editLabel.setBackground(s.getBackground());
			textArea.setBackground(new Color(d, new RGB( Integer.parseInt(prop.getProperty("textBkgColorR")) , Integer.parseInt(prop.getProperty("textBkgColorG")) , Integer.parseInt(prop.getProperty("textBkgColorB")) )));
			//voiceIndex = Integer.parseInt(prop.getProperty("voiceIndex"));
			//make sure that a font was specified
			if(prop.getProperty("fontColorR")!=null){
				textArea.setForeground(new Color(d, new RGB( Integer.parseInt(prop.getProperty("fontColorR")) , Integer.parseInt(prop.getProperty("fontColorG")) , Integer.parseInt(prop.getProperty("fontColorB")) )));
				Font font = new Font(d, new FontData(prop.getProperty("fontName"), Integer.parseInt(prop.getProperty("fontHeight")), Integer.parseInt(prop.getProperty("fontStyle"))) );
				textArea.setFont(font);
			}
			
			System.out.println("user.prop found. Customization applied");
		} 
		catch (IOException e) {
			System.out.println("no user.prop found or a field is missing");
		}

	}
}
