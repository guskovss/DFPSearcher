/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.calculation;

import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import ru.gss.dfpsearcher.data.DataList;
import ru.gss.dfpsearcher.data.DataLine;

/**
 * Model of data table.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class DataTableModel extends AbstractTableModel {

    /**
     * Data.
     */
    private DataList data;
    /**
     * Headers of table columns.
     */
    private String[] colNames = {"x", "y"};
 
    /**
     * Constructor.
     * @param aData data
     */
    public DataTableModel(final DataList aData) {
        data = aData;
    }

    /**
     * Header of table column.
     * @param column index of table column
     * @return header of table column
     */
    @Override
    public String getColumnName(final int column) {
        return colNames[column];
    }

    /**
     * Count of table column.
     * @return count of table column
     */
    public int getColumnCount() {
        return 2;
    }

    /**
     * Count of table row.
     * @return count of table row
     */
    public int getRowCount() {
        return data.getData().size();
    }

    /**
     * Class of table column.
     * @param columnIndex index of table column
     * @return class of table column
     */
    @Override
    public Class < ? > getColumnClass(final int columnIndex) {
        return String.class;
    }

    /**
     * Convertation number to string.
     * @param value number
     * @return string representation of number
     */
    private String convertToString(final Double value) {
        if (value == null) {
            return "";
        }
        return String.format(Locale.US, "%.2f", value);
    }

    /**
     * Value of table cell.
     * @param rowIndex index of table row
     * @param columnIndex index of table column
     * @return value of table cell
     */
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        DataLine o = data.getData().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return convertToString(o.getX());
            case 1:
                return convertToString(o.getY());
            default:
                return null;
        }
    }
}
