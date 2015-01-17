package cafe.analysis;

public class FundamentalFrequencyAutocorrelation extends FundamentalFrequency {
	
	public FundamentalFrequencyAutocorrelation(int _n, int s) {
		init(_n,s);
	}
	
	public void calculate() {
		afm();
		hnr();
	}

}
