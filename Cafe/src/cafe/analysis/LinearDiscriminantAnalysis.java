package cafe.analysis;

import static java.lang.System.out;
import javastat.multivariate.DiscriminantAnalysis;
import javastat.util.DataManager;

//double[] testgroup = new double[2];// tyle ile danych do nauki, grupa 1,2,...
//double[][] testdata = new double[4][]; //dane do nauki
//double[][] predata = new double[4][]; // dane do kwalifikacji

public class LinearDiscriminantAnalysis {

	double[][] testdata;
	double[][] testgroup;
    DiscriminantAnalysis testclass;

    
	public LinearDiscriminantAnalysis() {

		testdata = new double[3][];
		testgroup = new double[1][];

		DataManager dm = new DataManager();
        dm.scanFileToMatrix(System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "res" +
                System.getProperty("file.separator") +
                "data.txt", testdata, 3);
		dm.scanFileToMatrix(System.getProperty("user.dir") +
    			System.getProperty("file.separator") +
    			"res" +
    			System.getProperty("file.separator") +
    			"group.txt", testgroup, 1);
        
        testclass = new DiscriminantAnalysis(testgroup[0], testdata);

//        double [][] linearDiscriminants3 = testclass.linearDiscriminants; 
//        out.println(
//                "\n\nThe first discriminant based on non-null constructor  = ["
//                + dm.roundDigits(linearDiscriminants3[0][0], 3.0) + " , "
//                + dm.roundDigits(linearDiscriminants3[0][1], 3.0) + " , ");
//        out.println("The second discriminant based on non-null constructor = ["
//                    + dm.roundDigits(linearDiscriminants3[1][0], 3.0) + " , "
//                    + dm.roundDigits(linearDiscriminants3[1][1], 3.0) + " , ");
        
	}
	
	public double classification (double[][] predata) {
		
//		double[] testgroup = new double[2];// tyle ile danych do nauki, grupa 1,2,...
//		double[][] testdata = new double[4][]; //dane do nauki
//        double[][] predata = new double[4][]; // dane do kwalifikacji
//
//        DiscriminantAnalysis testclass = new DiscriminantAnalysis(testgroup, testdata);        
//        int[] predictedGroup = testclass.predictedGroup(testgroup, testdata, predata);  
//        for (int i = 0; i < predictedGroup.length; i++)
//        	System.out.println(predictedGroup[0]);
        
        int[] predictedGroup = testclass.predictedGroup(testgroup[0], testdata, predata);
        return predictedGroup[0];		
	}
	
    public static void main(String arg[])
    {
//		double[] testgroup = {1,1,2,2};// tyle ile danych do nauki, grupa 1,2,...
//		double[][] testdata = {{0.1,0.1},{1.1,1.1}}; //dane do nauki
        double[][] predata = {{0.0034152369865197746},{0.014361051035126303},{-7.75023921527959}}; // dane do kwalifikacji

        double[][] testdata = new double[3][];
        DataManager dm = new DataManager();
        dm.scanFileToMatrix(System.getProperty("user.dir") +
                            System.getProperty("file.separator") +
                            "res" +
                            System.getProperty("file.separator") +
                            "data.txt", testdata, 3);
		double[][] testgroup = new double[1][];
		dm.scanFileToMatrix(System.getProperty("user.dir") +
                			System.getProperty("file.separator") +
                			"res" +
                			System.getProperty("file.separator") +
                			"group.txt", testgroup, 1);
        
        DiscriminantAnalysis testclass = new DiscriminantAnalysis(testgroup[0], testdata);
        double [][] linearDiscriminants3 = testclass.linearDiscriminants; 
        int[] predictedGroup = testclass.predictedGroup(testgroup[0], testdata, predata);  
        for (int i = 0; i < predictedGroup.length; i++)
        	System.out.println(i+" "+predictedGroup[i]);
        
//        double[][] linearDiscriminants3 = {testclass.linearDiscriminants[0],
//                testclass.linearDiscriminants[1]};        
        out.println(
                "\n\nThe first discriminant based on non-null constructor  = ["
                + dm.roundDigits(linearDiscriminants3[0][0], 3.0) + " , "
                + dm.roundDigits(linearDiscriminants3[0][1], 3.0) + " ]");
//        out.println("The second discriminant based on non-null constructor = ["
//                    + dm.roundDigits(linearDiscriminants3[1][0], 3.0) + " , "
//                    + dm.roundDigits(linearDiscriminants3[1][1], 3.0) + " , ");
    }	

}
