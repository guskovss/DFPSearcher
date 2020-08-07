/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher.commons;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Dialog for choose or save of file.
 * @version 1.1.0 26.02.2020
 * @author Sergey Guskov
 */
public final class FileChooserFactory {
    
    /**
     * Dialog for choose or save of file.
     */
    private static JFileChooser chooser;

    /**
     * Constructor.
     */
    private FileChooserFactory() {
    }

    /**
     * Dialog for choose or save of file.
     * @param type type of dialog
     * @return dialog
     */
    public static JFileChooser getChooser(final int type) {
        createChooser(type);
        return chooser;
    }

    /**
     * Create dialog for choose or save of file.
     * @param type type of dialog
     */
    private static void createChooser(final int type) {
        //Create dialog
        if (chooser == null) {
            //Little redifinition JFileChooser
            chooser = new JFileChooser() {
                @Override
                public void approveSelection() {
                    //Save
                    if (getDialogType() == SAVE_DIALOG) {
                        //If not/uncorrect extension, then show message
                        File file = getSelectedFile();
                        String nf = file.getName();
                        FileNameExtensionFilter ff = (FileNameExtensionFilter) getFileFilter();
                        String ext = "." + ff.getExtensions()[0];
                        if (nf.equals(ext)) {
                            JOptionPane.showMessageDialog(this,
                                    "Некорректное имя файла.",
                                    "Сохранить как", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if (!nf.endsWith(ext)) {
                            JOptionPane.showMessageDialog(this,
                                    "Расширение файла не указано или указано неверно.",
                                    "Сохранить как", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        //Existing file
                        if (file.exists() && (JOptionPane.showConfirmDialog(this,
                                "Файл " + file + " уже существует.\nЗаменить его?",
                                "Сохранить как", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)) {
                            return;
                        }
                    }
                    //Open
                    if (getDialogType() == OPEN_DIALOG) {
                        //Existing file
                        File file = getSelectedFile();
                        if (!file.exists()) {
                            JOptionPane.showMessageDialog(this,
                                    "Файл " + file + " не существует.\nУбедитесь в правильности указанного имени файла.",
                                    "Открыть", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    super.approveSelection();
                }
            };
            chooser.setCurrentDirectory(new File("."));
            chooser.setAcceptAllFileFilterUsed(false);
        }
        chooser.setSelectedFile(new File(""));
        //Delete all filters
        int n = chooser.getChoosableFileFilters().length;
        for (int i = 0; i < n; i++) {
            chooser.removeChoosableFileFilter(chooser.getChoosableFileFilters()[n - 1 - i]);
        }
        //Create filters
        //Filter txt
        if (type == 3) {
            FileNameExtensionFilter curFileFilter = new FileNameExtensionFilter("*.txt", "txt");
            chooser.addChoosableFileFilter(curFileFilter);
            chooser.setFileFilter(curFileFilter);
        }
        //Filter png
        if (type == 5) {
            FileNameExtensionFilter curFileFilter = new FileNameExtensionFilter("*.png", "png");
            chooser.addChoosableFileFilter(curFileFilter);
            chooser.setFileFilter(curFileFilter);
        }
    }
}
