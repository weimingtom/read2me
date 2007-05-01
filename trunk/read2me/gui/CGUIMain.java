package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
//import org.eclipse.gef.tools.AbstractTool.*;

//import java.awt.Font;
import java.util.Properties;
import java.io.*;
import java.util.*;


public class CGUIMain {
	
	/** To keep track if we are currently reading or not */
	private static boolean isPlaying = false;
	/** To keep track if we are in pause mod */
	private boolean isPaused=false;
	public static Shell s;
	public static Display d;
	/** StyledText widget for the text area */
	private static StyledText textArea;
	/** Previous paragraph button */
	private Button BbackParagraph;
	/** Previous sentence Button */
	private Button Bback;
	/** Next sentence button */
	private Button Bnext;
	/** Next paragraph Button */
	private Button BnextParagraph;
	/** Play button */
	private final Button Bplay;
	/** Stop Button */
	private Button Bstop;
	/** Interface for the GuiCommand class */
	private CGUICommandInterface guiControl;
	/** reference to the play image */
	private final Image Iplay;
	/** reference to the pause image */
	private final Image Ipause;
	/** Menu Toolbar widget */
	private static CToolbar toolbar;
	/** Label for the volume logo */
	private Label volumeLabel;
	/** Label for the speed logo */
	private Label speedLabel;
	/** label for the editable status */
	private Label editLabel;
//	private static int voiceIndex =0;
	/** Properties object */
	private Properties prop;
	/** Scale widget for the speed */
	private static Scale Sspeed;
	/** Scale widget for the speed */
	private static Scale Svolume;
	/** array containing all the words that the user wants to be read in another way */
	private static Vector<String> words;


	/**
	 * Function that will build the User Interface
	 * This function redraw and update the widgets when it's waken up otherwise it's in sleep mode.
	 * @param _guiControl reference to the control layer: all the functions are made there
	 * @param voices String Array containing all the names for all the voices 
	 */
	public CGUIMain(final CGUICommandInterface _guiControl, String[] voices){
		
		guiControl = _guiControl;

		d = new Display();
		s = new Shell(d);

		// vector containing the words that are going to be replaced in the text
		words = new Vector<String>(10,1);

		// The Gui is organised with a gridLAyout with 13 colomns
		GridLayout layout = new GridLayout();
		layout.numColumns = 13;
		layout.makeColumnsEqualWidth = false;
		layout.horizontalSpacing = 0;
		layout.marginTop = 5;
		layout.marginLeft = 2;
		layout.marginRight = 2;

		s.setLayout(layout);
		s.setSize(660,450);
		s.setMinimumSize(660, 450);
		s.setText("Read2Me!"); // title of the window

		//load the images
		Iplay = new Image(d, "./resources/Play.png");
		Ipause = new Image(d, "./resources/Pause.png");
		final Image Istop = new Image(d, "./resources/Stop.png");
		final Image Iback = new Image(d, "./resources/Back.png");
		final Image IbackParagraph = new Image(d, "./resources/BackParagraph.png");
		final Image Inext = new Image(d, "./resources/Next.png");
		final Image InextParagraph = new Image(d, "./resources/NextParagraph.png");
		final Image Ivolume = new Image(d, "./resources/Sound.png");
		final Image Ispeed = new Image(d, "./resources/speedFull.png");
		final Image Imp3 = new Image(d, "./resources/Export.png");
		final Image Itip = new Image(d, "./resources/tip.png");
		final Image Iedit = new Image(d,"./resources/Pencil.png");
		final Image Ixedit = new Image(d,"./resources/PencilX.png");
		final Image Iclear = new Image(d,"./resources/Clear.png");
		final Image WindowIcon = new Image(d,"./resources/WindowIcon.png");


		// Back paragraph button
		GridData data = new GridData(SWT.CENTER);
		BbackParagraph = new Button(s, SWT.PUSH);
		BbackParagraph.setImage(IbackParagraph);
		BbackParagraph.setLayoutData(data);
		BbackParagraph.setToolTipText("One paragraph back (Up Arrow when it's playing)");

		// One sentence back Button
		data = new GridData(SWT.CENTER);
		Bback = new Button(s, SWT.PUSH);
		Bback.setImage(Iback);
		Bback.setLayoutData(data);
		Bback.setToolTipText("One sentence back (Left Arrow when it's playing)");

		// Next sentence Button
		data = new GridData(SWT.CENTER);
		Bnext = new Button(s, SWT.PUSH);
		Bnext.setImage(Inext);
		Bnext.setLayoutData(data);
		Bnext.setToolTipText("Next sentence (Right Arrow when it's playing)");

		// Next Paragraph Button
		data = new GridData(SWT.CENTER);
		BnextParagraph = new Button(s, SWT.PUSH);
		BnextParagraph.setImage(InextParagraph);
		BnextParagraph.setLayoutData(data);
		BnextParagraph.setToolTipText("Next paragraph (Down Arrow when it's playing)");

		// Separator: used to create a blank
		Button BnotShown = new Button(s, SWT.PUSH);
		BnotShown.setText("Not shown");
		BnotShown.setVisible(false);
		BnotShown.setLayoutData(data);

		// Play / Pause Button
		//data = new GridData(SWT.CENTER);
		Bplay = new Button(s, SWT.PUSH);
		Bplay.setImage(Iplay);
		Bplay.setLayoutData(data);
		Bplay.setToolTipText("Play / Pause button (Space Bar when it's playing)");


		// Stop Button
		data = new GridData(SWT.CENTER);
		Bstop = new Button(s, SWT.PUSH);
		Bstop.setImage(Istop);
		Bstop.setLayoutData(data);
		Bstop.setToolTipText("Stop button (Return Key when it's playing)");

		// Edit or Non Edit label
		data = new GridData(SWT.CENTER);
		editLabel = new Label(s, SWT.PUSH);
		editLabel.setImage(Iedit);
		editLabel.setLayoutData(data);
		editLabel.setToolTipText("You can edit the text :)");

		// Separator: blank space
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
		data.horizontalIndent = 30;
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
		volumeLabel.setLayoutData(data);
		volumeLabel.setToolTipText("Volume");


		// text area (11 colomns)
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

		// Left Scale
		data = new GridData(SWT.CENTER | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
		data.verticalIndent = 10;
		data.horizontalIndent = -2;
		data.horizontalSpan = 1;
		//final Slider Svolume = new Slider(s, SWT.VERTICAL);
		Svolume = new Scale(s, SWT.VERTICAL);
		Svolume.setMaximum(10);
		Svolume.setMinimum(0);
		Svolume.setIncrement(1);
		Svolume.setPageIncrement(2);
		Svolume.setToolTipText("Adjust the volume");
		//Svolume.setSelection(Svolume.getMaximum() - 10 + Svolume.getMinimum() - Svolume.getThumb());
		Svolume.setSelection(Svolume.getMaximum() - 10 + Svolume.getMinimum() );
		guiControl.volume(10);
		Svolume.setLayoutData(data);



		// Right Slider
		data = new GridData(SWT.CENTER /*| GridData.HORIZONTAL_ALIGN_CENTER*/ | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
		data.horizontalIndent = 17;
		data.verticalIndent = 10;
		data.horizontalSpan = 1;
		//final Slider Sspeed = new Slider(s, SWT.VERTICAL);
		Sspeed = new Scale(s, SWT.VERTICAL);
		Sspeed.setMaximum(40);
		Sspeed.setMinimum(1);
		Sspeed.setIncrement(1);
		Sspeed.setPageIncrement(16);
		//Sspeed.setThumb(3);
		Sspeed.setToolTipText("Adjust the reading speed");
		//Sspeed.setSelection(Sspeed.getMaximum() - 15 + Sspeed.getMinimum() - Sspeed.getThumb());
		Sspeed.setSelection(Sspeed.getMaximum() - 15 + Sspeed.getMinimum());
		guiControl.speed(150);
		Sspeed.setLayoutData(data);

		// Left Value
		data = new GridData(SWT.CENTER | GridData.HORIZONTAL_ALIGN_CENTER );
		data.horizontalSpan = 1;
		//data.verticalAlignment = SWT.BEGINNING;
		final Text volumeValue = new Text(s, SWT.BORDER );
		volumeValue.setEditable(false);
		int tempVolume = Svolume.getMaximum() - Svolume.getSelection() + Svolume.getMinimum() ;//- Svolume.getThumb();
		volumeValue.setText(""+ tempVolume);
		volumeValue.setLayoutData(data);

		// Right Value
		data = new GridData(GridData.CENTER | GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 1;
		final Text speedValue = new Text(s, SWT.BORDER | SWT.SINGLE);
		speedValue.setEditable(false);
		int tempSpeed = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum() ;// - Sspeed.getThumb();
		speedValue.setText(""+tempSpeed);
		speedValue.setLayoutData(data);

		// get the user customization (window color, font, voice...)
		// needs to be done before the toolbar
		getUserProperties();

		// Display the Menu toolbar
		toolbar = new CToolbar(s,d, textArea, volumeLabel, speedLabel, editLabel, voices, Svolume, Sspeed);

		// link between the the GuiMain and GuiCommand classes
		guiControl.setGUIMain(this);
		textArea.setFocus();

		
//======= Listeners =======================================================
		// Listener Back Paragraph
		BbackParagraph.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				guiControl.backParagraph();
				highlight(guiControl);
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		}); 
		
		// Listener back sentence
		Bback.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				guiControl.backSentence();
				highlight(guiControl);
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		// Listener next sentence
		Bnext.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				guiControl.nextSentence();
				highlight(guiControl);
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		// Listener next paragraph
		BnextParagraph.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				guiControl.nextParagraph();
				highlight(guiControl);
				textArea.setFocus();
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
						isPaused=false;
						textArea.setEditable(false);	
					}
					else
					{
						Bplay.setImage(Iplay);
						isPaused = true;
						textArea.setEditable(false);
					}
					highlight(guiControl);
					textArea.setFocus();
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		
		

		// Listener Stop button
		Bstop.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				isPlaying = guiControl.stop();
				editLabel.setImage(Iedit);
				editLabel.setToolTipText("You can edit the text :)");
				Bplay.setImage(Iplay);
				isPaused = false;
				textArea.setEditable(true);
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		// listener clear button
		Bclear.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				textArea.setText("");
				updateGUIControl(guiControl);
				isPlaying = guiControl.stop();
				editLabel.setImage(Iedit);
				editLabel.setToolTipText("You can edit the text :)");
				Bplay.setImage(Iplay);
				isPaused = false;
				textArea.setEditable(true);
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		// Listener mp3 button
		Bmp3.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				updateGUIControl(guiControl);
				guiControl.mp3();
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		// Listener Tip button
		Btip.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				
				Shell shellWin = new Shell(d);
				shellWin.setImage(WindowIcon); //sets the uper left corner icon (in the window bar)

				GridLayout tipLayout = new GridLayout();
				tipLayout.numColumns = 2;
				tipLayout.makeColumnsEqualWidth = false;
				tipLayout.horizontalSpacing = 15;
				tipLayout.verticalSpacing = 5;

				shellWin.setLayout(tipLayout);
				shellWin.setSize(380, 700);
				shellWin.setMinimumSize(380,700);
				shellWin.setText("Read2Me! - Tips");

				GridData data = new GridData(SWT.CENTER);
				Label tip = new Label(shellWin, SWT.PUSH);
				tip.setText("Read2Me! is\n\n\n");
				tip.setLayoutData(data);
				Label tip2 = new Label(shellWin,SWT.BEGINNING);
				tip2.setText("as simple as paste text and hit the play button! Enjoy\n\n\n");
				
				
				data = new GridData(SWT.CENTER);
				Label backParagraph = new Label(shellWin, SWT.PUSH);
				backParagraph.setImage(IbackParagraph);
				backParagraph.setLayoutData(data);
				backParagraph.setToolTipText("Go back one paragraph \n(UP ARROW)");
				Label labBackParag = new Label(shellWin,SWT.BEGINNING);
				labBackParag.setText("Go back one paragraph \n(UP ARROW)");

				data = new GridData(SWT.CENTER);
				Label back = new Label(shellWin, SWT.PUSH);
				back.setImage(Iback);
				back.setLayoutData(data);
				back.setToolTipText("Go back one sentence \n(LEFT ARROW)");
				Label labBack = new Label(shellWin,SWT.BEGINNING);
				labBack.setText("Go back one sentence \n(LEFT ARROW)");

				data = new GridData(SWT.CENTER);
				Label next = new Label(shellWin, SWT.PUSH);
				next.setImage(Inext);
				next.setLayoutData(data);
				next.setToolTipText("Jump to next sentence \n(RIGHT ARROW)");
				Label labNext = new Label(shellWin,SWT.BEGINNING);
				labNext.setText("Jump to next sentence \n(RIGHT ARROW)");

				data = new GridData(SWT.CENTER);
				Label nextP = new Label(shellWin, SWT.PUSH);
				nextP.setImage(InextParagraph);
				nextP.setLayoutData(data);
				nextP.setToolTipText("Jump to next paragraph \n(DOWN ARROW)");
				Label labNextP = new Label(shellWin,SWT.BEGINNING);
				labNextP.setText("Jump to next paragraph \n(DOWN ARROW)");

				data = new GridData(SWT.CENTER);
				Label play = new Label(shellWin, SWT.PUSH);
				play.setImage(Iplay);
				play.setLayoutData(data);
				play.setToolTipText("Read the text \n(SPACE BAR)");
				Label labPlay = new Label(shellWin,SWT.BEGINNING);
				labPlay.setText("Read the text \n(SPACE BAR)");

				data = new GridData(SWT.CENTER);
				Label pause = new Label(shellWin, SWT.PUSH);
				pause.setImage(Ipause);
				pause.setLayoutData(data);
				pause.setToolTipText("Pause the reading \n(SPACE BAR)");
				Label labPause = new Label(shellWin,SWT.BEGINNING);
				labPause.setText("Pause the reading \n(SPACE BAR)");

				data = new GridData(SWT.CENTER);
				Label stop = new Label(shellWin, SWT.PUSH);
				stop.setImage(Istop);
				stop.setLayoutData(data);
				stop.setToolTipText("Stop reading and go back to the top of the text \n(RETURN KEY)");
				Label labStop = new Label(shellWin,SWT.BEGINNING);
				labStop.setText("Stop reading and go back to the top of the text \n(RETURN KEY)");

				data = new GridData(SWT.CENTER);
				Label edit = new Label(shellWin, SWT.BEGINNING);
				edit.setImage(Iedit);
				edit.setLayoutData(data);
				edit.setToolTipText("Edit status ON");
				Label labEdit = new Label(shellWin,SWT.BEGINNING);
				labEdit.setText("Edit mode is ON. You can edit the text");

				data = new GridData(SWT.CENTER);
				Label editX = new Label(shellWin, SWT.BEGINNING);
				editX.setImage(Ixedit);
				editX.setLayoutData(data);
				editX.setToolTipText("Edit status OFF");
				Label labEditX = new Label(shellWin,SWT.BEGINNING);
				labEditX.setText("Edit mode is OFF. Press STOP to switch to edit mode.");
				
				data = new GridData(SWT.CENTER);
				Label clear = new Label(shellWin, SWT.PUSH);
				clear.setImage(Iclear);
				clear.setLayoutData(data);
				clear.setToolTipText("Clear Button");
				Label labClear = new Label(shellWin,SWT.BEGINNING);
				labClear.setText("Clear the text area");

				data = new GridData(SWT.CENTER);
				Label export = new Label(shellWin, SWT.PUSH);
				export.setImage(Imp3);
				export.setLayoutData(data);
				export.setToolTipText("Export Button");
				Label labExport = new Label(shellWin,SWT.BEGINNING);
				labExport.setText("Export the current text to an audio file \nTakes effect when you hit the next time you STOP");

				data = new GridData(SWT.CENTER);
				Label vol = new Label(shellWin, SWT.BEGINNING);
				vol.setImage(Ivolume);
				vol.setLayoutData(data);
				vol.setToolTipText("Volume adjustment");
				Label labVol = new Label(shellWin,SWT.BEGINNING);
				labVol.setText("Adjust the volume of the voice with the scale \nTakes effect the next time you hit STOP");

				data = new GridData(SWT.CENTER);
				Label speed = new Label(shellWin, SWT.BEGINNING);
				speed.setImage(Ispeed);
				speed.setLayoutData(data);
				speed.setToolTipText("Speed adjustment");
				Label labSpeed = new Label(shellWin,SWT.BEGINNING);
				labSpeed.setText("Adjust the reading speed with the scale");
				
				data = new GridData(SWT.CENTER);
				Label adHelp = new Label(shellWin, SWT.PUSH);
				adHelp.setText("\n\n\nFor advanced");
				adHelp.setLayoutData(data);
				Label adHelp2 = new Label(shellWin,SWT.BEGINNING);
				adHelp2.setText("\n\n\nhelp or more info, see the menu Help -> F.A.Q");
				
				shellWin.open();
				textArea.setFocus();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		// Listener Volume slider
		Svolume.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int VValue = Svolume.getMaximum() - Svolume.getSelection() + Svolume.getMinimum() ;// - Svolume.getThumb();
				volumeValue.setText("" +VValue);
				guiControl.volume(VValue);
				textArea.setFocus();
			}
		});
		
		// Listener Speed Slider
		Sspeed.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int SValue = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum();// - Sspeed.getThumb();
				speedValue.setText("" +SValue);
				guiControl.speed(SValue*10);
				textArea.setFocus();
			}
		});
		
		// Listener that allows the user to use shortcuts
		textArea.addKeyListener(new KeyListener() {
			
			public void keyPressed(KeyEvent re)
			{
				if(isPlaying == true || isPaused == true)
				{
					System.out.println(re.character);
					System.out.println(re.keyCode);
					// Next sentence with right arrow
					if(re.keyCode == 16777220)
					{
						updateGUIControl(guiControl);
						guiControl.nextSentence();
						highlight(guiControl);
					}
					// Back Sentence with left arrow
					else if(re.keyCode == 16777219)
					{
						updateGUIControl(guiControl);
						guiControl.backSentence();
						highlight(guiControl);
					}
					// Back Paragraph with up arrow
					else if(re.keyCode == 16777217)
					{
						updateGUIControl(guiControl);
						guiControl.backParagraph();
						highlight(guiControl);
					}
					// next paragraph with down arrow
					else if(re.keyCode == 16777218)
					{
						updateGUIControl(guiControl);
						guiControl.nextParagraph();
						highlight(guiControl);
					}
					// play / pause with space bar
					else if(re.keyCode == 32)
					{
						if(textArea.getText().length() != 0)
						{
							updateGUIControl(guiControl);
							editLabel.setImage(Ixedit);
							editLabel.setToolTipText("You can't edit - Press stop to edit");
							isPlaying = guiControl.play(isPlaying);
							if(isPlaying == true)
							{
								Bplay.setImage(Ipause);
								isPaused=false;
								textArea.setEditable(false);
							}
							else
							{
								Bplay.setImage(Iplay);
								isPaused=true;
								textArea.setEditable(false);
							}
							highlight(guiControl);
							//textArea.setFocus();
						}
					}
					else if(re.keyCode == 13)
					{
						updateGUIControl(guiControl);
						isPlaying = guiControl.stop();
						editLabel.setImage(Iedit);
						editLabel.setToolTipText("You can edit the text :)");
						Bplay.setImage(Iplay);
						isPaused = false;
						textArea.setEditable(true);
					}
					
				}
			}
			
			public void keyReleased(KeyEvent re)
			{
				
			}
		});

		// Sets the Read2me! icon in the window title
		s.setImage(WindowIcon);
		s.open();
		
		while(!s.isDisposed()){
			/* Those functions get called when the Gui is awake
			 * it checks if the the gui needs to highlight new sentences
			 * or if it needs to set up the GUI in stop mod
			 * 
			 * Those functions calls the GuiCommand class
			 */
			
			// if we need to highlight another sentence
			if(guiControl.getNeedUpdate())
			{
				highlight(guiControl);
				guiControl.setNeedUpdate();
			}
			
			// If we need to be in stop mod
			if(guiControl.getNeedToStop())
			{
				isPlaying = false;
				Bplay.setImage(Iplay);
				editLabel.setImage(Iedit);
				editLabel.setToolTipText("You can edit the text :)");
				textArea.setEditable(true);
				guiControl.setNeedToStop();
			}
			
			// sleep if there is noting to do
			if(!d.readAndDispatch())
				d.sleep();
		}

		// when we close the program, we first stop the player
		guiControl.stop();

		// and we exit the program
		d.dispose();
		System.exit(0);
	}

	/**
	 * Loads the myWords.txt files each time we press play in order to replace all the abbreviations
	 * that the user might have typed in the text area.
	 * the format in the file is: word_to_replace = new_word
	 * @param _t the text in the text area that is going to be read
	 * @return the text modified
	 */
	private static String modifyText(String _t)
	{
		try
		{
			Scanner inFile=new Scanner(new File("resources/myWords.txt"));
			String line;
			int pos = -1;
			while(inFile.hasNextLine())
			{
				line=inFile.nextLine();
				pos = line.indexOf('=');
				if(!(pos == 0 || pos == -1 || pos == line.length()-1 || line.charAt(0) == '#'))
				{
					words.addElement(line.substring(0, pos).trim());
					words.addElement(line.substring(pos+1, line.length()).trim());
				}
			}
			inFile.close();

			for(int i=0; i<words.size(); i+=2)
			{
				_t = _t.replace(words.elementAt(i),words.elementAt(i+1));
			}
		}
		catch(Exception e)
		{
			System.out.println("file: myWord.txt wasn't found");
			return _t;
		}
		return _t;
	}

	/**
	 * Set parameters to the GuiCommand class:
	 * caret position, text, voice
	 * @param guiControl instance to the guiCommand class
	 */
	private static void updateGUIControl(final CGUICommandInterface guiControl)
	{
		int t = textArea.getCaretOffset();
		String text = modifyText(textArea.getText());
		textArea.setText(text);
		guiControl.setText(text);
		guiControl.setPosition(t);
		guiControl.setVoiceIndex(toolbar.getIndexVoice());
	}

	/**
	 * Highlight a sentence in the text Area knowing the beginning position and the end position
	 * and update the caret position to the begining of that sentence
	 * @param guiControl 
	 */
	private static void highlight(final CGUICommandInterface guiControl)
	{
		textArea.setCaretOffset(guiControl.getSentence()[0]);
		textArea.setSelection(guiControl.getSentence()[0],guiControl.getSentence()[1]);
	}

	/**
	 * Wake up the display to make it update the widgets
	 * This function is called when an event has occured (button pressed, end of sentence reached)
	 */
	public void updateDisplay()
	{
		d.wake();
	}

	/** 
	 * Read the properties defined previously by the user and apply them to the GUI
	 * Background color, font.
	 */
	private void getUserProperties(){
		prop = new Properties();
		//Read properties file.
		try {
			prop.load(new FileInputStream("user.prop"));
			s.setBackground(new Color(d, new RGB(  Integer.parseInt(prop.getProperty("windowsColorR")) , Integer.parseInt(prop.getProperty("windowsColorG")) , Integer.parseInt(prop.getProperty("windowsColorB")) )));
			volumeLabel.setBackground(s.getBackground());
			speedLabel.setBackground(s.getBackground());
			editLabel.setBackground(s.getBackground());
			Svolume.setBackground(s.getBackground());
			Sspeed.setBackground(s.getBackground());
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
			System.out.println("no user.prop found");
		}
	}
}