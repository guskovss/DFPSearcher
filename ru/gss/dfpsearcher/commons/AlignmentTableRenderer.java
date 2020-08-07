/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.commons;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for table cell with right horizontal alignment.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class AlignmentTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        super.setHorizontalAlignment(SwingConstants.RIGHT);
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return this;
    }
}
