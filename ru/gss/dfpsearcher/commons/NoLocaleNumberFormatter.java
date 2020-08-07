/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.commons;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import java.text.ParseException;
import java.util.Locale;

/**
 * Formatter for double numbers.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class NoLocaleNumberFormatter extends NumberFormatter {

    /**
     * Count of decimal symbols.
     */
    private int format;

    /**
     * Constructor.
     */
    public NoLocaleNumberFormatter() {
        super(NumberFormat.getNumberInstance());
        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols dfs = fmt.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        fmt.setDecimalFormatSymbols(dfs);
        super.setFormat(fmt);
        format = 0;
    }

    /**
     * Constructor.
     * @param aFormat count of decimal symbols
     */
    public NoLocaleNumberFormatter(final int aFormat) {
        super(NumberFormat.getNumberInstance());
        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols dfs = fmt.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        fmt.setDecimalFormatSymbols(dfs);
        super.setFormat(fmt);
        format = aFormat;
    }

    /**
     * Convert value to string.
     * @param value value
     * @return string representation of value
     * @throws java.text.ParseException parse exception
     */
    @Override
    public String valueToString(final Object value) throws ParseException {
        Number v = (Number) value;
        if (v == null) {
            return null;
        }
        return String.format(Locale.US, "%." + format + "f", v.doubleValue());
    }

    /**
     * Convert string to value.
     * @param text string representation of value
     * @return value
     * @throws java.text.ParseException parse exception
     */
    @Override
    public Object stringToValue(final String text) throws ParseException {
        if (text == null || text.isEmpty()) {
            return null;
        } else {
            String s = text.replaceAll(",", ".");
            return super.stringToValue(s);
        }
    }
}
