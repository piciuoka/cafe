package cafe.analysis;

public class FundamentalFrequency extends Analysis {

	public void calculate() {		
	}

	public int getFundamentalFrequencyCount() {
		  return index_res;
		}
	
	public double getFundamentalFrequency(int i) {
		  assert((i>=0) && (i<=index_res));
		  return res[i];
		}
	
	public double[] getFundamentalFrequencyTable() {
		double[] temp = new double[index_res];
		temp = java.util.Arrays.copyOf(res, index_res);
		return temp;
	}

	public double[] getFundamentalFrequencyAmplitudeTable() {
		double[] temp = new double[index_res];
		temp = java.util.Arrays.copyOf(resAmplitude, index_res);
		return temp;
	}
	
	public double[] getHNRTable() {
		double[] temp = new double[index_res];
		temp = java.util.Arrays.copyOf(hnrTable, index_res-4);
		return temp;
	}

	public double[] getHNRTabledB() {
		double[] temp = new double[index_res];
		temp = java.util.Arrays.copyOf(hnrTabledB, index_res-4);
		return temp;
	}
	
}
