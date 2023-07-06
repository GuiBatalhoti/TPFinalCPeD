package client;

import java.awt.BasicStroke;
import java.io.Serializable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
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
        JFreeChart chart = ChartFactory.createLineChart(
                "Coffee Consumption Latin America",
                "Year",
                "Total Domestic Consumption",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        return chart;
    }
}
