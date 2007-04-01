package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.program.Program;

public class CToolbar {

	CToolbar(final Shell s, final Display d)
	{
		GridData data = new GridData(GridData.CENTER | GridData.HORIZONTAL_ALIGN_END);
		data.verticalIndent = -20;
		final ToolBar bar = new ToolBar(s,SWT.HORIZONTAL);
		bar.setSize(300,10);
		bar.setLocation(0,0);
		bar.setLayoutData(data);

		// create the menu
		Menu m = new Menu(s,SWT.BAR);

		// create a file menu and add an exit item
		final MenuItem file = new MenuItem(m, SWT.CASCADE);
		file.setText("&File");
		final Menu filemenu = new Menu(s, SWT.DROP_DOWN);
		file.setMenu(filemenu);
		final MenuItem openMenuItem = new MenuItem(filemenu, SWT.PUSH);
		openMenuItem.setText("&Open\tCTRL+O");
		openMenuItem.setAccelerator(SWT.CTRL+'O');
		final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
		final MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
		exitMenuItem.setText("E&xit");

		// create an edit menu and add cut copy and paste items
		final MenuItem edit = new MenuItem(m, SWT.CASCADE);
		edit.setText("&Edit");
		final Menu editmenu = new Menu(s, SWT.DROP_DOWN);
		edit.setMenu(editmenu);
		final MenuItem cutMenuItem = new MenuItem(editmenu, SWT.PUSH);
		cutMenuItem.setText("&Cut");
		final MenuItem copyMenuItem = new MenuItem(editmenu, SWT.PUSH);
		copyMenuItem.setText("Co&py");
		final MenuItem pasteMenuItem = new MenuItem(editmenu, SWT.PUSH);
		pasteMenuItem.setText("&Paste");            

		//create a Voices menu and add Child item
		final MenuItem voices = new MenuItem(m, SWT.CASCADE);
		voices.setText("&Voices");
		final Menu voicesmenu = new Menu(s, SWT.DROP_DOWN);
		voices.setMenu(voicesmenu);
		final MenuItem Voice1MenuItem = new MenuItem(voicesmenu, SWT.PUSH);
		Voice1MenuItem.setText("Voice 1");
		final MenuItem Voice2MenuItem = new MenuItem(voicesmenu, SWT.PUSH);
		Voice2MenuItem.setText("Voice 2");

//		create a Settings menu and add Child item
		final MenuItem settings = new MenuItem(m, SWT.CASCADE);
		settings.setText("&Settings");
		final Menu settingsmenu = new Menu(s, SWT.DROP_DOWN);
		settings.setMenu(settingsmenu);
		final MenuItem MP3MenuItem = new MenuItem(settingsmenu, SWT.PUSH);
		MP3MenuItem.setText("MP3 Lame converter");
		final MenuItem preferencesMenuItem = new MenuItem(settingsmenu, SWT.PUSH);
		preferencesMenuItem.setText("Preferences...");

		// create a Help menu and add an about item
		final MenuItem help = new MenuItem(m, SWT.CASCADE);
		help.setText("&Help");
		final Menu helpmenu = new Menu(s, SWT.DROP_DOWN);
		help.setMenu(helpmenu);
		final MenuItem tipsMenuItem = new MenuItem(helpmenu, SWT.PUSH);
		tipsMenuItem.setText("&Tips");
		final MenuItem faqMenuItem = new MenuItem(helpmenu, SWT.PUSH);
		faqMenuItem.setText("&F.A.Q.");
		final MenuItem aboutMenuItem = new MenuItem(helpmenu, SWT.PUSH);
		aboutMenuItem.setText("&About");



		// add action listeners for the menu items

		openMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Open");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		preferencesMenuItem.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				final Shell prefWin = new Shell(s,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
				//prefWin.setLayout(new FillLayout());
				prefWin.setSize(300, 300);

				final Button cancelPrefButton = new Button(prefWin, SWT.PUSH);
				cancelPrefButton.setText("Cancel");
				cancelPrefButton.setBounds(80, 220, 50, 30);

				final Button savePrefButton = new Button(prefWin, SWT.PUSH);
				savePrefButton.setText("Save");
				savePrefButton.setBounds(130, 220, 50, 30);

				prefWin.open();

				cancelPrefButton.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent e) {
						prefWin.close();
					}
					public void widgetDefaultSelected(SelectionEvent e) {                
					}
				});
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		exitMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		cutMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Cut");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		copyMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Copy");
			}
			public void widgetDefaultSelected(SelectionEvent e) {               
			}
		});

		pasteMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Paste");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		Voice1MenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Voice 1 selected");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		Voice2MenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Voice 2 selected");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});

		aboutMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Help Invoked");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});            

		tipsMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				//textArea.setText("display an html page with the help");
				//Display display = new Display();

				//Browser browser = new Browser(shell,SWT.NONE);
				//browser.setUrl("http://unc.edu/~rjean");
				//browser.setUrl("\tips.html");
				//CHTML test = new CHTML();
				//browser.setText(test.getHTML());
				Program.launch(".\\images\\tips.html");
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		faqMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("F.A.Q. Invoked");
				Program.launch(".\\images\\tips.html");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});            

		s.setMenuBar(m);
	}        


}