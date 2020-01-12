/*
 * File name: Accessible.java
 * Author:    Alex Carrozzi
 * Date: Dec 6, 2019 
 * Purpose: Interface used by the Client and Server UIs, must implement
 *            there own getDisplay and closeChat definitions
 */

import javax.swing.JTextArea;

/**
 * Interface for UI
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see javax.swing.JTextArea 
 */
public interface Accessible {
    /**
     * Method must return a JTextArea
     * @return JTextArea
     */
    JTextArea getDisplay();
    /**
     * Method must return nothing and be overridden 
     */
    void closeChat();
    
}