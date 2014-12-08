package cafe.analysis;

public class Autocorrelation {

	private static double[] x,r;
	
	public static double[] get_fr() {
		return x;
	}

	public static double[] get_fi() {
		return r;
	}

	public static void acorr(double[] _x, double[] _r, int l, int np) {
		double d;
		int k,i;
	
	    x = java.util.Arrays.copyOf(_x, _x.length);
	    r = java.util.Arrays.copyOf(_r, _r.length);	    

		for (k=1; k<=np; k++) {
			d=0;
		 	for (i=0; i<l-k-1; i++) {
			     d=d+x[i]*x[i+k];
		 	}
		 	r[k-1]=d;
		}
	}

}
