/*
 * File name: ChatRunnable.java
 * Author:    Alex Carrozzi
 * Date: Dec 6th, 2019 
 * Purpose: Provides a thread for Server and Client
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Java class that implements runnable interface, implements own behaviour of run method
 * @author Alex Carrozzi
 * @version 1.0
 * @param <T> 
 * @since 1.8.0_181
 * @see java.io.IOException,java.io.ObjectInputStream,java.io.ObjectOutputStream
 *      java.net.Socket,java.time.LocalDateTime,java.time.format.DateTimeFormatter
 *      javax.swing.JFrame,javax.swing.JTextArea
 * 
 */
public class ChatRunnable <T extends JFrame & Accessible> implements Runnable{
    
    /**
     * Generic ui
     */
    final T ui;
    /**
     * Socket object
     */
    final Socket socket;
    /**
     * ObjectInputStream object
     */
    final ObjectInputStream inputStream;
    /**
     * ObjectOutputStream object
     */
    final ObjectOutputStream outputStream;
    /**
     * JTextArea object
     */
    final JTextArea display;
    
    /**
     * Class constructor, initializes fields
     * @param ui initializes ui field
     * @param connection initializes connection field
     */
    public ChatRunnable(T ui, ConnectionWrapper connection){
        this.ui = ui;
        this.socket = connection.getSocket();
        this.inputStream = connection.getInputStream();
        this.outputStream = connection.getOutputStream();
        this.display = ui.getDisplay();
    }
    
    /**
     * Overrides run method to implement own behaviour 
     */
    @Override
    public void run(){
        //Declaring string strin
        String strin;
        //Trimed version of strin
        String strinTrim;
        
        while(true){
            //If socket is not closed
            if(!socket.isClosed()){
                try{
                    //Use inputStream to read an object and assign it to strin
                    strin = (String)inputStream.readObject();
                    strinTrim = strin.trim();
                    if(socket.isClosed()){
                        break;
                    }
                    
                    LocalDateTime timeNow = LocalDateTime.now();
                    DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("MMMM dd, HH:mm a");
                    String timeFormatted = dtformat.format(timeNow);
                    
                    //Compare to CHAT_TERMINATOR constant
                    //If message reads bye
                    if(strinTrim.equals(ChatProtocolConstants.CHAT_TERMINATOR)){
                        final String terminate;
                        terminate = ChatProtocolConstants.DISPLACMENT +timeFormatted+ ChatProtocolConstants.LINE_TERMINATOR+strin;
                        display.append(terminate);
                        break;
                    }
                    else{
                        final String append;
                        append = ChatProtocolConstants.DISPLACMENT+ timeFormatted + ChatProtocolConstants.LINE_TERMINATOR+strin;
                        display.append(append);
                    }  
                }catch(IOException | ClassNotFoundException e){
                  //exceptions will cause loop to break
                  break;
                }
            }
            else{
                break;
            }
           
        }
        if(!socket.isClosed()){
            try{
                outputStream.writeBytes(ChatProtocolConstants.DISPLACMENT+ChatProtocolConstants.CHAT_TERMINATOR+ChatProtocolConstants.LINE_TERMINATOR);
                ui.closeChat();
            }catch(IOException e){
                System.out.println("ChatRunnable run Error");
            }
        }
        
        //Finally, before method returns, ui calls closeChat()
        //ui.closeChat();
        
    }
    
    
}
