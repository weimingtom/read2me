package guiMain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
   

public class CGUIMain {
	private static String playPause = "Play";
	private static Shell s;
	private static Display d;
    
	
	public static void main(String[] a){
    	d = new Display();
        s = new Shell(d);

        GridLayout layout = new GridLayout();
        layout.numColumns = 11;
        layout.makeColumnsEqualWidth = false;
        layout.horizontalSpacing = 0;
        layout.marginTop = 15;
        layout.marginLeft = 15;
        layout.marginRight = 15;
        
        s.setLayout(layout);
        s.setSize(575,450);

        //s.setBackground(d.getSystemColor(SWT.COLOR_BLUE));
        s.setMinimumSize(575, 450);
        s.setText("Read 2 Me!");
        //s.forceActive();
    	
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
       
        
        GridData data = new GridData(SWT.CENTER);
    	final Button BbackParagraph = new Button(s, SWT.PUSH);
    	BbackParagraph.setImage(IbackParagraph);
    	BbackParagraph.setLayoutData(data);
    	BbackParagraph.setToolTipText("One paragraph back");
    	
    	data = new GridData(SWT.CENTER);
    	Button Bback = new Button(s, SWT.PUSH);
    	Bback.setImage(Iback);
    	Bback.setLayoutData(data);
    	Bback.setToolTipText("One sentence back");
    	
    	data = new GridData(SWT.CENTER);
    	Button Bnext = new Button(s, SWT.PUSH);
    	Bnext.setImage(Inext);
    	Bnext.setLayoutData(data);
    	Bnext.setToolTipText("Next sentence");
    	
    	data = new GridData(SWT.CENTER);
    	Button BnextParagraph = new Button(s, SWT.PUSH);
    	BnextParagraph.setImage(InextParagraph);
    	BnextParagraph.setLayoutData(data);
    	BnextParagraph.setToolTipText("One paragraph further");
    	
    	Button BnotShown = new Button(s, SWT.PUSH);
    	BnotShown.setText("Not shown");
    	BnotShown.setVisible(false);
    	BnotShown.setLayoutData(data);
    	
    	data = new GridData(SWT.CENTER);
    	final Button Bplay = new Button(s, SWT.PUSH);
    	Bplay.setImage(Iplay);
    	Bplay.setLayoutData(data);
    	Bplay.setToolTipText("Play / Pause button");
    	
    	data = new GridData(SWT.CENTER);
    	Button Bstop = new Button(s, SWT.PUSH);
    	Bstop.setImage(Istop);
    	Bstop.setLayoutData(data);
    	Bstop.setToolTipText("Stop button");
    	
    	Button BnotShown2 = new Button(s, SWT.PUSH);
    	BnotShown2.setText("Not shown");
    	BnotShown2.setVisible(false);
    	BnotShown2.setLayoutData(data);
    	
    	data = new GridData(SWT.CENTER);
    	Button Bmp3 = new Button(s, SWT.PUSH);
    	Bmp3.setImage(Imp3);
    	Bmp3.setLayoutData(data);
    	Bmp3.setToolTipText("Convert to mp3");
    	
    	data = new GridData(SWT.CENTER);
    	Button BnotShown3 = new Button(s, SWT.PUSH);
    	BnotShown3.setText("Not shown");
    	BnotShown3.setVisible(false);
    	BnotShown3.setLayoutData(data);
    	
    	data = new GridData(SWT.CENTER);
    	Button Btip = new Button(s, SWT.PUSH);
    	Btip.setImage(Itip);
    	Btip.setLayoutData(data);
    	Btip.setToolTipText("Help");
    	
    	// end of first row
    	
    	
    	// Volume label
    	data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING);
    	data.verticalIndent = 20;
    	data.horizontalSpan = 2;
    	Label volumeLabel = new Label(s, SWT.BEGINNING);
    	volumeLabel.setImage(Ivolume);
    	volumeLabel.setLayoutData(data);
    	volumeLabel.setToolTipText("Volume");
	    
    	// text area
    	data = new GridData(GridData.FILL_BOTH);
    	data.verticalIndent = 40;
    	data.verticalSpan = 3;
    	data.horizontalSpan = 7;
    	data.grabExcessHorizontalSpace = true;
    	final StyledText textArea = new StyledText(s, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    	textArea.setLayoutData(data);
    	
    	// Speed Label
    	data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING);
    	data.horizontalIndent = 20;
    	data.verticalIndent = 20;
    	data.horizontalSpan = 2;
    	Label speedLabel = new Label(s, SWT.BEGINNING);
    	speedLabel.setImage(Ispeed);
    	speedLabel.setLayoutData(data);
    	speedLabel.setToolTipText("Reading speed");
    	
    	// Left Slider
    	data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
    	data.verticalIndent = 20;
    	data.horizontalSpan = 2;
    	final Slider Svolume = new Slider(s, SWT.VERTICAL);
    	Svolume.setMaximum(25);
    	Svolume.setMinimum(0);
    	Svolume.setIncrement(1);
    	Svolume.setPageIncrement(5);
    	Svolume.setThumb(3);  // dimension of the thing
    	Svolume.setToolTipText("Adjust the volume");
    	Svolume.setLayoutData(data);
	    
    	// Right Slider
	    data = new GridData(SWT.LEFT | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL);
    	data.horizontalIndent = 20;
    	data.verticalIndent = 20;
    	data.horizontalSpan = 2;
    	final Slider Sspeed = new Slider(s, SWT.VERTICAL);
    	Sspeed.setMaximum(25);
    	Sspeed.setMinimum(0);
    	Sspeed.setIncrement(1);
    	Sspeed.setPageIncrement(5);
    	Sspeed.setThumb(3);  // dimension of the thing
    	Sspeed.setToolTipText("Adjust the reading speed");
    	Sspeed.setLayoutData(data);

	    // Left Value
    	data = new GridData(GridData.CENTER);
    	data.horizontalSpan = 2;
    	final Text volumeValue = new Text(s, SWT.BORDER | SWT.SINGLE);
    	volumeValue.setEditable(false);
    	volumeValue.setLayoutData(data);
		
    	// Right Value
		data = new GridData(GridData.CENTER | GridData.HORIZONTAL_ALIGN_END);
    	data.horizontalSpan = 2;
    	final Text speedValue = new Text(s, SWT.BORDER | SWT.SINGLE);
    	speedValue.setEditable(false);
    	speedValue.setLayoutData(data);
    	
    	final CToolbar toolbar = new CToolbar(s,d);
    	
    	
    	// LISTENERS
    	
    	// Listener Back Paragraph
    	BbackParagraph.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("Back one paragraph");
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener back sentence
    	Bback.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("Back one sentence");
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener next sentence
    	Bnext.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("next sentence");
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener next paragraph
    	BnextParagraph.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("next paragraph");
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener Play/pause button
    	Bplay.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
              if(playPause.equalsIgnoreCase("play"))
              {
            	  Bplay.setImage(Ipause);
            	  playPause = "pause";
            	  textArea.setText("Currently playing");
              }  
              else
              {
            	  Bplay.setImage(Iplay);
            	  playPause = "play";
            	  textArea.setText("Currently in pause");
              }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
            }
          });
    	// Listener Stop button
    	Bstop.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("Stop playing");
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener mp3 button
    	Bmp3.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("Convert to mp3");
              }

              public void widgetDefaultSelected(SelectionEvent event) {
              }
    	});
    	// Listener Tip button
    	Btip.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent event) {
                textArea.setText("display an html page with the help");
                //Display display = new Display();
                Shell shell = new Shell(s,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
                shell.setLayout(new FillLayout());
                shell.setSize(500, 400);
                Browser browser = new Browser(shell,SWT.NONE);
                //browser.setUrl("http://unc.edu/~rjean");
                CHTML test = new CHTML();
                browser.setText(test.getHTML());
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
        	      }
        	    });
    	// Listener Speed Slider
    	Sspeed.addListener(SWT.Selection, new Listener() {
    	      public void handleEvent(Event event) {
        	        int SValue = Sspeed.getMaximum() - Sspeed.getSelection() + Sspeed.getMinimum() - Sspeed.getThumb();
        	        speedValue.setText("Speed: " +SValue);
        	      }
        	    });
    	
    	s.open();
        while(!s.isDisposed()){
            if(!d.readAndDispatch())
                d.sleep();
        }
        d.dispose();
    }
}
