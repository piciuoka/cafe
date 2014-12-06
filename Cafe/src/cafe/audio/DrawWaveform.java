package cafe.audio;

import java.io.File;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class DrawWaveform {
	
	private Image image;
	private int width;
	private int height;
	private Device device;
	private String fileName;
	public DrawWaveform(int w, int h,Device dv,String fn) {
		width = w;
		height = h;
		device=dv;
		fileName=fn;
	}
		
	public void draw() {
		image = new Image(device,width,height);
		GC gc = new GC(image);
		Rectangle bounds = image.getBounds();
		
		try {
			WavFile wavFile = WavFile.openWavFile(new File(fileName));
			int numChannels = wavFile.getNumChannels();
			double[] buffer = new double[100 * numChannels];
			int framesRead;
			double x = 0;
			double dx = ((double) width / wavFile.getNumFrames());
			do {
				framesRead = wavFile.readFrames(buffer, 100);
				for (int s=0 ; s<(framesRead * numChannels)-1 ; s++)
				{
					gc.drawLine((int) x,(int)((1+buffer[s])*(height/2.0)),(int) x,(int)((1+buffer[s+1])*(height/2.0))); 	
					x += dx;
				}
			}
			while ((framesRead != 0) && (x<width));
			wavFile.close();
			gc.dispose();

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void redraw(int w,int h){
		width=w;
		height=h;
		draw();
	}
	public Image getImage() {
		return image;
	}

	
}
