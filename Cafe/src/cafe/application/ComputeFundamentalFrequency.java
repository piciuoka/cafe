package cafe.application;

import cafe.analysis.AudioFeature;
import cafe.analysis.FundamentalFrequency;
import cafe.analysis.FundamentalFrequencyAutocorrelation;
import cafe.analysis.FundamentalFrequencyCepstrum;
import cafe.analysis.WindowFunction;

public class ComputeFundamentalFrequency {

	private FundamentalFrequency ff;
    private int bps;
    private int n;
	
	public ComputeFundamentalFrequency(int np, int s, char f,boolean autocorr) {
		
		bps=s;
		n=np;
		if(autocorr){
		ff = new FundamentalFrequencyAutocorrelation(np, s);
		} else{
	ff = new FundamentalFrequencyCepstrum(np, s);	
		}
		switch(f){
		case 'h': ff.setWindow(WindowFunction.HANNING); break;
		case 'r':ff.setWindow(WindowFunction.RECTANGLE); break;
		case 'b':ff.setWindow(WindowFunction.BLACKMANN); break;
		case 'g':ff.setWindow(WindowFunction.GAUSS); break;
		default: ff.setWindow(WindowFunction.RECTANGLE); 
		}
	}
	
	public void copy(double x) {
		ff.copy(x);
	}
	
	public void transform() {
		
		ff.calculate();
						
		AudioFeature audioFeature = new AudioFeature(ff);

		System.out.println("Jitter (absolute)  : "+Double.toString(audioFeature.jitterAbsolute()));
		System.out.println("Jitter (relative)  : "+Double.toString(audioFeature.jitterRelative()));
		System.out.println("Shimmer (absolute) : "+Double.toString(audioFeature.shimmerAbsolute()));
		System.out.println("Shimmer (relative) : "+Double.toString(audioFeature.shimmerRelative()));
		
//		for(int i=0;i<fft.getBasIndex();i++) 
//			System.out.println("i : "+Double.toString(fft.getBas(i)));			
					
	}
		
	public double[] getFundamentalFrequencyTable() {
		return ff.getFundamentalFrequencyTable();
	}

	public double[] getFundamentalFrequencyAmplitudeTable() {
		return ff.getFundamentalFrequencyAmplitudeTable();
	}
	
}
