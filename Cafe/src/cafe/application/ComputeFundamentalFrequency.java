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
    private int classification;
    private double jitter, shimmer, hnr;
	
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
	
	public int transform() {
		
		ff.calculate();
						
		AudioFeature audioFeature = new AudioFeature(ff);
		
		jitter = audioFeature.jitterRelative();
		shimmer = audioFeature.shimmerRelative();
		hnr = audioFeature.harmonicsToNoise();

		System.out.println("Jitter (absolute)  : "+Double.toString(audioFeature.jitterAbsolute()));
		System.out.println("Jitter (relative)  : "+Double.toString(jitter));
		System.out.println("Shimmer (absolute) : "+Double.toString(audioFeature.shimmerAbsolute()));
		System.out.println("Shimmer (relative) : "+Double.toString(shimmer));
		System.out.println("HNR                : "+Double.toString(hnr));
	
		LinearDiscriminantAnalysis lda = new LinearDiscriminantAnalysis();
		double [][] predata = new double[3][1];
		predata[0][0] = jitter;
		predata[1][0] = shimmer;
		predata[2][0] = hnr;
		
		classification = (int) lda.classification(predata);
		
		System.out.print(" Classification : "+classification+" == ");

		if (classification == 1)
			System.out.println(" HEALTHY ");
		else
			System.out.println(" PATHOLOGICAL ");
		
		return classification;
					
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
	
	public int getClassification() {
		return classification;
	}

	public double getJitter() {
		return jitter;
	}

	public double getShimmer() {
		return shimmer;
	}

	public double getHNR() {
		return hnr;
	}
	
}
