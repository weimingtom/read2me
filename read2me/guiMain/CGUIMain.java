package guiMain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import textToSpeech.*;
   

public class CGUIMain {
	private static boolean isPlaying = false;
	private static Shell s;
	private static Display d;
	private static StyledText textArea;
	

	// ------------------ Main ------------------------------
	public static void main(String[] a){
    	/*
		final CPlayerInterface player = new CPlayer();   
		player.createSynthesizers();
		player.setSynthesizer(1);
		*/
		final CGUICommandInterface guiControl = new CGUICommand();
				
		d = new Display();
        s = new Shell(d);

        GridLayout layout = new GridLayout();
        layout.numColumns = 11;
        layout.makeColumnsEqualWidth = false;
        layout.horizontalSpacing = 0;
        layout.marginTop = 5;
        layout.marginLeft = 15;
        layout.marginRight = 15;
        
        s.setLayout(layout);
        s.setSize(600,450);

        //s.setBackground(d.getSystemColor(SWT.COLOR_BLUE));
        s.setMinimumSize(600, 450);
        s.setText("Read 2 Me!");
    	
        final Image Iplay = new Image(d, "./Images/Play.png");
        final Image Ipause = new Image(d, "./Images/Pause.png");
        final Image Istop = new Image(d, "./Images/Stop.png");
        final Image Iback = new Image(d, "./Images/Back.png");
        final Image IbackParagraph = new Image(d, "./Images/BackParagraph.png");
        final Image Inext = new Image(d, "./Images/Next.png");
        final Image InextParagraph = new Image(d, "./Images/NextParagraph.png");
        final Image Ivolume = new Image(d, "./Images/Sound.png");
        final Image Ispeed = new Image(d, "./Images/speedFull.png");
        final Image Imp3 = new Image(d, "./Images/mp3.png");
        final Image Itip = new Image(d, "./Images/tip.png");
       
        // Back paragraph button
        GridData data = new GridData(SWT.CENTER);
    	final Button BbackParagraph = new Button(s, SWT.PUSH);
    	BbackParagraph.setImage(IbackParagraph);
    	BbackParagraph.setLayoutData(data);
    	BbackParagraph.setToolTipText("One paragraph back");
    	
    	// One sentence back Button
    	data = new GridData(SWT.CENTER);
    	Button Bback = new Button(s, SWT.PUSH);
    	Bback.setImage(Iback);
    	Bback.setLayoutData(data);
    	Bback.setToolTipText("One sentence back");
    	
    	// Next sentence Button
    	data = new GridData(SWT.CENTER);
    	Button Bnext = new Button(s, SWT.PUSH);
    	Bnext.setImage(Inext);
    	Bnext.setLayoutData(data);
    	Bnext.setToolTipText("Next sentence");
    	
    	// Next Paragraph Button
    	data = new GridData(SWT.CENTER);
    	Button BnextParagraph = new Button(s, SWT.PUSH);
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
    	final Button Bplay = new Button(s, SWT.PUSH);
    	Bplay.setImage(Iplay);
    	Bplay.setLayoutData(data);
    	Bplay.setToolTipText("Play / Pause button");
    	
    	// Stop Button
    	data = new GridData(SWT.CENTER);
    	Button Bstop = new Button(s, SWT.PUSH);
    	Bstop.setImage(Istop);
    	Bstop.setLayoutData(data);
    	Bstop.setToolTipText("Stop button");
    	
    	// Separator
    	Button BnotShown2 = new Button(s, SWT.PUSH);
    	BnotShown2.setText("Not shown");
    	BnotShown2.setVisible(false);
    	BnotShown2.setLayoutData(data);
    	
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
    	Label volumeLabel = new Label(s, SWT.BEGINNING);
    	volumeLabel.setImage(Ivolume);
    	volumeLabel.setLayoutData(data);
    	volumeLabel.setToolTipText("Volume");
	    
    	// text area
    	data = new GridData(GridData.FILL_BOTH);
    	data.verticalIndent = 40;
    	data.verticalSpan = 2;
    	data.horizontalSpan = 9;
    	data.grabExcessHorizontalSpace = true;
    	textArea = new StyledText(s, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    	textArea.setWordWrap(true);
    	textArea.setLayoutData(data);
    	
    	// Speed Label
    	data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
    	data.horizontalIndent = 20;
    	data.verticalIndent = 20;
    	data.horizontalSpan = 1;
    	Label speedLabel = new Label(s, SWT.BEGINNING);
    	speedLabel.setImage(Ispeed);
    	speedLabel.setLayoutData(data);
    	speedLabel.setToolTipText("Reading speed");
    	
    	// Left Slider
    	data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
    	data.verticalIndent = 20;
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
	    data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
    	data.horizontalIndent = 20;
    	data.verticalIndent = 20;
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
    	data = new GridData(GridData.CENTER);
    	data.horizontalSpan = 2;
    	data.verticalAlignment = SWT.BEGINNING;
    	final Text volumeValue = new Text(s, SWT.BORDER | SWT.SINGLE);
    	volumeValue.setEditable(false);
    	int tempVolume = Svolume.getMaximum() - Svolume.getSelection() + Svolume.getMinimum() - Svolume.getThumb();
    	volumeValue.setText("Vol: "+ tempVolume);
    	volumeValue.setLayoutData(data);
		
    	//not shown button
    	data = new GridData(SWT.CENTER);
    	data.horizontalSpan = 7;
    	Button BnotShown4 = new Button(s, SWT.PUSH);
    	BnotShown4.setText("Not shown");
    	BnotShown4.setVisible(false);
    	BnotShown4.setLayoutData(data);
    	
    	// Right Value
		data = new GridData(GridData.CENTER | GridData.HORIZONTAL_ALIGN_END);
    	data.horizontalSpan = 2;
    	final Text speedValue = new Text(s, SWT.BORDER | SWT.SINGLE);
    	speedValue.setEditable(false);
    	int tempSpeed = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum() - Sspeed.getThumb();
    	speedValue.setText("Speed: "+ tempSpeed);
    	speedValue.setLayoutData(data);
    	
    	final CToolbar toolbar = new CToolbar(s,d);
    	
    	// LISTENERS
    	
    	// Listener Back Paragraph
    	BbackParagraph.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                guiControl.setText(getText());
                guiControl.backParagraph();
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener back sentence
    	Bback.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                guiControl.setText(getText());
                guiControl.backSentence();
                
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener next sentence
    	Bnext.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                guiControl.setText(getText());
                guiControl.nextSentence();
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener next paragraph
    	BnextParagraph.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                guiControl.setText(getText());
                guiControl.nextParagraph();
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener Play/pause button
    	Bplay.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
	            
            	guiControl.setText(getText());
            	isPlaying = guiControl.play(isPlaying);
            	if(isPlaying == true)
            		Bplay.setImage(Ipause);
            	else
            		Bplay.setImage(Iplay);
            	/*
            	if(isPlaying == false) // player is in play mode
	            {
	            	String text = getText();
            		//System.out.println(text);
            		if(player.isPaused())
	            	{
	            		player.resume();
	            	}
	            	else
	            	{ // just to this part when we first play play
	            		CSpeechObject speech = CSpeechObject.createTextSpeech(text);
	            		player.addSpeech(speech);
	            		player.play(speech);
	            	}
	            	Bplay.setImage(Ipause);
	            	isPlaying = true;
	            }  
	            else  // player is in pause
	            {
	            	player.pause();
            		Bplay.setImage(Iplay);
	            	isPlaying = false;
	            }*/
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
          });
    	// Listener Stop button
    	Bstop.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                //textArea.setText("Stop playing");
                
                isPlaying = guiControl.stop();
                Bplay.setImage(Iplay);
            	//isPlaying = false;
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener mp3 button
    	Bmp3.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
    			guiControl.mp3();
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener Tip button
    	Btip.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                Shell shell = new Shell(d);
                shell.setLayout(new FillLayout());
                shell.setSize(500, 400);
                Browser browser = new Browser(shell,SWT.NONE);
                //browser.setUrl("http://unc.edu/~rjean");
                CHTML textHTML = new CHTML();
                browser.setText(textHTML.getHTML());
                shell.open();
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener Volume slider
    	Svolume.addListener(SWT.Selection, new Listener() {
    	      public void handleEvent(Event event) {
        	        int VValue = Svolume.getMaximum() - Svolume.getSelection() + Svolume.getMinimum() - Svolume.getThumb();
        	        volumeValue.setText("Vol: " +VValue);
        	        //player.setVolume(VValue);
        	        guiControl.volume(VValue);
        	      }
        	    });
    	// Listener Speed Slider
    	Sspeed.addListener(SWT.Selection, new Listener() {
    	      public void handleEvent(Event event) {
        	        int SValue = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum() - Sspeed.getThumb();
        	        speedValue.setText("Speed: " +SValue);
        	        //player.setSpeakingSpeed(SValue*10);
        	        guiControl.speed(SValue*10);
        	      }
        	    });
    	
    	s.open();
        while(!s.isDisposed()){
            if(!d.readAndDispatch())
                d.sleep();
        }
        //player.stop();
        guiControl.stop();
        d.dispose();
    }
	
	private static String getText()
	{
		return textArea.getText();
	}
	
}
