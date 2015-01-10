package cafe.analysis;

public class Interpolation {

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
