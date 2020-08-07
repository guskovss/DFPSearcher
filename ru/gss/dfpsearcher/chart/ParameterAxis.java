/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.chart;

/**
 * Parameters of chart axis.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class ParameterAxis {
    
    /**
     * Minimum value.
     */
    private double min;
    /**
     * Maximum value.
     */
    private double max;
    /**
     * Scale step.
     */
    private double step;
    /**
     * Autochois of range.
     */
    private boolean autoRange;
    /**
     * Autochois of scale step.
     */
    private boolean autoStep;
   
    /**
     * Constructor.
     */
    public ParameterAxis() {
        min = 0;
        max = 1;
        step = 0.1;
        autoRange = false;
        autoStep = false;
    }

    /**
     * Minimum value.
     * @return minimum value
     */
    public double getMin() {
        return min;
    }

    /**
     * Minimum value.
     * @param aMin minimum value to set
     */
    public void setMin(final double aMin) {
        min = aMin;
    }

    /**
     * Maximum value.
     * @return maximum value
     */
    public double getMax() {
        return max;
    }

    /**
     * Maximum value.
     * @param aMax maximum value to set
     */
    public void setMax(final double aMax) {
        max = aMax;
    }

    /**
     * Scale step.
     * @return scale step
     */
    public double getStep() {
        return step;
    }

    /**
     * Scale step.
     * @param aStep scale step to set
     */
    public void setStep(final double aStep) {
        step = aStep;
    }

    /**
     * Autochois of range.
     * @return autochois of range
     */
    public boolean isAutoRange() {
        return autoRange;
    }

    /**
     * Autochois of range.
     * @param aAutoRange autochois of range to set
     */
    public void setAutoRange(final boolean aAutoRange) {
        autoRange = aAutoRange;
    }

    /**
     * Autochois of scale step.
     * @return autochois of scale step
     */
    public boolean isAutoStep() {
        return autoStep;
    }

    /**
     * Autochois of scale step.
     * @param aAutoStep autochois of scale step to set
     */
    public void setAutoStep(final boolean aAutoStep) {
        autoStep = aAutoStep;
    }
}
