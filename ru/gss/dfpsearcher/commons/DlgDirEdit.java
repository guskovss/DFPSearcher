/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.commons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXDatePicker;

/**
 * Abstract dialog add/edit.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 * @param <T> тип объекта
 */
public abstract class DlgDirEdit < T > extends DlgParent {

    /**
     * Change object in dialog.
     */
    private boolean changeObj;
    /**
     * Editable object.
     */
    private T tempObj;
    /**
     * Change limits.
     */
    private boolean constrain = true;

    /**
     * Constructor.
     */
    public DlgDirEdit() {
        super();
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                constrain = false;
                showConstrainMessage();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Editable object.
     * @param aTempObj editable object
     */
    public abstract void setTempObj(T aTempObj);

    /**
     * Сreate new editable object.
     * @return new editable object
     */
    public abstract T createTempObj();
    
    /**
     * Сreate new editable object from template.
     * @param aTempObj template
     * @return new editable object
     */
    public T createTempObj(final T aTempObj) {
        return aTempObj;
    }

    /**
     * Editable object.
     * @return editable object
     */
    public T getTempObj() {
        return tempObj;
    }

    /**
     * Editable object.
     * @param aTempObj editable object
     */
    protected void putTempObj(final T aTempObj) {
        tempObj = aTempObj;
        changeObj = false;
    }

    /**
     * Change object in dialog.
     * @param aChangeObj change object in dialog
     */
    public void setChangeObj(final boolean aChangeObj) {
        changeObj = aChangeObj;
        dispose();
    }

    /**
     * Change object in dialog.
     * @return change object in dialog
     */
    public boolean isChangeObj() {
        return changeObj;
    }

    /**
     * Message of delete limits.
     */
    private void showConstrainMessage() {
         JOptionPane.showMessageDialog(
                this, "Проверка допустимого диапазона значений отключена",
                "Внимание", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Message of uncorrect data.
     */
    protected void showUncorrectMessage() {
        JOptionPane.showMessageDialog(
                this, "Данные не введены или введены некорректно",
                "Внимание", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Message of uncorrect limits.
     */
    protected void showNullConstrainMessage() {
        JOptionPane.showMessageDialog(
                this, "Не определены границы допустимого интервала",
                "Внимание", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Message of interval.
     * @param min minimum border of interval
     * @param max maximum border of interval
     */
    protected void showIntervalMessage(final double min, final double max) {
        JOptionPane.showMessageDialog(
                this, "Значение должно находиться между " + String.format("%.2f", min) + " и " + String.format("%.2f", max),
                "Внимание", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Check date combobox.
     * @param jxdp date combobox
     * @return true if date not choise, else false
     */
    protected boolean checkDatePicker(final JXDatePicker jxdp) {
        boolean b = false;
        if (jxdp.getDate() == null) {
            b = true;
            showUncorrectMessage();
            jxdp.requestFocus();
        }
        return b;
    }

    /**
     * Check text field.
     * @param jtf text field
     * @return true if text field is empty, else false
     */
    protected boolean checkTextField(final JTextField jtf) {
        boolean b = jtf.getText().trim().isEmpty();
        if (b) {
           showUncorrectMessage();
           jtf.requestFocus();
        }
        return b;
    }

    /**
     * Check combobox.
     * @param jcb combobox
     * @return true if element not choise, else false
     */
    protected boolean checkComboBox(final JComboBox jcb) {
        boolean b = false;
        if (jcb.getSelectedIndex() < 0) {
            b = true;
            showUncorrectMessage();
            jcb.requestFocus();
        }
        return b;
    }

    /**
     * Get Double value from formatted text field.
     * @param jftf formatted text field
     * @return value
     */
    protected Double getDoubleFromFormattedTextField(final JFormattedTextField jftf) {
        Number n = (Number) jftf.getValue();
        if (n == null) {
            return null;
        } else {
            return n.doubleValue();
        }
    }

    /**
     * Check interval.
     * @param val value
     * @param min minimum border of interval
     * @param max maximum border of interval
     * @return true, if value is not in interval, else false
     */
    protected boolean checkDoubleInInterval(final double val, final Double min, final Double max) {
        if ((min == null) || (max == null)) {
            showNullConstrainMessage();
            return true;
        }
        if (constrain) {
            if ((val < min) || (val > max)) {
                showIntervalMessage(min, max);
                return true;
            }
        }
        return false;
    }

    /**
     * Check formatted text field. Support null.
     * @param jftf formatted text field
     * @param min minimum border of interval
     * @param max maximum border of interval
     * @return true, if value is not in interval, else false
     */
    protected boolean checkFormattedTextFieldNullSupposed(
            final JFormattedTextField jftf, final Double min, final Double max) {
        boolean b = false;
        Double d = getDoubleFromFormattedTextField(jftf);
        if (d != null) {
            if (checkDoubleInInterval(d, min, max)) {
                jftf.requestFocus();
                b = true;
            }
        }
        return b;
    }

    /**
     * Check formatted text field. NOT support null.
     * @param jftf formatted text field
     * @param min minimum border of interval
     * @param max maximum border of interval
     * @return true, if field is empty or value is not in interval, else false
     */
    protected boolean checkFormattedTextFieldNullNoSupposed(
            final JFormattedTextField jftf, final Double min, final Double max) {
        boolean b = false;
        Double d = getDoubleFromFormattedTextField(jftf);
        if (d == null) {
            showUncorrectMessage();
            jftf.requestFocus();
            b = true;
        } else {
            if (checkDoubleInInterval(d, min, max)) {
                jftf.requestFocus();
                b = true;
            }
        }
        return b;
    }

    /**
     * Create negative value. Support null.
     * @param v value
     * @return negative value
     */
    protected Double negativeDouble(final Double v) {
        if (v == null) {
            return null;
        }
        return -v;
    }
}
