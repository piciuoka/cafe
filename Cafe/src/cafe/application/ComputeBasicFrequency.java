package cafe.application;

import cafe.analysis.AudioFeature;
import cafe.analysis.FundamentalFrequency;
import cafe.analysis.FundamentalFrequencyAutocorrelation;
import cafe.analysis.FundamentalFrequencyCepstrum;
import cafe.analysis.WindowFunction;

public class ComputeBasicFrequency {

	private FundamentalFrequency ff;
    private int bps;
    private int n;
	
	public ComputeBasicFrequency(int np, int s) {
		
		bps=s;
		n=np;
		ff = new FundamentalFrequencyAutocorrelation(np, s);
//		ff = new FundamentalFrequencyCepstrum(np, s);		
	}
	
	public void copy(double x) {
		ff.copy(x);
	}
	
	public void transform() {

		ff.setWindow(WindowFunction.HANNING);
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
