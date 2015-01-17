package cafe.application;

import cafe.analysis.AudioFeature;
import cafe.analysis.FundamentalFrequency;
import cafe.analysis.FundamentalFrequencyAutocorrelation;
import cafe.analysis.FundamentalFrequencyCepstrum;
import cafe.analysis.LinearDiscriminantAnalysis;
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
		System.out.println("HNR                : "+Double.toString(audioFeature.harmonicsToNoise()));
	
		LinearDiscriminantAnalysis lda = new LinearDiscriminantAnalysis();
		double [][] predata = new double[3][1];
		predata[0][0]= audioFeature.jitterRelative();
		predata[1][0]= audioFeature.shimmerRelative();
		predata[2][0]= audioFeature.harmonicsToNoise();
		
		int classification = (int) lda.classification(predata);
		
		System.out.print(" Classification : "+classification+" == ");

		if (classification == 1)
			System.out.println(" HEALTHY ");
		else
			System.out.println(" PATHOLOGICAL ");
					
	}
		
	public double[] getFundamentalFrequencyTable() {
		return ff.getFundamentalFrequencyTable();
	}

	public double[] getFundamentalFrequencyAmplitudeTable() {
		return ff.getFundamentalFrequencyAmplitudeTable();
	}

	public double[] getHNRTabledB() {
		return ff.getHNRTabledB();
	}
	
}
