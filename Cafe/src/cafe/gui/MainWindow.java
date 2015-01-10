package cafe.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Button;
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

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import cafe.application.ComputeBasicFrequency;
import cafe.application.ComputePhaseSpectrum;
import cafe.audio.DrawWaveform;
import cafe.audio.WavFile;

public class MainWindow {
	private TargetDataLine line;
	File tempFile;
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
// gobbles with WindowDesigner		
//		shell = new Shell();
//		drawingLabel=new Label(shell, SWT.NORMAL);
		
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

// needed here		
 		shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		drawingLabel=new Label(shell, SWT.NORMAL);
		 		
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
					ComputePhaseSpectrum cph = new ComputePhaseSpectrum(finish-start,samplesPerSec);

					System.out.println("	- copy " + Integer.toString(finish-start) +" bytes");
					for (int i=0; i<start; i++) 
						framesRead = wavFile.readFrames(buffer,1);
					for (int i=0; i<finish-start; i++) {
						framesRead = wavFile.readFrames(buffer,1);
						cbf.copy(buffer[0]);
						cph.copy(buffer[0]);
					}
					wavFile.close();

					cbf.transform();
// time consuming
//					cph.transform();
										
					ChartResultWindow resultWindow = new ChartResultWindow("Chart");
					resultWindow.open(cbf.getBasicFrequencyTable(),"Basic Frequency", "t", "f [Hz]" );
					
					ChartResultWindow resultWindow1 = new ChartResultWindow("Chart");
					resultWindow1.open(cbf.getBasicFrequencyAmplitudeTable(),"Amplitude", "t","A" );

//					ChartResultWindow resultWindow2 = new ChartResultWindow("Chart");
//					resultWindow2.open(cph.getPhaseTable(),"Phase Spectrum", "f","Theta [rad]" );
					
				} catch (Exception exc) {
					System.err.println(exc);
					exc.printStackTrace();
				}				
			}
		}
		});
		mntmCompute.setText("Compute ");
		MenuItem mntmMusic = new MenuItem(menu, SWT.CASCADE);
		mntmMusic.setText("&Sound");
		Menu menu_2 = new Menu(mntmMusic);
		mntmMusic.setMenu(menu_2);
		MenuItem mntmPlay = new MenuItem(menu_2, SWT.NONE);
		mntmPlay.setText("&Play");
		mntmPlay.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try{
				if(!fileName.isEmpty()){
					Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
			    clip.start();
				}
				}catch(Exception e1){
					System.err.println(e1);
					e1.printStackTrace();
				}
			}
		});
		MenuItem mntmRecord = new MenuItem(menu_2, SWT.NONE);
		mntmRecord.setText("&Record");
		mntmRecord.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText("Recording");
				dialog.setSize(440,170);
		        Rectangle shellBounds = shell.getBounds();
		        Point dialogSize = dialog.getSize();
		        dialog.setLocation(
				          shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
				          shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
			final Button btnRecord=new Button(dialog, SWT.NORMAL);
			final Button btnStop=new Button(dialog, SWT.NORMAL);
			final Button btnSave=new Button(dialog, SWT.NORMAL);
			final Button btnPlay=new Button(dialog, SWT.NORMAL);
			btnRecord.setText("Record");
			btnRecord.setSize(80,25);
			btnRecord.setLocation(20,50);
			btnRecord.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e){
					btnRecord.setEnabled(false);
					btnStop.setEnabled(true);
					btnSave.setEnabled(false);
					btnPlay.setEnabled(false);
					try {
						tempFile = File.createTempFile("temp-file-name", ".wav");
						System.out.println(tempFile);
						fileName=tempFile.toString();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if(tempFile!=null){
						class Recorder implements Runnable {

						    public void run() {
		            try {
						AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
						AudioFormat format =new AudioFormat(44100,16,1,true,true);
						DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
						if (!AudioSystem.isLineSupported(info)) {
			                System.out.println("Line not supported");
			                return;
						}

			            	line = (TargetDataLine) AudioSystem.getLine(info);
						line.open(format);
			            line.start();
			            AudioInputStream ais = new AudioInputStream(line);
			          // not allways trash!  ais.skip(90112); //skipping 1 sec trash = 88KB
			            AudioSystem.write(ais, fileType, tempFile);
			            } catch (LineUnavailableException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							    }
						};
						(new Thread(new Recorder())).start();
					}
				}
			});
			
			btnStop.setText("Stop");
			btnStop.setSize(80,25);
			btnStop.setLocation(120,50);
			btnStop.setEnabled(false);
			btnStop.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e){
					btnRecord.setEnabled(true);
					btnStop.setEnabled(false);
					btnSave.setEnabled(true);
					btnPlay.setEnabled(true);
					line.stop();
					line.close();
					//mntmOpen.notifyListeners(SWT.Selection, new Event());

		    		int width = shell.getSize().x;
		    		int height = shell.getSize().y-MENU_HEIGHT;				    				    		
		    		drawingLabel.setSize(width,height);
		    		drawingLabel.setLocation(0, 0);
		    		dw = new DrawWaveform(width,height,drawingLabel.getDisplay(),fileName);
		    		dw.draw();
		    		drawingLabel.setImage(dw.getImage());
				}
			});
			
			btnSave.setText("Save");
			btnSave.setSize(80,25);
			btnSave.setLocation(220,50);
			btnSave.setEnabled(false);
			btnSave.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e){
			        FileDialog fd = new FileDialog(shell, SWT.SAVE);
			        fd.setText("Save");
			        fd.setFilterPath("C:/");
			        String[] filterExt = { "*.wav", "*.*" };
			        fd.setFilterExtensions(filterExt);
			        fileName = fd.open();
			   
			        System.out.println(fileName);
			        if (fileName != null) {
			        	FileInputStream fr=null;
			        	FileOutputStream fw=null;
			        	try {
							fr=new FileInputStream(tempFile);
				
						   	fw=new FileOutputStream(fileName,false);
						   	byte[] buffer = new byte[8 * 1024];
						   	int bytesRead;
						    while ((bytesRead = fr.read(buffer)) != -1){				    
						      fw.write(buffer,0,bytesRead);
						    }
						    fr.close();
						    fw.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			     
			        	
				        
				        dialog.close();
			        }
				}
			});
			btnPlay.setText("Play");
			btnPlay.setSize(80,25);
			btnPlay.setLocation(320,50);
			btnPlay.setEnabled(false);
			btnPlay.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e){
					try{
						if(!fileName.isEmpty()){
							Clip clip = AudioSystem.getClip();
						clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
					    clip.start();
						}
						}catch(Exception e1){
							System.err.println(e1);
							e1.printStackTrace();
						}
				}
			});
		    dialog.open();
			}
		});
		
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
