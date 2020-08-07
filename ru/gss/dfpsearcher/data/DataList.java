/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * List of point.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class DataList {
    
    /**
     * List of point.
     */
    private ArrayList<DataLine> data;

    /**
     * Count of parse exeptions.
     */
    private int parseExceptionCount;

    /**
     * Index of fracture point.
     */
    private int pointIndex;

    /**
     * Coordinate x of fracture point.
     */
    private double pointX;

    /**
     * Coefficient 1 for approximate line 1.
     */
    private double line1P;

    /**
     * Coefficient 2 for approximate line 1.
     */
    private double line1Q;

    /**
     * Coefficient 1 for approximate line 2.
     */
    private double line2P;

    /**
     * Coefficient 2 for approximate line 2.
     */
    private double line2Q;

    /**
     * Constructor.
     */
    public DataList() {
        data = new ArrayList<DataLine>();
        parseExceptionCount = 0;
    }

    /**
     * Parse double value.
     * @param s string representation of double value
     * @return double value or null
     */
    private Double parseDouble(final String s) {
        if (s.trim().isEmpty()) {
            return null;
        }
        if (s.equals("-")) {
            return null;
        }
        try {
            String ss = s.replaceAll(",", ".");
            return Double.valueOf(ss);
        } catch (NumberFormatException ex) {
            parseExceptionCount++;
            return null;
        }
    }

    /**
     * Load data from file.
     * @param file file
     * @throws java.io.IOException exception
     */
    public void loadDataFromFile(final File file) throws IOException {
        BufferedReader reader = null;
        try {
            //Read all lines from file
            reader = new BufferedReader(new FileReader(file));
            ArrayList<String> strings = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
            data.clear();
            pointIndex = 0;
            pointX = 0;
            line1P = 0;
            line1Q = 0;
            line2P = 0;
            line2Q = 0;
            parseExceptionCount = 0;
            for (int i = 0; i < strings.size(); i++) {
                DataLine dl = new DataLine();
                String[] s = strings.get(i).split("\t");
                int columnCount = s.length;
                if (columnCount > 0) {
                    dl.setX(parseDouble(s[0]));
                }
                if (columnCount > 1) {
                    dl.setY(parseDouble(s[1]));
                }
                data.add(dl);
            }
        } finally {
            reader.close();
        }
    }

    /**
     * Calculation.
     */
   public void calculate() {
       //Search fracture point
       double x1 = data.get(0).getX();
       double y1 = data.get(0).getY();
       double xn = data.get(data.size() - 1).getX();
       double yn = data.get(data.size() - 1).getY();
       double s = 0;
       int m = 0;
       for (int i = 0; i < data.size(); i++) {
           double xi = data.get(i).getX();
           double yi = data.get(i).getY();
           double d = (xi - x1) * (yn - yi) - (yi - y1) * (xn - xi);
           if (d > s) {
               s = d;
               m = i;
           }
           data.get(i).setD(d);
       }
       pointIndex = m;
       
       //Calculate coefficients for approximate lines
       double sx = 0;
       double sy = 0;
       double sxy = 0;
       double sx2 = 0;
       for (int i = 0; i < m + 1; i++) {
           sx = sx + data.get(i).getX();
           sy = sy + data.get(i).getY();
           sxy = sxy + data.get(i).getX() * data.get(i).getY();
           sx2 = sx2 + data.get(i).getX() * data.get(i).getX();
       }
       line1P = ((m + 1) * sxy - sx * sy) / ((m + 1) * sx2 - sx * sx);
       line1Q = (sx2 * sy - sx * sxy) / ((m + 1) * sx2 - sx * sx);
       int n = data.size();

       sx = 0;
       sy = 0;
       sxy = 0;
       sx2 = 0;
       for (int i = m; i < n; i++) {
           sx = sx + data.get(i).getX();
           sy = sy + data.get(i).getY();
           sxy = sxy + data.get(i).getX() * data.get(i).getY();
           sx2 = sx2 + data.get(i).getX() * data.get(i).getX();
       }
       line2P = ((n - m) * sxy - sx * sy) / ((n - m) * sx2 - sx * sx);
       line2Q = (sx2 * sy - sx * sxy) / ((n - m) * sx2 - sx * sx);

       //Calculate coordinate x of fracture point
       pointX = (getLine2Q() - getLine1Q()) / (getLine1P() - getLine2P());
   }

    /**
     * Create dataset for chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetData() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries(1);
        for (int j = 0; j < data.size(); j++) {
            series1.add(data.get(j).getX(), data.get(j).getY());
        }
        dataset.addSeries(series1);

        if (pointIndex > 0) {
            XYSeries series2 = new XYSeries(2);
            double dx = 0.1 * (data.get(data.size() - 1).getX() - data.get(0).getX());
            double x = data.get(0).getX();
            double y = getLine1P() * x + getLine1Q();
            series2.add(x, y);
            x = pointX + dx;
            y = getLine1P() * x + getLine1Q();
            series2.add(x, y);
            dataset.addSeries(series2);

            XYSeries series3 = new XYSeries(3);
            x = pointX - dx;
            y = getLine2P() * x + getLine2Q();
            series3.add(x, y);
            x = data.get(data.size() - 1).getX();
            y = getLine2P() * x + getLine2Q();
            series3.add(x, y);
            dataset.addSeries(series3);
        }
        return dataset;
    }

    /**
     * Count of parse exeptions.
     * @return count of parse exeptions
     */
    public int getParseExceptionCount() {
        return parseExceptionCount;
    }

    /**
     * List of point.
     * @return list of point
     */
    public ArrayList<DataLine> getData() {
        return data;
    }

    /**
     * Index of fracture point.
     * @return index of fracture point
     */
    public int getPointIndex() {
        return pointIndex;
    }

    /**
     * Coordinate x of fracture point.
     * @return coordinate x of fracture point
     */
    public double getPointX() {
        return pointX;
    }

    /**
     * Coefficient 1 for approximate line 1.
     * @return coefficient 1 for approximate line 1
     */
    public double getLine1P() {
        return line1P;
    }

    /**
     * Coefficient 2 for approximate line 1.
     * @return coefficient 2 for approximate line 1
     */
    public double getLine1Q() {
        return line1Q;
    }

    /**
     * Coefficient 1 for approximate line 2.
     * @return coefficient 1 for approximate line 2
     */
    public double getLine2P() {
        return line2P;
    }

    /**
     * Coefficient 2 for approximate line 2.
     * @return coefficient 2 for approximate line 2
     */
    public double getLine2Q() {
        return line2Q;
    } 
}
