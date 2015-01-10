package cafe.analysis;

public class Analysis {
	
//	private static final int PERIODCOUNT = 4;
	private static final double OVERLAP = 0.5;
	private static final int WINDOW = 1024;
	private static final boolean DEBUG = false;
		
	private int window = 0;
	private WindowFunction windowFunction = new WindowFunction();
	
	private int n;
	private int freq;
	private int points;
	private int sign;
	private double bas; 	
	
	private double[] re;
	private double[] im;
	private double[] data;
	public int index;

	protected int index_res;
    protected double res[];
    protected double resAmplitude[];
    
    private double phaseResult[];
		
	public void init(int _n, int s){
		n = _n;
		freq = s;
//		int log2 = (int)(Math.log(n)/Math.log(2)+1e-10);
		double log2 = Math.log(n)/Math.log(2);
		int ln_n = (int) Math.round(log2+0.5);
		points = (int) Math.round(Math.pow(2, ln_n));

		re = new double[points+1];
		im = new double[points+1];

		index = 0;
		data = new double[_n+1];
		clear();
	}
		
	public void clear() {
	
//		index = 0;
		for (int i=0;i<points;i++) {
			re[i] = 0;
			im[i] = 0;
		}
	}

	public void clearData() {
		index = 0;
	}
	
	public void copy(double x) {
		
		assert ((index>=0) && (index<n));
		data[index] = x;
		index++;
	}
	
	public void fft(int lnn) {
	
		sign = -1;
	    cafe.analysis.FFT.fft(re,im,lnn,sign);
	    re = cafe.analysis.FFT.get_re();
	    im = cafe.analysis.FFT.get_im();
	}
	
	public double[] getTheta(int _begin, int count) {
 	    assert((count>=0) && (_begin+count<n));

 	    double log2 = Math.log(count)/Math.log(2);
		int lnn = (int)Math.round(log2+1.0);
		int npoints=(int)Math.round(Math.pow(2.0,lnn));

	    for (int i=0;i<count;i++) {
	    	re[i]=data[_begin+i];
	    	im[i]=0;
	    }
	    for (int i=count;i<npoints;i++) {
	    	re[i]=0;
	    	im[i]=0;	    	
	    }	    
		sign = -1;
	    cafe.analysis.FFT.fft(re,im,lnn,sign);
	    return cafe.analysis.FFT.get_theta();	    
	}

	public double[] getPhaseSpectrumTable() {
		return phaseResult;
	}
	
	public double getRe(int i) {
		assert((i>=0) && (i<points));
		return re[i];
	}

	public double[] getReTable() {
	    for (int i=0;i<1024;i++) 
	    	re[i]=data[i];	
		sign = -1;
	    cafe.analysis.FFT.fft(re,im,10,sign);
	    re = cafe.analysis.FFT.get_re();
	    im = cafe.analysis.FFT.get_im();
		return re;
	}
	
	public double[] getPhaseTable() {
	    for (int i=0;i<1024;i++) 
	    	re[i]=data[i];	
	    cafe.analysis.FFT.fft(re,im,10,-1);
	    double[] temp1 = cafe.analysis.FFT.get_theta();
//	    for (int i=0; i<temp1.length;i++)
//	    	temp1[i]=Math.abs(temp1[i]);
		return temp1;
	}
	
	public double getIm(int i) {
	  assert((i>=0) && (i<points));
	  return im[i];
	}

	public double getMod(int i) {
	  assert((i>=0) && (i<points));
	  return Math.sqrt(re[i]*re[i]+im[i]*im[i]);
	}

	public double getFrequency(int i) {
	  assert((i>=0) && (i<points));
	  return (i*freq)/points;
	}
	
	

//	public int getPoints() {
//	  return points;
//	}

//	public double getFundamentalFrequency(int i) {
//	  assert((i>=0) && (i<=index_res));
//	  return res[i];
//	}
//
//	public int getFundamentalFrequencyCount() {
//	  return index_res;
//	}
//
//	public double[] getBasicFrequencyTable() {
//		double[] temp = new double[index_res];
//		temp = java.util.Arrays.copyOf(res, index_res);
//		return temp;
////		return res;
//	}
//
//	public double[] getBasicFrequencyAmplitudeTable() {
//		double[] temp = new double[index_res];
//		temp = java.util.Arrays.copyOf(resAmplitude, index_res);
//		return temp;
////		return resAmplitude;
//	}
	
	
	public void setWindow(int w) {
		window = w;
	}
	
	public double[] doWindow(double[] t, int m) {		
		switch (window) {		
			case WindowFunction.HANNING: return windowFunction.doHanning(t,m); 
			case WindowFunction.BLACKMANN: return windowFunction.doBlackman(t,m);
			case WindowFunction.GAUSS: return windowFunction.doGauss(t,m,freq); 
			default: return t;
		}
	
	}
	

	public void acorr(int l) {
	// !!! speed up
		int _n;
//		if (n>350) _n=350; else _n=n;
		_n=l;
		cafe.analysis.Autocorrelation.acorr(re,im,l,_n);
		re = cafe.analysis.Autocorrelation.get_fr();
		im = cafe.analysis.Autocorrelation.get_fi();		
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
//	    if (_N<350) n2=_N; else n2=350;
	    n2=_N;
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
	  			y=(new Interpolation().interpolation(tx[lastElement]-1,getIm((int)Math.round(tx[lastElement]-1)),
	  							tx[lastElement],getIm((int)Math.round(tx[lastElement])),
	  							tx[lastElement]+1,getIm((int)Math.round(tx[lastElement]+1)),x));
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
	    int next;
	    double framebas,lastframebas;
	    
	    clear();
	    for (i=0; i<n; i++)  
	    	re[i]=data[i];
	    
//	    re = doWindow(re,n);

	    acorr(n);

	    bas=getABasFr(n);

	    if (DEBUG)
	    	System.out.println("F0 = " + Double.toString(freq/bas));    
	    
	    if (bas == 0) {
	    	System.out.println("Basic Frequency not found");
	    	return;
	    }
	    
	    lastframebas = bas;
	    
//	    res = new double[(int)Math.round(((n*2)/bas)+0.5)];
//	    resAmplitude = new double[(int)Math.round(((n*2)/bas)+0.5)];
	    res = new double[(int)Math.round(n/(WINDOW*OVERLAP)+0.5)];
	    resAmplitude = new double[(int)Math.round(n/(WINDOW*OVERLAP)+0.5)];
	    
	    index_res=0;

// raises level of error in F0 estimation     
//	    next=(int)Math.round(bas*PERIODCOUNT);
	    next=WINDOW;
	    j=0;
	  
	    do {

	    	for (i=0; i<next; i++)
	    		re[i]=data[i+j];
	    	
		    resAmplitude[index_res]=max(re,next);
		    
		    re = doWindow(re,next);

	    	acorr(next);
	    		    	
	    	framebas=getABasFr(next);
	    	
	    	if ((framebas!=0) && (Math.abs(freq/framebas-freq/lastframebas)<25)
	    			/* && (Math.abs(freq/framebas-freq/bas)<25)*/ ) {
	    		
	    		res[index_res]=freq/framebas;
	    		lastframebas=framebas;
	    	} else {
//		    	lastframebas=bas;
		    	res[index_res]=freq/lastframebas;
//		    	res[index_res]=0;
	    	}

	    	index_res++;

// raises level of error in F0 estimation 
//	    	j=(int)Math.round(bas*(index_res));
//		    j=(int)Math.round(((bas*PERIODCOUNT*OVERLAP)*index_res));
	    	
		    j=(int)Math.round(((WINDOW*OVERLAP)*index_res));

	    }
	    while (j+next<n);

		if (DEBUG)
	    for (i=0; i<index_res; i++) { 
			System.out.println(Integer.toString(i)+" "+Double.toString(res[i]));	    	
	    }	    	  
	}
	
	
	public void ffm() {
		
		int i,j;
	    int next;
	    double framebas,lastframebas;
	
	    clear();
	    
		double log2 = Math.log(n)/Math.log(2);
		int ln_n = (int)Math.round(log2+1);		
		int count=(int)Math.round(Math.pow(2.0,ln_n));

	    for (i=0; i<n; i++) 
	    	re[i]=data[i];
	    
//	    re = doWindow(re,n);

	    fft(ln_n);

	    for (i=0; i<count; i++) {
	      re[i]=getMod(i);
	      im[i]=re[i];
	    }
	    
	    fft(ln_n);

	    for (i=0; i<count; i++) {
		      im[i]=getMod(i);
		}
	    
	    bas=getABasFr(count/2);
	    
	    if (DEBUG)
	    	System.out.println("F0 = " + Double.toString(freq/bas));    

	    if (bas==0) {
	    	System.out.println("Basic Frequency not found");
	    	return;
	    }
	    
	    lastframebas=bas;

	    res = new double[(int)Math.round(n/(WINDOW*OVERLAP)+0.5)];
	    resAmplitude = new double[(int)Math.round(n/(WINDOW*OVERLAP)+0.5)];
	    index_res=0;
	    
// raises level of error in F0 estimation 	    
//	    next=(int)Math.round(bas*PERIODCOUNT);
	    next=WINDOW;
   
//	    lnN:=Round(Log2(next)+1);
		log2 = Math.log(next)/Math.log(2);
		ln_n = (int)Math.round(log2+1);		
		count=(int)Math.round(Math.pow(2.0,ln_n));

//		re = new double[points+1];
//		im = new double[points+1];
		clear();

		j=0;
		
		do {

			for (i=0;i<next;i++) {
		    	re[i]=data[i+j];
		    	im[i]=0;
		    }
		    for (i=next;i<count;i++) {
		    	re[i]=0;
		    	im[i]=0;
		    }
		    		    
		    resAmplitude[index_res]=max(re,next);
		    
		    re = doWindow(re,next);
		    
		    fft(ln_n);
		    
		    for (i=0;i<count;i++) {
		    	if (getFrequency(i)>40) {
		    		re[i]=getMod(i);
		    		im[i]=re[i];
		    	} else {
		    		re[i]=0;
		    		im[i]=0;
		    	}
		    }
		    
		    fft(ln_n);

		    for (i=0; i<count; i++) {
			      im[i]=getMod(i);
			}		    
		    
		    framebas=getABasFr(count/2);

	    	if ((framebas!=0) && (Math.abs(freq/framebas-freq/lastframebas)<25) 
	    		/* && (Math.abs(freq/framebas-freq/bas)<25)*/ ) {
		    		
		    	res[index_res]=freq/framebas;
		    	lastframebas=framebas;
		    } else {
//		    	lastframebas=bas;
		    	res[index_res]=freq/lastframebas;
//		    	res[index_res]=0;
		    }

		    index_res++;

// raises level of error in F0 estimation 
//	    	j=(int)Math.round(bas*(index_res));
//		    j=(int)Math.round(((bas*PERIODCOUNT*OVERLAP)*index_res));		    

		    j=(int)Math.round(((WINDOW*OVERLAP)*index_res));
		    
		}
		while (j+next<n);
	
		if (DEBUG)
			for (i=0; i<index_res; i++) { 
				System.out.println(Integer.toString(i)+" "+Double.toString(res[i]));	    	
			}
		
	}	
	
	private double max(double[] t, int n) {
		double max = t[0];
		for (int i=1;i<n; i++) 
			if (t[i]>max) max=t[i];
		return max;
	}

//	public double getFundamentalFrequencyAmplitude(double frequency, double[] in, int lnn) {
//
//		double[] temp = new double[in.length];
//		cafe.analysis.FFT.fft(in,temp,lnn,-1);
//		int i = (int)Math.floor(freq/frequency+0.5);
//		return cafe.analysis.FFT.get_re()[i];		
//	}	
	
	public void fpm() {
		
		int i,j=0,k=0,next=WINDOW;
		double[] temp;
		double sum;
		
		phaseResult = new double[(int)Math.round((n/(WINDOW*OVERLAP))+0.5)];
		do {
			temp = getTheta(j,next);
			sum=0;
			for (i=0;i<temp.length;i++)
				sum+=temp[i];
			phaseResult[k++]=sum;
			j+=(int)Math.round(WINDOW*OVERLAP+0.5);
		}		
		while (j+next<n);
		
		if (DEBUG)
		    for (i=0; i<phaseResult.length; i++) { 
				System.out.println(Integer.toString(i)+" "+Double.toString(phaseResult[i]));	    	
		    }
		
	}

	
//	public CoeffPack coeff() {
//	
//		int i;
//	    double c,c0,cmax;
//	    int j;
//	    double y1,y2;
//	    CoeffPack result = new CoeffPack(); 
//	    
//	    c=0;
//	    cmax=-100000;
//	    j=0;
//	    result.c=0;
//	    result.max=0;
//	    if (bas==0) return result;
//	    
//	    
//	    for (i=0;i<index_res;i++) {
//	    	y1=res[i];
//	    	y2=res[i+1];
//	    	if ((y1!=0) && (y2!=0)) {
//	    		j++;
//	    		c0=Math.abs(y2-y1)/bas;
//	    		c=c+c0;
//	    		if (c0>cmax) cmax=c0;
//	    	}
//	    }
//	    
//	  result.c = Math.round((c*10000.0)/j)/10000.0;
//	  result.max = Math.round(cmax*10000.0)/10000.0;
//
////	  result.c = ((c*10000)/j)/10000.0;
////	  result.max = (cmax*10000)/10000.0;
//	  
//	  return result;	  
//	}
	
	
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
	
}
