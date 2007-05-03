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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.program.Program;

public class CToolbar {

	private static CPreferenceWindow pref;
	
	/**
	 * @return the index of the selected voice
	 */
	int getIndexVoice()
	{
		return pref.selected;
	}

	/**
	 * creates the menu toolbar
	 * @param s the shell
	 * @param d the display
	 * @param textArea the textarea
	 * @param volumeLabel
	 * @param speedLabel
	 * @param editLabel
	 * @param voices
	 * @param Svolume the volume scale
	 * @param Sspeed the reading speed scale
	 */
	CToolbar(final Shell s, final Display d, final StyledText textArea, final Label volumeLabel, final Label speedLabel, final Label editLabel, final String[] voices, final Scale Svolume, final Scale Sspeed)
	{
		pref = new CPreferenceWindow();
		
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
		final MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
		exitMenuItem.setText("E&xit\tCTRL+Q");
		exitMenuItem.setAccelerator(SWT.CTRL + 'Q');
		

		// create an edit menu and add cut copy and paste items
		/*final MenuItem edit = new MenuItem(m, SWT.CASCADE);
		edit.setText("&Edit");
		final Menu editmenu = new Menu(s, SWT.DROP_DOWN);
		edit.setMenu(editmenu);
		final MenuItem cutMenuItem = new MenuItem(editmenu, SWT.PUSH);
		cutMenuItem.setText("&Cut");
		final MenuItem copyMenuItem = new MenuItem(editmenu, SWT.PUSH);
		copyMenuItem.setText("Co&py");
		final MenuItem pasteMenuItem = new MenuItem(editmenu, SWT.PUSH);
		pasteMenuItem.setText("&Paste");     */       

//		create a Settings menu and add Child item
		final MenuItem preferencesMenuItem = new MenuItem(m, SWT.CASCADE);
		preferencesMenuItem.setText("&Preferences");
		preferencesMenuItem.setAccelerator(SWT.CTRL + 'P');
		/*final Menu settingsmenu = new Menu(s, SWT.DROP_DOWN);
		settings.setMenu(settingsmenu);
		final MenuItem preferencesMenuItem = new MenuItem(settingsmenu, SWT.PUSH);
		preferencesMenuItem.setText("Preferences...");
*/
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
		
		final MenuItem separator = new MenuItem(m,SWT.SEPARATOR);
		separator.setText("---------");
		final MenuItem voice = new MenuItem(m, SWT.CASCADE | GridData.HORIZONTAL_ALIGN_END);
		voice.setText("Current voice: "+voices[pref.selected]);
		

		// add action listeners for the menu items
 
		voice.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				
				pref.display(s,d,textArea, volumeLabel, speedLabel, editLabel, voices, Svolume, Sspeed, voice);
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});
		
		preferencesMenuItem.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				
				pref.display(s,d,textArea, volumeLabel, speedLabel, editLabel, voices, Svolume, Sspeed, voice);
				
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
/*
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
		});*/


		aboutMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				final Shell aboutWin = new Shell(s,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
				aboutWin.setSize(366, 318);
				aboutWin.setText("About");
				final Image aboutScreen = new Image(d, "./resources/AboutScreen.jpg");
				
				aboutWin.addPaintListener(new PaintListener() {
				      public void paintControl(PaintEvent event) {
				        event.gc.drawImage(aboutScreen, 0, 0);
				      }
				});
				aboutWin.open();
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});            

		tipsMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				Program.launch(".\\resources\\tips.html");
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		faqMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Program.launch(".\\resources\\FAQ.html");
			}
			public void widgetDefaultSelected(SelectionEvent e) {                
			}
		});            

		s.setMenuBar(m);
	}        


}