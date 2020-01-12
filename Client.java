/*
 * File name: Client.java
 * Author:    Alex Carrozzi 
 * Date: Dec 6th 2019 
 * Purpose: Contains the main method used to launch client chat UI
 */


import java.awt.Dimension;
import java.awt.EventQueue;


/**
 * Contains main method of client 
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see java.awt.Dimension,java.awt.EventQueue
 */
public class Client {
    
    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[]args){
        
        EventQueue.invokeLater(new Runnable(){
            
            @Override
            public void run(){
                
                ClientChatUI chat = new ClientChatUI("Andy's ClientChatUI");
                
                chat.setMinimumSize(new Dimension(588,500));
                chat.setResizable(false);
                chat.setLocationByPlatform(true);
                chat.setVisible(true);
                
            }
        });
    }
    
}
