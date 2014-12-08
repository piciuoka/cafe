package cafe.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class ResultWindow {

	protected Shell shell;
	private Display display;
	private Image image;

	/**
	 * Open the window.
	 */
	public void open(double[] table) {
		display = Display.getDefault();
		createContents(table);
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
	 */
	protected void createContents(double[] table) {
		shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		shell.setSize(800, 600);
		shell.setText("Application");

        shell.addListener (SWT.Paint, new Listener () {
            public void handleEvent (Event e) {
                GC gc = e.gc;
                if (image != null)
                	gc.drawImage(image, 0, 0,image.getBounds().width,image.getBounds().height,
                						0, 0, shell.getSize().x, shell.getSize().y);
                gc.dispose();
            }            
        });

        shell.addListener(SWT.Resize, new Listener(){
			public void handleEvent (Event e) {
			    shell.redraw();
			}			
		});
        
	    draw(table);
       
	}

	public void draw(double[] table) {
		int width = shell.getSize().x;
		int height = shell.getSize().y - 60;
		image = new Image(display,width,height);
		GC gc = new GC(image);
		
		int length = table.length; // int length = table.length/2;
		double maxValue = getMaxValue(table);
		double dx=(float)width/(float)length;
		double dy=(float)height/(maxValue+10.0);
			
		for (int i=0; i<length-1;i++) {			
			gc.drawLine((int)(i*dx), height-(int)(table[i]*dy), (int)((i+1)*dx), height-(int)(table[i+1]*dy));
		}
		gc.dispose();
		shell.redraw();		
	}

	public double getMaxValue(double[] table) {
		double max = table[0];
		for (double d : table) 
			if (max<d) max=d;
		return max;
	}
	
	
}
