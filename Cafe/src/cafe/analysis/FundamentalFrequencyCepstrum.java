package cafe.analysis;

public class FundamentalFrequencyCepstrum extends FundamentalFrequency {
	
	public FundamentalFrequencyCepstrum(int _n, int s) {
		init(_n,s);
	}
	
	public void calculate() {
		ffm();
		hnr();
	}
}
