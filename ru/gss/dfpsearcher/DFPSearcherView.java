/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import ru.gss.dfpsearcher.chart.ChartMaker;
import ru.gss.dfpsearcher.calculation.DataTableModel;
import ru.gss.dfpsearcher.chart.DlgParameterChartEdit;
import ru.gss.dfpsearcher.commons.FileChooserFactory;
import ru.gss.dfpsearcher.data.DataList;

/**
 * The main frame of the application.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public class DFPSearcherView extends FrameView {

    static {
        UIManager.put("JXTable.column.horizontalScroll", "Горизонтальная прокрутка");
        UIManager.put("JXTable.column.packAll", "Упаковка всех столбцов");
        UIManager.put("JXTable.column.packSelected", "Упаковка выбранного столбца");
    }

    /**
     * Constructor.
     * @param app application
     */
    public DFPSearcherView(final SingleFrameApplication app) {
        super(app);
        initComponents();

        //Icon
        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.dfpsearcher.DFPSearcherApp.class).getContext().getResourceMap(DFPSearcherView.class);
        //getFrame().setIconImage(resourceMap.getImageIcon("mainFrame.icon").getImage());

        //Translate
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.lookInLabelText", "Папка:");
        UIManager.put("FileChooser.saveInLabelText", "Папка:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Фильтр");
        UIManager.put("FileChooser.upFolderToolTipText", "Наверх");
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.newFolderToolTipText", "Новая папка");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.helpButtonText", "Справка");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.helpButtonToolTipText", "Справка");
        UIManager.put("FileChooser.openDialogTitleText", "Открыть");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить как");
        UIManager.put("ProgressMonitor.progressText", "Загрузка...");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.messageDialogTitle", "Внимание");

        //Main objects
        data = new DataList();

        //Settings of data table
        tmData = new DataTableModel(data);
        jtData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtData.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtData.setModel(tmData);
      
        chartPanel = new ChartPanel(ChartMaker.createChart(data));
        chartPanel.setPopupMenu(jpmChart);
        chartPanel.setMouseWheelEnabled(true);
        jpChart.add(chartPanel);
    }

    /**
     * Text message.
     * @param s message
     */
    private void addToLog(final String s) {
        jtaLog.append(s + "\n");
    }

    /**
     * Existing data.
     */
    private boolean existData = false;

    /**
     * Existing data.
     * @return existing data
     */
    public boolean isExistData() {
        return existData;
    }

    /**
     * Existing data.
     * @param b existing data
     */
    public void setExistData(final boolean b) {
        boolean old = isExistData();
        existData = b;
        firePropertyChange("existData", old, isExistData());
    }

    /**
     * Message of number parse exeptions.
     * @param n count of exeptions
     */
    private void showParseExceptionMessage(final int n) {
        JOptionPane.showMessageDialog(this.getFrame(),
                "Количество ошибок при распознавании чисел - " + n,
                "Внимание", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Message of exeption.
     * @param ex exeption
     */
    public void showErrorMessage(final Exception ex) {
        JOptionPane.showMessageDialog(
                DFPSearcherApp.getApplication().getMainFrame(), ex,
                "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Chart save.
     */
    @Action
    public void acChartSaveAs() {
        JFileChooser ch = FileChooserFactory.getChooser(5);
        if (ch.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try {
                ChartUtilities.saveChartAsPNG(f, chartPanel.getChart(), chartPanel.getWidth(), chartPanel.getHeight());
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Chart parameters.
     */
    @Action
    public void acChartParameter() {
        DlgParameterChartEdit d = new DlgParameterChartEdit();
        d.setTempObj(chartPanel.getChart());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
    }
   
    /**
     * Chart repaint.
     */
    @Action(enabledProperty = "existData")
    public void acPlot() {
        chartPanel.setChart(ChartMaker.createChart(data));
    } 

    /**
     * Open file.
     */
    @Action
    public void acOpenFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(3);
        if (chooser.showOpenDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                data.loadDataFromFile(f);
                addToLog("Открыт файл с данными " + f.getAbsolutePath());
            } catch (FileNotFoundException ex) {
                showErrorMessage(ex);
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
            if (data.getParseExceptionCount() > 0) {
                showParseExceptionMessage(data.getParseExceptionCount());
                return;
            }
            tmData.fireTableDataChanged();
            chartPanel.setChart(ChartMaker.createChart(data));
            setExistData(true);
        }
    }

    /**
     * Calculation.
     */
    @Action(enabledProperty = "existData")
    public void acCalculate() {
        data.calculate();
        addToLog("Координата x точки излома " + String.format(Locale.US, "%.2f", data.getData().get(data.getPointIndex()).getX()));
        String s1 = "+ ";
        if (data.getLine1Q() < 0) {
            s1 = "- ";
        }
        String s2 = "+ ";
        if (data.getLine2Q() < 0) {
            s2 = "- ";
        }
        addToLog("Уравнения аппроксимирующих прямых y = " +
                String.format(Locale.US, "%.2f", data.getLine1P()) + " x " + s1 +
                String.format(Locale.US, "%.2f", Math.abs(data.getLine1Q())) + "; y = " +
                String.format(Locale.US, "%.2f", data.getLine2P()) + " x " + s2 +
                String.format(Locale.US, "%.2f", Math.abs(data.getLine2Q())));
        addToLog("Координата x точки пересечения аппроксимирующих прямых " + String.format(Locale.US, "%.2f", data.getPointX()));
        chartPanel.setChart(ChartMaker.createChart(data));
    }

    /**
     * Action for About button.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            aboutBox = new DFPSearcherAboutBox();
        }
        aboutBox.setLocationRelativeTo(this.getFrame());
        aboutBox.setVisible(true);
    }

    //CHECKSTYLE:OFF
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jpChart = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtData = new org.jdesktop.swingx.JXTable();
        jToolBar1 = new javax.swing.JToolBar();
        jbtnOpen = new javax.swing.JButton();
        jbtnCalculation = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu jmFile = new javax.swing.JMenu();
        jmiOpen = new javax.swing.JMenuItem();
        jmiCalculation = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jmiExit = new javax.swing.JMenuItem();
        javax.swing.JMenu jmHelp = new javax.swing.JMenu();
        javax.swing.JMenuItem jmiAbout = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        jpmChart = new javax.swing.JPopupMenu();
        jmiChartSave = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jmiChartParameters = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(350);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(200, 226));
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jPanel3.setMinimumSize(new java.awt.Dimension(600, 290));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(762, 290));

        jpChart.setName("jpChart"); // NOI18N
        jpChart.setLayout(new javax.swing.BoxLayout(jpChart, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 200));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(250, 128));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtData.setColumnControlVisible(true);
        jtData.setName("jtData"); // NOI18N
        jtData.setSortable(false);
        jScrollPane2.setViewportView(jtData);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar1.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 36));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.dfpsearcher.DFPSearcherApp.class).getContext().getActionMap(DFPSearcherView.class, this);
        jbtnOpen.setAction(actionMap.get("acOpenFile")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.dfpsearcher.DFPSearcherApp.class).getContext().getResourceMap(DFPSearcherView.class);
        jbtnOpen.setIcon(resourceMap.getIcon("jbtnOpen.icon")); // NOI18N
        jbtnOpen.setDisabledIcon(resourceMap.getIcon("jbtnOpen.disabledIcon")); // NOI18N
        jbtnOpen.setFocusable(false);
        jbtnOpen.setHideActionText(true);
        jbtnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnOpen.setName("jbtnOpen"); // NOI18N
        jbtnOpen.setRolloverIcon(resourceMap.getIcon("jbtnOpen.rolloverIcon")); // NOI18N
        jbtnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnOpen);

        jbtnCalculation.setAction(actionMap.get("acCalculate")); // NOI18N
        jbtnCalculation.setIcon(resourceMap.getIcon("jbtnCalculation.icon")); // NOI18N
        jbtnCalculation.setDisabledIcon(resourceMap.getIcon("jbtnCalculation.disabledIcon")); // NOI18N
        jbtnCalculation.setFocusable(false);
        jbtnCalculation.setHideActionText(true);
        jbtnCalculation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnCalculation.setName("jbtnCalculation"); // NOI18N
        jbtnCalculation.setRolloverIcon(resourceMap.getIcon("jbtnCalculation.rolloverIcon")); // NOI18N
        jbtnCalculation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnCalculation);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        jSplitPane1.setTopComponent(jPanel1);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtaLog.setColumns(20);
        jtaLog.setEditable(false);
        jtaLog.setFont(resourceMap.getFont("jtaLog.font")); // NOI18N
        jtaLog.setRows(5);
        jtaLog.setWrapStyleWord(true);
        jtaLog.setName("jtaLog"); // NOI18N
        jScrollPane1.setViewportView(jtaLog);

        jSplitPane1.setRightComponent(jScrollPane1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        jmFile.setText(resourceMap.getString("jmFile.text")); // NOI18N
        jmFile.setName("jmFile"); // NOI18N

        jmiOpen.setAction(actionMap.get("acOpenFile")); // NOI18N
        jmiOpen.setName("jmiOpen"); // NOI18N
        jmFile.add(jmiOpen);

        jmiCalculation.setAction(actionMap.get("acCalculate")); // NOI18N
        jmiCalculation.setName("jmiCalculation"); // NOI18N
        jmFile.add(jmiCalculation);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jmFile.add(jSeparator1);

        jmiExit.setAction(actionMap.get("quit")); // NOI18N
        jmiExit.setName("jmiExit"); // NOI18N
        jmFile.add(jmiExit);

        menuBar.add(jmFile);

        jmHelp.setText(resourceMap.getString("jmHelp.text")); // NOI18N
        jmHelp.setName("jmHelp"); // NOI18N

        jmiAbout.setAction(actionMap.get("showAboutBox")); // NOI18N
        jmiAbout.setName("jmiAbout"); // NOI18N
        jmHelp.add(jmiAbout);

        menuBar.add(jmHelp);

        statusPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setMinimumSize(new java.awt.Dimension(20, 20));
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(567, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jpmChart.setName("jpmChart"); // NOI18N

        jmiChartSave.setAction(actionMap.get("acChartSaveAs")); // NOI18N
        jmiChartSave.setName("jmiChartSave"); // NOI18N
        jpmChart.add(jmiChartSave);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jpmChart.add(jSeparator2);

        jmiChartParameters.setAction(actionMap.get("acChartParameter")); // NOI18N
        jmiChartParameters.setName("jmiChartParameters"); // NOI18N
        jpmChart.add(jmiChartParameters);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbtnCalculation;
    private javax.swing.JButton jbtnOpen;
    private javax.swing.JMenuItem jmiCalculation;
    private javax.swing.JMenuItem jmiChartParameters;
    private javax.swing.JMenuItem jmiChartSave;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiOpen;
    private javax.swing.JPanel jpChart;
    private javax.swing.JPopupMenu jpmChart;
    private org.jdesktop.swingx.JXTable jtData;
    private javax.swing.JTextArea jtaLog;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private DataTableModel tmData;
    private DataList data;
    private ChartPanel chartPanel;
    //CHECKSTYLE:ON
}
