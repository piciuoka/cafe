package cafe.analysis;

public class WindowFunction {
	
	public double[] doHanning(double[] t, int m) {
		for (int i=0; i<m; i++) {
		  t[i]=0.5*(1-Math.cos(2.0*Math.PI*i/m))*t[i];
		}
		return t;
	}
		
	public double[] doBlackman(double[] t, int m) { //dla porównywania fazy
		for (int i=0; i<m; i++) {
		  t[i]=(0.42-0.5*Math.cos(2*Math.PI*i/m)+0.08*Math.cos(4.0*Math.PI*i/m))*t[i];
		}
		return t;
	}
	
	public double[] doGauss(double[] t, int m, int k) {
		if (k==0) k=1;
		for (int i=0; i<m; i++) {
			double temp = ((i-m-1.0)/2.0)/k;
			t[i]=Math.exp(-temp*temp)*t[i];
		}
		return t;
	}
}
