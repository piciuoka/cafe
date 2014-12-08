package cafe.gui;

import java.io.File;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import cafe.application.ComputeBasicFrequency;
import cafe.audio.DrawWaveform;
import cafe.audio.WavFile;

public class MainWindow {

	protected Shell shell;
	protected Display display;
	private Image image;
	private String fileName;
	private Label drawingLabel;
	private DrawWaveform dw;
	private static int MENU_HEIGHT = 60;

	/**
	 * Open the window.
	 */

	public MainWindow(){
		shell = new Shell();
		drawingLabel=new Label(shell, SWT.NORMAL);
		
	}

	public void open() {
		display = Display.getDefault();
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

/* 
 * 	Drawing on canvas	
 		shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
*/ 		 		
		shell.setLocation(400, 200);
		shell.setSize(800, 600);
		shell.setText("CAFE Application");

        shell.addListener (SWT.Paint, new Listener () 
        {
            public void handleEvent (Event e) {
                GC gc = e.gc;
                if (image != null)
                	gc.drawImage(image, 0, 0);
                gc.dispose();
            }
        });
        
		shell.addListener(SWT.Resize, new Listener(){
			public void handleEvent (Event e) {
				if(dw != null){
					int width=shell.getSize().x;
					int height=shell.getSize().y-MENU_HEIGHT;
					System.out.println(width);
					drawingLabel.setSize(width,height);
					dw.redraw(width, height);
					drawingLabel.setLocation(0, 0);
					drawingLabel.setImage(dw.getImage());
				}
			}
		});
		
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
		        fileName = fd.open();
		        System.out.println(fileName);
		        if (fileName != null) {

		    		int width = shell.getSize().x;
		    		int height = shell.getSize().y-MENU_HEIGHT;
/* 
 * 	Drawing on canvas		    		
	    			DrawWaveform dw = new DrawWaveform(width,height,drawingLabel.getDisplay(),fileName);
//		    		DrawWaveform dw = new DrawWaveform(width,height);		    		
		    		dw.draw(display,fileName);
//		    		dw.draw();		    		
		    		image = dw.getImage();
		    		shell.redraw();
*/
    				    				    		
		    		drawingLabel.setSize(width,height);
		    		drawingLabel.setLocation(0, 0);
		    		dw = new DrawWaveform(width,height,drawingLabel.getDisplay(),fileName);
		    		dw.draw();
		    		drawingLabel.setImage(dw.getImage());
		        }
			}
		});

		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.setText("E&xit");
		mntmExit.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				System.exit(0);
			}
		});
		MenuItem mntmCompute = new MenuItem(menu, SWT.NONE);
		mntmCompute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			System.out.println("Compute:");
			if (fileName != null) {
				try {
					System.out.println("	- open: " + fileName);
					WavFile wavFile = WavFile.openWavFile(new File(fileName));
					int numChannels = wavFile.getNumChannels();
					int samplesPerSec = (int) wavFile.getSampleRate();
					System.out.println("	- file length: " + Long.toString(wavFile.getNumFrames()));
					
												
					int start = 0;
					int finish = (int) wavFile.getNumFrames();
				
					int[] buffer = new int[1 * numChannels];
					int framesRead;
//TODO OK					
//					start = 4000-2;
//					finish = 6000-2;

					ComputeBasicFrequency cbf = new ComputeBasicFrequency(finish-start,samplesPerSec);

					System.out.println("	- copy " + Integer.toString(finish-start) +" bytes");
					for (int i=0; i<start; i++) 
						framesRead = wavFile.readFrames(buffer,1);
					for (int i=0; i<finish-start; i++) {
						framesRead = wavFile.readFrames(buffer,1);
						cbf.copy(buffer[0]);
//						System.out.println(Integer.toString(i)+" "+Integer.toString(buffer[0]));
					}
					wavFile.close();

					cbf.transform();
					System.out.println("Result: ");
					
					ResultWindow resultWindow = new ResultWindow();
					resultWindow.open(cbf.getBasicFrequencyTable());
					
				} catch (Exception exc) {
					System.err.println(exc);
					exc.printStackTrace();
				}				
			}
		}
		});
		mntmCompute.setText("Compute ");
		
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

				FontDescriptor fontDescriptor = FontDescriptor.createFrom(label.getFont()).setHeight(12);
				Font font16 = fontDescriptor.createFont(label.getDisplay());
				label.setFont(font16);
				label.setText("Computer Aided Feature Extraction");
					
				dialog.open();				
			}
		});		
	}
}
