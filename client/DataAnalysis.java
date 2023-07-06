package client;

import java.io.Serializable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import compute.Task;

public class DataAnalysis implements Task<JFreeChart>, Serializable{
    
    private DefaultCategoryDataset dataset;
    
    @Override
    public JFreeChart execute() {
        System.out.println("\n\nExecuting Data Analysis...");
        JFreeChart chart = createLineChart(dataset);

        if (chart == null) {
            System.out.println("Error creating chart.");
            return null;
        }
        System.out.println("Data Analysis executed.\n\n");
        return chart;
    }

    public DataAnalysis(DefaultCategoryDataset dataset) {
        this.dataset = dataset;
    }

    private static JFreeChart createLineChart(DefaultCategoryDataset dataset) {

        //calculate mean year consumption
        double[] mean = new double[dataset.getColumnCount()];
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            double sum = 0;
            for (int j = 0; j < dataset.getRowCount(); j++) {
                sum += dataset.getValue(j, i).doubleValue();
            }
            mean[i] = sum / dataset.getRowCount();
        }

        //calculate standard deviation
        double[] standardDeviation = new double[dataset.getColumnCount()];
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            double sum = 0;
            for (int j = 0; j < dataset.getRowCount(); j++) {
                sum += Math.pow(dataset.getValue(j, i).doubleValue() - mean[i], 2);
            }
            standardDeviation[i] = Math.sqrt(sum / dataset.getRowCount());
        }

        //add mean and standard deviation to dataset
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            int year = 1990 + i;
            dataset.addValue(mean[i], "Mean", String.valueOf(year));
            dataset.addValue(standardDeviation[i], "Standard Deviation", String.valueOf(year));
        }

        //create the chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Consumption Mean and Standard Deviation", // chart title
                "Year", // domain axis label
                "Consumption", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        );
        
        return chart;
    }
}
