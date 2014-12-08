package cafe.analysis;

public class Analysis {
	
	private int n;
	private int freq;
	private int ln_n;
	private int points;
	private int sign;
	private double bas; 	
	
	private double[] fr;
	private double[] fi;
	private double[] data;

	private int index_res;
    private double res[];
	
	public int index;

	
	public void init(int _n, int s){
		n = _n;
		freq = s;
//		int log2 = (int)(Math.log(n)/Math.log(2)+1e-10);
		double log2 = Math.log(n)/Math.log(2);
		ln_n = (int) Math.round(log2+0.5);
		points = (int) Math.round(Math.pow(2, ln_n));

		fr = new double[points+1];
		fi = new double[points+1];

		index = 0;
		data = new double[_n+1];
		clear();
	}
		
	public void clear() {
	
		index = 0;
		for (int i=0;i<points;i++) {
			fr[i] = 0;
			fi[i] = 0;
		}
	}
	
	public void copy(double x) {
		
		assert ((index>=0) && (index<n));
		data[index] = x;
		index++;
	}
	
	public void fft() {
	
		sign = -1;
	    cafe.analysis.FFT.fft(fr,fi,ln_n,sign);
	    fr = cafe.analysis.FFT.get_fr();
	    fi = cafe.analysis.FFT.get_fi();
	}

	public double getRe(int i) {
	  assert((i>=0) && (i<points));
	  return fr[i];
	}

	public double getIm(int i) {
	  assert((i>=0) && (i<points));
	  return fi[i];
	}

	public double getMod(int i) {
	  assert((i>=0) && (i<points));
	  return Math.sqrt(fr[i]*fr[i]+fi[i]*fi[i]);
	}

	public double getFreq(int i) {
	  assert((i>=0) && (i<points));
	  return (i*freq)/points;
	}

	public int getPoints() {
	  return points;
	}

	public double getBas(int i) {
	  assert((i>=0) && (i<=index_res));
	  return res[i];
	}

	public int getBasIndex() {
	  return index_res;
	}

	public double[] getBasicFrequencyTable() {
		double[] temp = new double[index_res];
		temp = java.util.Arrays.copyOf(res, index_res);
		return temp;
//		return res;
	}
	
	public void doHanning() {
		for (int i=0; i<n; i++) {
		  data[i]=0.5*(1-Math.cos(2*Math.PI*i/n))*data[i];
		}
	}
		
	public void doBlackman() { //dla porównywania fazy
		for (int i=0; i<n; i++) {
		  data[i]=(0.42-0.5*Math.cos(2*Math.PI*i/n)+0.08*Math.cos(4*Math.PI*i/n))*data[i];
		}
	}
	
	public void doGauss(int k) {
	 if (k==0) k=freq;
		for (int i=0; i<n; i++) {
			double temp = ((i-n-1)/2)/k;
			data[i]=Math.exp(-temp*temp)*data[i];
		}
	}

	public void acorr(int l) {
	// !!! speed up
		int _n;
		if (n>350) _n=350; else _n=n;
		cafe.analysis.Autocorrelation.acorr(fr,fi,l,_n);
		fr = cafe.analysis.Autocorrelation.get_fr();
		fi = cafe.analysis.Autocorrelation.get_fi();		
	}

	public double getABasFr(int _N) {
		int i;
	    double y,y1,y2;
	    double[] tx,ty;
	    int howmany,n2;
	    double maxx,maxy,x;
	    double result;

	    tx = new double[_N+1];
	    ty = new double[_N+1];

	    for (i=0; i<_N; i++) {
	    	tx[i]=0;
	    	ty[i]=0;
	    }
	    howmany=0;

	    // speedup
	    if (_N<350) n2=_N; else n2=350;
	    for (i=79; i<n2-1; i++) {
	    	y1=getIm(i-1)-getIm(i);
	    	y2=getIm(i)-getIm(i+1);
	    	if (y1*y2<0) {
	    		if ((y1<0) && (y2>0)) {
	    			tx[howmany]=i;
	    			ty[howmany]=getIm(i);
	    			howmany++;
	    		}
	    	}
	    }
////  Caution: position of tx and ty is swapped	    	    
////  Bubblesort(ty,tx,0,howmany-1);

		int lower,upper,shift,bubble;
	    double swap;

	    lower=0;
	    upper=howmany-1;
	    while (upper>lower) {
	    	shift=lower;
	    	for (bubble=lower; bubble<upper; bubble++) {
	    		if (ty[bubble]>ty[bubble+1]) {
	    			swap=tx[bubble];
	    			tx[bubble]=tx[bubble+1];
	    			tx[bubble+1]=swap;

	    			swap=ty[bubble];
	    			ty[bubble]=ty[bubble+1];
	    			ty[bubble+1]=swap;

	    			shift=bubble;
	    		}
	    	}
	    upper=shift;
	    } 
	    	    	    
//// end bubblesort	    
	    
	  	result=0;
	  	if (howmany>0) {
		  	int lastElement = howmany-1;
	  		x=tx[lastElement]-1;
	  		maxy=getIm((int)Math.round(tx[lastElement]-1));
	  		
	  		do {
	  			y=interpolation(tx[lastElement]-1,getIm((int)Math.round(tx[lastElement]-1)),
	  							tx[lastElement],getIm((int)Math.round(tx[lastElement])),
	  							tx[lastElement]+1,getIm((int)Math.round(tx[lastElement]+1)),x);
	  			if (y>maxy) maxy=y;
	  			x=x+0.01;
	  		}
	  		while (y>=maxy);
	  	result=x+1.0;
	  	}
	  	return result;
	}	

	public void afm() {
	
		int i,j;
	    double step;
	    int next;
	    double framebas,lastframebas;
	    
	    clear();
	    for (i=0; i<n; i++)  
	    	fr[i]=data[i];

	    acorr(n);

	    bas=getABasFr(n);
	    System.out.println("F0 = " + Double.toString(bas));    
	    
	    if (bas == 0) {
	    	System.out.println("Basic Frequency not found");
	    	return;
	    }
	    
	    lastframebas = bas;
	    
	    res = new double[(int)Math.round(((n*2)/bas)+0.5)];
	    index_res=0;

	    step=2;
	    next=(int)Math.round(bas*step);
	    j=0;
	  
	    do {

	    	for (i=0; i<next; i++)
	    		fr[i]=data[i+j];

	    	acorr(next);
	    	framebas=getABasFr(next);
	    	
	    	if ((framebas!=0) && (Math.abs(freq/framebas-freq/lastframebas)<25)
	    			/* && (Math.abs(freq/framebas-freq/bas)<25)*/ ) {
	    		
	    		res[index_res]=freq/framebas;
	    		lastframebas=framebas;
	    	} else {
	    		lastframebas=bas;
	    		res[index_res]=0;
	    	}

	    	index_res++;

	    	j=(int)Math.round(bas*(index_res));

	    }
	    while (j+next<n);

	    for (i=0; i<index_res; i++) { 
			System.out.println(Integer.toString(i)+" "+Double.toString(res[i]));	    	
	    }	    	  
	}
	
	
	public void afm2() {
		
		int i,j;
	    double krok;
	    int next;
	    double framebas,lastframebas;
	    
	    clear();
	    for (i=0; i<n; i++)  
	    	fr[i]=data[i];

	    acorr(n);

	    
//	    bas=getABasFr(n);
//	    System.out.println(Double.toString(bas));    
//	    
//	    if (bas == 0) {
//	    	System.out.println("Basic Frequency not found");
//	    	return;
//	    }
//	    
//	    lastframebas = bas;
//	    
//	    res = new double[(int)Math.round(((n*2)/bas)+0.5)];
//	    index_res=0;
//
//	    krok=2;
//	    next=(int)Math.round(bas*krok);
//	    j=0;
//	  
//	    do {
//
//	    	for (i=0; i<next; i++)
//	    		fr[i]=data[i+j];
//
//	    	acorr(next);
//	    	framebas=getABasFr(next);
//	    	
//	    	if ((framebas!=0) && (Math.abs(freq/framebas-freq/lastframebas)<50)
//	    			/* && (Math.abs(freq/framebas-freq/bas)<25)*/ ) {
//	    		
//	    		res[index_res]=freq/framebas;
//	    		lastframebas=framebas;
//	    	} else {
//	    		lastframebas=bas;
//	    		res[index_res]=0;
//	    	}
//
//	    	index_res++;
//
//	    	j=(int)Math.round(bas*(index_res));
//
//	    }
//	    while (j+next<n);
//
//	    for (i=0; i<index_res; i++) { 
//			System.out.println(Integer.toString(i)+" "+Double.toString(res[i]));	    	
//	    }	   	   
	    
	}
	
	
	
	public void ffm() {
		
		int i,j;
	    double krok;
	    int next;
	    double framebas,lastframebas;
	
	    clear();
	    for (i=0; i<n; i++) 
	    	fr[i]=data[i];
	    
	    fft();

	    for (i=0; i<getPoints(); i++) {
	      fr[i]=getMod(i);
	      fi[i]=fr[i];
	    }
	    
	    fft();
	    
	    bas=getABasFr(getPoints()/2);
	    
	    if (bas==0) {
	    	System.out.println("Basic Frequency not found");
	    	return;
	    }
	    
	    lastframebas=bas;
	    res = new double[(int)Math.round(((n*2)/bas)+0.5)];
	    index_res=0;
	    krok=2;
	    next=(int)Math.round(bas*krok);
   
//	    lnN:=Round(Log2(next)+1);
		double log2 = Math.log(next)/Math.log(2);
		ln_n = (int)Math.round(log2+1);
		
		points=(int)Math.round(Math.pow(2.0,ln_n));

		fr = new double[points+1];
		fi = new double[points+1];
		clear();

		j=0;
		
		do {

		    for (i=0;i<next;i++)
		    	fr[i]=data[i+j];
	
		    fft();
		    
		    for (i=0;i<getPoints();i++) {
		    	if (getFreq(i)>40) {
		    		fr[i]=getMod(i);
		    		fi[i]=fr[i];
		    	} else {
		    		fr[i]=0;
		    		fi[i]=0;
		    	}
		    }
		    
		    fft();
	
		    framebas=getABasFr(getPoints()/2);

	    	if ((framebas!=0) && (Math.abs(freq/framebas-freq/lastframebas)<25) 
	    		/* && (Math.abs(freq/framebas-freq/bas)<25)*/ ) {
		    		
		    	res[index_res]=freq/framebas;
		    	lastframebas=framebas;
		    } else {
		    	lastframebas=bas;
		    	res[index_res]=0;
		    }

		    index_res++;

		    j=(int)Math.round(bas*(index_res));
		    
		}
		while (j+next<n);
		
	    for (i=0; i<index_res; i++) { 
			System.out.println(Integer.toString(i)+" "+Double.toString(res[i]));	    	
	    }
		
	}	

	public void fpm() {
		
	}
	
	public CoeffPack coeff() {
	
		int i;
	    double c,c0,cmax;
	    int index;
	    double y1,y2;
	    CoeffPack result = new CoeffPack(); 
	    
	    c=0;
	    cmax=-100000;
	    index=0;
	    result.c=0;
	    result.max=0;
	    if (bas==0) return result;
	    
	    
	    for (i=0;i<index_res;i++) {
	    	y1=res[i];
	    	y2=res[i+1];
	    	if ((y1!=0) && (y2!=0)) {
	    		index++;
	    		c0=Math.abs(y2-y1)/bas;
	    		c=c+c0;
	    		if (c0>cmax) cmax=c0;
	    	}
	    }
	    
	  result.c = Math.round((c*10000.0)/index)/10000.0;
	  result.max = Math.round(cmax*10000.0)/10000.0;

//	  result.c = ((c*10000)/index)/10000.0;
//	  result.max = (cmax*10000)/10000.0;
	  
	  return result;	  
	}
	
	
	public void bubbleSort(double [] _tx, double [] _ty, int s, int e) {
		
		int lower,upper,shift,bubble;
	    double swap;

	    lower=s;
	    upper=e;
	    while (upper>lower) {
	    	shift=lower;
	    	for (bubble=lower; bubble<upper; bubble++) {
	    		if (_tx[bubble]>_tx[bubble+1]) {
	    			swap=_tx[bubble];
	    			_tx[bubble]=_tx[bubble+1];
	    			_tx[bubble+1]=swap;

	    			swap=_ty[bubble];
	    			_ty[bubble]=_ty[bubble+1];
	    			_ty[bubble+1]=swap;

	    			shift=bubble;
	    		}
	    	}
	    upper=shift;
	    }
	}

	
	public double interpolation(double x1, double y1, double x2, double y2, double x3, double y3, double x0) {
	
		double[] tab = new double [3*3+3];
		double[] x = new double [3];
		double[] b = new double [3];
	    int k,l,n;
	    double y,p;

	    n=3;
	    x[0]=x1;
	    x[1]=x2;
	    x[2]=x3;

	    tab[0]=y1;
	    tab[1]=y2;
	    tab[2]=y3;

	    for (k=1; k<n; k++) 
	    	for (l=k; l<n; l++)
	    		tab[k*n+l]=(tab[(k-1)*n+l]-tab[(k-1)*n+l-1])/(x[l]-x[l-k]);

	    for (k=0; k<n; k++) 
	    	b[k]=tab[k*(n+1)];

	    y=0;
	    p=1;
	    for (k=0; k<n;k++) {
	    	y=y+b[k]*p;
	    	p=p*(x0-x[k]);
	    }

	    return y;
	}
	
}
