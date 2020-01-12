/*
 * File name: ServerChatUI.java
 * Author:    Alex Carrozzi
 * Date: Dec 6th, 2019 
 * Purpose: Create server UI and run background process
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Creates server UI and runs server backend
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see package server,
 *      java.awt.BorderLayout,java.awt.Color,java.awt.Dimension,java.awt.FlowLayout
 *      java.awt.event.ActionEvent,java.awt.event.ActionListener,java.awt.event.WindowAdapter
 *      java.awt.event.WindowEvent,java.io.IOException,java.io.ObjectOutputStream
 *      java.net.Socket,javax.swing.BorderFactory,javax.swing.JButton,javax.swing.JFrame
 *      javax.swing.JPanel,javax.swing.JScrollPane,javax.swing.JTextArea,javax.swing.JTextField
 *      javax.swing.border.TitledBorder
 * 
 */
public class ServerChatUI extends JFrame implements Accessible{
    /**
     * Send button
     */
    JButton sendButton;
    /**
     * message field
     */
    JTextField message;
    /**
     * Main chat panel
     */
    JTextArea display;
    /**
     * embedded ObjectOutputStream object
     */
    ObjectOutputStream outputStream;
    /**
     * embedded Socket object
     */
    Socket socket;
    /**
     * embedded ConnectionWrapper object
     */
    ConnectionWrapper connection;
    /**
     * embedded Runnable object
     */
    Runnable run;
    
    /**
     * Class constructor initializes socket, launches the backend and 
     * displays the UI
     * @param sck Socket object used to initialize socket field
     */
    public ServerChatUI(Socket sck){
        
        this.socket = sck;
        setFrame(createUI());
        runClient();
        
    }
    
    /**
     * Creates the entire server UI
     * @return a JPanel containing the server UI
     */
    public final JPanel createUI(){
        //Create a JPanel
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        //Create Controller object
        Controller control = new Controller();
        
        //Initialize properties of message button
        message = new JTextField("Type a message");
        message.setPreferredSize(new Dimension(450,25));
        
        //Initialize properties of send button
        sendButton = new JButton("Send");
        sendButton.setEnabled(true);
        sendButton.setMnemonic('S');
        sendButton.setPreferredSize(new Dimension(90,24));
        sendButton.addActionListener(control);
        sendButton.setActionCommand("send");
        //Create a JPanel to contain the components used in the message row
        JPanel messageRow = new JPanel();
        messageRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageRow.add(message);
        messageRow.add(sendButton);
        messageRow.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,10),"MESSAGE"));
        
        //Initialize properties of display TextArea
        display = new JTextArea(30,45);
        display.setEditable(false);
        //Create a JPanel to hold display
        JPanel bottom = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(display);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,10),"CHAT DISPLAY",TitledBorder.CENTER,TitledBorder.CENTER));
        
        //Finally add the two JPanels north and center to pan and return pan
        pan.add(messageRow,BorderLayout.NORTH);
        pan.add(scroll,BorderLayout.CENTER);
        return pan;
        
    }
    
    /**
     * Sets the ServerUI JPanel to the main frame, sets size and adds
     * windowListener, declared final to avoid constructor overrideable method warning
     * @param pan Instance of JPanel containing ServerUI
     */
    public final void setFrame(JPanel pan){
        //Create WindowController object and add it to the frame
        WindowController controller = new WindowController();
        addWindowListener(controller);
        //Set size restrictions
        setMinimumSize(new Dimension(588,500));
        setResizable(false);
        add(pan,BorderLayout.CENTER);
    }
    
    /**
     * Initializes connection field and outputStream field, declared final to avoid constructor warning
     */
    public final void runClient(){
        connection = new ConnectionWrapper(socket);
        try{
            //Use connection to createStreams
            connection.createStreams();
            //Initialize outputStream field
            outputStream = connection.getOutputStream();
            
        }catch(IOException e){
           System.out.println("Run Client in server ERROR");
        }
        //Create a thread by passing runnable reference to the thread
        //constructor and starts the thread
        run = new ChatRunnable<>(this, connection);
        Thread thread = new Thread(run);
        thread.start();
    }
    
    /**
     * Overridden method from Accessible interface, returns display
     * @return TextArea display
     */
    @Override
    public JTextArea getDisplay(){
        return display;
    }
    
    /**
     * Overridden method from Accessible interface,Closes the connection
     */
    @Override
    public void closeChat(){
        if(!(socket.isClosed())){
            try{
                connection.closeConnection();
                //Specifications say must use platform.runlater ?
                //Applys only to FX applications ? 
                //Causes errors commented out
                //Platform.runLater(run);
            }catch(IOException e){
                System.out.println("closechat in server ERROR");
            }
            dispose();
        }
        
    }
    
    /**
     * Inner class used to create window controller for ServerUI
     * @author 
     * @version 1.0
     * @see package server
     * @since 1.8.0_181
     */
    private class WindowController extends WindowAdapter{
        /**
         * Handles windowClosing event
         * @param e WindowEvent 
         */
        @Override
        public void windowClosing( WindowEvent e){
            System.out.println("Server UI Window closing!");
           
            try{
                outputStream.writeObject(ChatProtocolConstants.DISPLACMENT+ChatProtocolConstants.CHAT_TERMINATOR+ChatProtocolConstants.LINE_TERMINATOR);
            }catch(IOException error){
            }
            finally{
                dispose();
            }
            System.out.println("Closing Chat !");
            try{
                connection.closeConnection();
            }catch(IOException error2){
            }
            finally{
                dispose();
            }
            dispose();
            System.out.println("Chat closed!");
           
            
        }
        /**
         * Prints out Server UI Closed to console
         * @param e Window Event
         */
        @Override
        public void windowClosed( WindowEvent e){
            
            System.out.println("Server UI Closed!");
        }
        
    }
    
    /**
     * Inner class used to handle action events of send button
     * @author Alex Carrozzi
     * @version 1.0
     */
    private class Controller implements ActionListener{
        
        /**
         * Overridden actionPerformed to handle send button click
         * @param event ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent event){
            
           String action = event.getActionCommand();
           if(action.equals("send")){
               //Call the private send method
               send();
           }
        }
       /**
        * Gets text from message field and writes it to display
        */
        private void send(){
            //Declare and initialize sendMessage String with messageFields text
            String sendMessage = message.getText();
            display.append(sendMessage+ChatProtocolConstants.LINE_TERMINATOR);
            try{
                outputStream.writeObject(ChatProtocolConstants.DISPLACMENT+sendMessage+ChatProtocolConstants.LINE_TERMINATOR);
            }catch(IOException e){
                System.out.println("send in server ERROR");
            }
            
        }
        
        
    }
    
   
}
