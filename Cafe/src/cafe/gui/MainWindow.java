package cafe.gui;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import cafe.audio.DrawWaveform;

public class MainWindow {

	protected Shell shell;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		
		shell = new Shell();
		shell.setLocation(400, 200);
		shell.setSize(800, 600);
		shell.setText("CAFE Application");
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("&File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("&Open");
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        FileDialog fd = new FileDialog(shell, SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath("C:/");
		        String[] filterExt = { "*.wav", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String fileName = fd.open();
		        System.out.println(fileName);
		        if (fileName != null) {

		    		Label label = new Label(shell,SWT.NORMAL);
		    		int width = shell.getSize().x;
		    		int height = shell.getSize().y;
		    		label.setSize(width,height);
		    		label.setLocation(0, 0);
		    		DrawWaveform dw = new DrawWaveform(width,height);
		    		dw.draw(label.getDisplay(),fileName);
		    		label.setImage(dw.getImage());		    		
		        }
			}
		});

		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.setText("E&xit");
		
		MenuItem mntmAbout = new MenuItem(menu, SWT.NONE);
		mntmAbout.setText("&About");
		mntmAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText("About");
				dialog.setSize(440,170);
		        
				// Move the dialog to the center of the top level shell.
		        Rectangle shellBounds = shell.getBounds();
		        Point dialogSize = dialog.getSize();

		        dialog.setLocation(
		          shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
		          shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
		        
				Label label = new Label(dialog,SWT.NORMAL);
				label.setSize(350,40);
				label.setLocation(50, 50);
/*
 *  ugly
 * 				
				FontData[] fd = label.getFont().getFontData();
				fd[0].setHeight(16);			
				label.setFont(new Font(label.getDisplay(), fd[0]));
*/
				FontDescriptor fontDescriptor = FontDescriptor.createFrom(label.getFont()).setHeight(16);
				Font font16 = fontDescriptor.createFont(label.getDisplay());
				label.setFont(font16);
				label.setText("Computer Aided Feature Extraction");
					
				dialog.open();
				
				
			}
		});
		
		
		
		final Canvas canvas = new Canvas(shell,SWT.NONE);
	    canvas.addPaintListener(new PaintListener() {
	        public void paintControl(PaintEvent e) {
	            Rectangle clientArea = canvas.getClientArea();
	            e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
	            e.gc.fillOval(0,0,clientArea.width,clientArea.height); 
	        }
	    });

	}
}
