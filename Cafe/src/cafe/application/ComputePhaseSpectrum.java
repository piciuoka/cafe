package cafe.application;

import cafe.analysis.Analysis;

public class ComputePhaseSpectrum {

	private  Analysis analysis;
    private int bps;
    private int n;
		
    public ComputePhaseSpectrum(int np, int s) {
			
		analysis = new Analysis();
		bps=s;
		n=np;
		analysis.init(np, s);
	}
		
	public void copy(double x) {
		analysis.copy(x);
	}
		
	public void transform() {
		analysis.fpm();			
	}
		
	public double[] getPhaseTable() {
		return analysis.getPhaseSpectrumTable();
	}
}
