/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.data;

/**
 * Point.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class DataLine {

    /**
     * Coordinate x.
     */
    private Double x;
    /**
     * Coordinate y.
     */
    private Double y;
    /**
     * Addition parameter.
     */
    private Double d;
    
    /**
     * Constructor.
     */
    public DataLine() {
        x = 0.0;
        y = 0.0;
    }

    /**
     * Coordinate x.
     * @return coordinate x
     */
    public Double getX() {
        return x;
    }

    /**
     * Coordinate x.
     * @param aX coordinate x
     */
    public void setX(final Double aX) {
        x = aX;
    }
    
    /**
     * Coordinate y.
     * @return coordinate y
     */
    public Double getY() {
        return y;
    }

    /**
     * Coordinate y.
     * @param aY coordinate y
     */
    public void setY(final Double aY) {
        y = aY;
    }

     /**
     * Addition parameter.
     * @return addition parameter
     */
    public Double getD() {
        return d;
    }

    /**
     * Addition parameter.
     * @param aD addition parameter
     */
    public void setD(final Double aD) {
        d = aD;
    }
}
