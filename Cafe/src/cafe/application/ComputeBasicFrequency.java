package cafe.application;

import cafe.analysis.Analysis;
import cafe.analysis.AudioFeature;

public class ComputeBasicFrequency {

	private Analysis analysis;
    private int bps;
    private int n;
	
	public ComputeBasicFrequency(int np, int s) {
		
		analysis = new Analysis();
		bps=s;
		n=np;
		analysis.init(np, s);
	}
	
	public void copy(double x) {
		analysis.copy(x);
	}
	
	public void transform() {

		analysis.setWindow(Analysis.HANNING);

//		analysis.afm();
		analysis.ffm();
		
		AudioFeature audioFeature = new AudioFeature(analysis.getBasicFrequencyTable(), analysis.getBasicFrequencyAmplitudeTable());

		System.out.println("Jitter (absolute)  : "+Double.toString(audioFeature.jitterAbsolute()));
		System.out.println("Jitter (relative)  : "+Double.toString(audioFeature.jitterRelative()));
		System.out.println("Shimmer (absolute) : "+Double.toString(audioFeature.shimmerAbsolute()));
		System.out.println("Shimmer (relative) : "+Double.toString(audioFeature.shimmerRelative()));
		
//		for(int i=0;i<fft.getBasIndex();i++) 
//			System.out.println("i : "+Double.toString(fft.getBas(i)));			
					
	}
		
	public double[] getBasicFrequencyTable() {
		return analysis.getBasicFrequencyTable();
	}

	public double[] getBasicFrequencyAmplitudeTable() {
		return analysis.getBasicFrequencyAmplitudeTable();
	}
	
}
