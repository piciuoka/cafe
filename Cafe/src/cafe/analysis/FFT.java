package cafe.analysis;

public class FFT {

	private static double[] re,im,alfa;
	private static int n;
		
	public static final int power2(int n) {
		return 1 << n;
	}
	
	public static double[] get_re() {
		return re;
	}

	public static double[] get_im() {
		return im;
	}
	
	public static double[] get_theta() {
		if (n>0) {
			alfa = new double[n];
			for (int i=0; i<n; i++) {
				alfa[i] = Math.atan2(re[i],im[i]);
			}
		}
		return alfa;
	}

	public static void fft(double[] _re, double[] _im, int ln_n, int sign) {
		
		int i,j,k,l, le, le1, ip, nd2;
	    double s, ur, ur1,  ui, wr, wi, tr, ti;
	    double div_n;

// TODO valuecheck	    
	    re = java.util.Arrays.copyOf(_re, _re.length);
	    im = java.util.Arrays.copyOf(_im, _im.length);	    
// fr = (double[])_fr.clone();	    
	    
		n = power2(ln_n);
		
		//  divN := 1/sqrt(n*1.0);
		div_n = 1.0/n;
		nd2 = n/2;
		j = 1;
		for (i=1;i<=n-1;i++) {
		    if (i < j) {
		    	s = re[i-1]; re[i-1] = re[j-1]; re[j-1] = s;
		    	s = im[i-1]; im[i-1] = im[j-1]; im[j-1] = s;
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
			        tr = re[ip]*ur - im[ip]*ui;
			        ti = re[ip]*ui + im[ip]*ur;
			        re[ip] = re[i] - tr;
			        im[ip] = im[i] - ti;
			        re[i] = re[i] + tr;
			        im[i] = im[i] + ti;
			        i = i + le;
			    }
			    ur1 = ur*wr - ui*wi;
			    ui = ur*wi + ui*wr;
			    ur = ur1;
			}
		}	// {l - l>kke}
		
		for (i=0; i<n; i++) {
		    re[i] = re[i]*div_n;
		    im[i] = im[i]*div_n;
		}
		
	}
	
}
