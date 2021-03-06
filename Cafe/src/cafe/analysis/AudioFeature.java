package cafe.analysis;

public class AudioFeature {

	private double[] fundamentalFrequency;
	private double[] fundamentalFrequencyAmplitude;
	private double[] hnrTable;
	
	
	public AudioFeature(double[] ff, double[] ffa, double[] hnr){
		fundamentalFrequency = ff;
		fundamentalFrequencyAmplitude = ffa;
		hnrTable = hnr;
	}

	public AudioFeature(FundamentalFrequency ff){
		fundamentalFrequency = ff.getFundamentalFrequencyTable();
		fundamentalFrequencyAmplitude = ff.getFundamentalFrequencyAmplitudeTable();
		hnrTable = ff.getHNRTable();
	}
	
	public double jitterAbsolute() {
		
		return averageAbsDifference(fundamentalFrequency);
	}

	public double jitterRelative() {
				
		return averageAbsDifference(fundamentalFrequency) / average(fundamentalFrequency);
	}

	public double shimmerAbsolute() {
		double sum = 0.0d;
		for(int i=0;i<fundamentalFrequencyAmplitude.length-1;i++)
			sum += Math.abs(20.0*Math.log10(fundamentalFrequencyAmplitude[i+1]/fundamentalFrequencyAmplitude[i]));
		return sum / (double)(fundamentalFrequencyAmplitude.length-1.0);
	}
	
	public double shimmerRelative() {

		return averageAbsDifference(fundamentalFrequencyAmplitude) / average(fundamentalFrequencyAmplitude);
	}

	public double harmonicsToNoise() {
		
		return 10.0*Math.log10(average(hnrTable));
	}

	private double averageAbsDifference(double[] t) {
		double sum = 0.0d;		
		for(int i=0;i<t.length-1;i++) 
			sum += Math.abs(t[i]-t[i+1]);		
		return sum / (t.length - 1.0);		
	}
	
	private double average(double[] t) {
		
		double sum = 0.0d;
		for (double a:t)
			sum += a;
		return sum / (double)t.length;
	}
	
}
