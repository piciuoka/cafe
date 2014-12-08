package cafe.analysis;

public class FFT {

	private static double[] fr,fi;
		
	public static final int power2(int n) {
		return 1 << n;
	}
	
	public static double[] get_fr() {
		return fr;
	}

	public static double[] get_fi() {
		return fi;
	}
	
	public static void fft(double[] _fr, double[] _fi, int ln_n, int sign) {
		
		int i,j,k,l, le, le1, ip, nd2;
	    double s, ur, ur1,  ui, wr, wi, tr, ti;
	    int n;
	    double div_n;

// TODO valuecheck	    
	    fr = java.util.Arrays.copyOf(_fr, _fr.length);
	    fi = java.util.Arrays.copyOf(_fi, _fi.length);	    
// fr = (double[])_fr.clone();	    
	    
		n = power2(ln_n);
		
		//  divN := 1/sqrt(n*1.0);
		div_n = 1.0/n;
		nd2 = n/2;
		j = 1;
		for (i=1;i<=n-1;i++) {
		    if (i < j) {
		    	s = fr[i-1]; fr[i-1] = fr[j-1]; fr[j-1] = s;
		    	s = fi[i-1]; fi[i-1] = fi[j-1]; fi[j-1] = s;
		    }
		    k = nd2;		
		    while (k<j) {
		    	j = j - k;
		    	k = k / 2;
		    }
		    j = j + k;
		}
	
		for (l=1;l<=ln_n;l++) {
		    le = power2(l);
			le1 = le / 2;
			ur = 1;
			ui = 0;
			wr = Math.cos(Math.PI/le1);
			wi = sign * Math.sin(Math.PI/le1);
			for (j=0; j<le1; j++) {
				i = j;
			    while (i<n) {
			    	ip = i + le1;
			        tr = fr[ip]*ur - fi[ip]*ui;
			        ti = fr[ip]*ui + fi[ip]*ur;
			        fr[ip] = fr[i] - tr;
			        fi[ip] = fi[i] - ti;
			        fr[i] = fr[i] + tr;
			        fi[i] = fi[i] + ti;
			        i = i + le;
			    }
			    ur1 = ur*wr - ui*wi;
			    ui = ur*wi + ui*wr;
			    ur = ur1;
			}
		}	// {l - l>kke}
		
		for (i=0; i<n; i++) {
		    fr[i] = fr[i]*div_n;
		    fi[i] = fi[i]*div_n;
		}
		
	}
	
}
