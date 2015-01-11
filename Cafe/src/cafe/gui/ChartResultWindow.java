package cafe.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
//import org.jfree.util.Rotation;


public class ChartResultWindow {

  private static final long serialVersionUID = 1L;
  private ChartPanel chartPanel;
  public ChartResultWindow(String applicationTitle) {
      // super(applicationTitle);        
    }
  
	public void open(double[] table, String chartTitle, String xTitle, String yTitle) {
		createContents(table, chartTitle, xTitle, yTitle);
      //  pack();
       // setVisible(true);
		
	}

	protected void createContents(double[] table, String chartTitle, String xTitle, String yTitle) {
        // This will create the dataset 
        final XYDataset  dataset = createDataset(table);        
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, chartTitle,  xTitle, yTitle);
        
        // we put the chart into a panel
       chartPanel = new ChartPanel(chart);
       //chartPanel.setDoubleBuffered(true);
        // default size
      //  chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        // add it to our application
    //    setContentPane(chartPanel);
        //add(chartPanel);
    //   setSize(800,600);
       chartPanel.setSize(100,100);
				
	}	

    public ChartPanel GiveChartPanel(){
    	//chartPanel.setDoubleBuffered(true);
    	chartPanel.revalidate();
    	return chartPanel;
    }
    
/** * Creates a sample dataset */

    private  XYDataset createDataset(double[] table) {
    	
        final XYSeries series1 = new XYSeries("First");        
		for (int i=0; i<table.length;i++)
	        series1.add(i, table[i]);			
        
    	final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        
        return dataset;        
    }
    
    
/** * Creates a chart */

    private JFreeChart createChart(XYDataset  dataset, String title, String xTitle, String yTitle) {
        
        JFreeChart chart = ChartFactory.createXYLineChart(
        	title,			        // chart title
        	xTitle,                    // domain axis label
        	yTitle,                   // range axis label
            dataset,                // data
            PlotOrientation.VERTICAL,  // orientation
            false,                   // include legend
            true,					// tooltips
            false);					// urls

/*        
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
*/

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(240,240,240));
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.darkGray);
        plot.setRangeGridlinePaint(Color.darkGray);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                        
        return chart;
        
    }


		      
}

