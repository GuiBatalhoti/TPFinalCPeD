package client;

import java.io.Serializable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.DefaultXYDataset;


import compute.Task;

public class DataAnalysis implements Task<JFreeChart>, Serializable{
    
    private XYDataset dataset;

    public DataAnalysis(XYDataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public JFreeChart execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}
