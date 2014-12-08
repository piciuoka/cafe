package cafe.application;

import cafe.analysis.Analysis;

public class ComputeBasicFrequency {

	private  Analysis fft;
    private int bps;
	
	public ComputeBasicFrequency(int np, int s) {
		
		fft = new Analysis();
		bps=s;
		fft.init(np, s);
	}
	
	public void copy(double x) {
		fft.copy(x);
	}
	
	public void transform() {

//		fft.doHanning();
//		fft.doBlackman();
//		fft.doGauss(0);
		
		fft.afm();
//		fft.ffm();
		
		System.out.println("Result : "+Double.toString(fft.coeff().c));
		System.out.println("Max. compound :"+Double.toString(fft.coeff().max));
		
//		for(int i=0;i<fft.getBasIndex();i++) 
//			System.out.println("i : "+Double.toString(fft.getBas(i)));			
					
	}
	
	public double[] getBasicFrequencyTable() {
		return fft.getBasicFrequencyTable();
	}
}
