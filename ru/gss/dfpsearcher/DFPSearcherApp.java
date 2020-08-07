/*
 * Dependence Fracture Point Searcher
 */
package ru.gss.dfpsearcher;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 * @version 1.1.0 06.04.2020
 * @author Sergey Guskov
 */
public class DFPSearcherApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new DFPSearcherView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * @param root root
     */
    @Override protected void configureWindow(final java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of DFPSearcherApp
     */
    public static DFPSearcherApp getApplication() {
        return Application.getInstance(DFPSearcherApp.class);
    }

    /**
     * Main method launching the application.
     * @param args args
     */
    public static void main(final String[] args) {
        launch(DFPSearcherApp.class, args);
    }
}
