/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import ru.gss.dfpsearcher.data.DataList;

/**
 * Chart.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public final class ChartMaker {

    /**
     * Constructor.
     */
    private ChartMaker() {
    }

    /**
     * Create chart.
     * @param dataset data
     * @param labelX name of axis x
     * @param labelY name of axis y
     * @param isStepPlot step chart
     * @return chart
     */
    public static JFreeChart createChart(final XYDataset dataset, final String labelX, final String labelY, final boolean isStepPlot) {
        XYPlot plot = createPlot(dataset, labelX, labelY, isStepPlot);
        JFreeChart chart = new JFreeChart("", plot);
        //Settings
        chart.setBackgroundPaint(Color.white);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.getLegend().setBorder(0, 0, 0, 0);
        return chart;  
    }

    /**
     * Create plot.
     * @param dataset data
     * @param labelX name of axis x
     * @param labelY name of axis y
     * @param isStepPlot step chart
     * @return plot
     */
    private static XYPlot createPlot(final XYDataset dataset, 
            final String labelX, final String labelY, final boolean isStepPlot) {
        NumberAxis xAxis = new NumberAxis(labelX);
        xAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        xAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        xAxis.setLowerMargin(0);
        xAxis.setUpperMargin(0);
        xAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        NumberAxis yAxis = new NumberAxis(labelY);
        yAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        yAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        yAxis.setLowerMargin(0);
        yAxis.setUpperMargin(0);
        yAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        
        //Parameters of series
        XYLineAndShapeRenderer renderer;
        if (isStepPlot) {
            renderer = new XYStepRenderer();
        } else {
            renderer = new XYLineAndShapeRenderer();
            for (int i = 0; i < 3; i++) {
                ((XYLineAndShapeRenderer) renderer).setSeriesShapesVisible(i, false);
            }
        }
        //Colors
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        
        //Tooltip and shapes of points
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesLinesVisible(0, false);
        for (int i = 0; i < 3; i++) {
            renderer.setSeriesToolTipGenerator(i, new StandardXYToolTipGenerator("{1}; {2}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
            //renderer.setSeriesShapesVisible(i, false);
        }
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.darkGray);
        plot.setRangeGridlinePaint(Color.darkGray);
        //plot.getRangeAxis().setAutoRangeMinimumSize(0.2);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        return plot;
    }

    /**
     * Create chart.
     * @param data data
     * @return chart
     */
    public static JFreeChart createChart(final DataList data) {
        JFreeChart chart = null;    
        NumberAxis domainAxis = new NumberAxis("x");
        domainAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        domainAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        domainAxis.setTickMarksVisible(false);
        domainAxis.setLowerMargin(0);
        domainAxis.setUpperMargin(0);
        XYPlot plot = createPlot(data.createDatasetData(), "", "", false);
        chart = new JFreeChart("", plot);
        chart.setBackgroundPaint(Color.white);
        chart.getLegend().setVisible(false);
        chart.setPadding(new RectangleInsets(10, 10, 10, 20));
        return chart;
    }
}
