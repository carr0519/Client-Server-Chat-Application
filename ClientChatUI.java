/*
 * File name: ClientChatUI.java
 * Author:    Alex Carrozzi 
 * Date: Dec 6th 2019 
 * Purpose: Create client UI and run background process
 */

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see java.awt.BorderLayout,java.awt.Color,javax.swing.BorderFactory,
 *      javax.swing.JButton,javax.swing.JComboBox,javax.swing.JFrame,
 *      javax.swing.JLabel,javax.swing.JPanel,javax.swing.JTextArea,javax.swing.JTextField,
 *      javax.swing.border.TitledBorder,javax.swing.border.TitledBorder,java.awt.Dimension.
 *      java.awt.FlowLayout,java.awt.Insets,java.awt.event.ActionEvent,
 *      java.awt.event.ActionListener,java.awt.event.WindowAdapter,java.awt.event.WindowEvent
 *      java.io.IOException,java.io.ObjectOutputStream,java.net.InetSocketAddress,
 *      java.net.Socket,java.net.UnknownHostException,javax.swing.JScrollPane
 *      javax.swing.border.EmptyBorder
 * 
 */
public class ClientChatUI extends JFrame implements Accessible{
    /**
     * Message field
     */
    JTextField message;
    /**
     * Host port field
     */
    JTextField hostField;
    /**
     * Send button
     */
    JButton sendButton;
    /**
     * Connect button
     */
    JButton connectButton;
    /**
     * Combo box
     */
    JComboBox<String> combo;
    /**
     * Main display
     */
    JTextArea display;
    /**
     * Embedded ObjectOutputStream object
     */
    ObjectOutputStream outputStream;
    /**
     * Embedded Socket object
     */
    Socket socket;
    /**
     * Embedded ConnectionWrapper object
     */
    ConnectionWrapper connection;
    
    /**
     * Class constructor sets title and starts background process
     * @param name 
     */
    public ClientChatUI(String name){
        setTitle(name);
        runClient();
    }
    
    /**
     * Creates the entire UI of the client and returns it inside a JPanel
     * @return An instance of JPanel containing client UI 
     */
    public JPanel createClientUI(){
        //Main JPanel
        JPanel pan = new JPanel(new BorderLayout());
        Controller control = new Controller();
        
        //Create and set properties of host label
        JLabel host = new JLabel("Host: ");
        host.setPreferredSize(new Dimension(35,30));
        host.setDisplayedMnemonic('H');
        //Initialize and set properties of host textfield
        hostField = new JTextField("localhost");
        hostField.setMargin(new Insets(0,5,0,0));
        hostField.setCaretPosition(0);
        hostField.setPreferredSize(new Dimension(505,25));
        addWindowListener( new WindowAdapter(){
            @Override
            public void windowOpened( WindowEvent e){
                hostField.requestFocusInWindow();
            }
        });
        //Create a JPanel to contain first row of components
        JPanel hostRow = new JPanel();
        host.setLabelFor(hostField);
        hostRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        hostRow.add(host);
        hostRow.add(hostField);
        hostRow.setBorder(new EmptyBorder(0,1,0,0));
        
        //Create and set properties of port label
        JLabel port = new JLabel("Port: ");
        port.setPreferredSize(new Dimension(35,30));
        port.setDisplayedMnemonic('P');
        //Initialize and set the properties of connect button
        connectButton = new JButton("Connect");
        connectButton.setOpaque(true);
        connectButton.setBackground(Color.RED);
        connectButton.setMnemonic('C');
        connectButton.setPreferredSize(new Dimension(120,25));
        connectButton.addActionListener(control);
        connectButton.setActionCommand("connect");
        
        String ports[] = new String[] {" ", "8089", "65000", "65535"};
        //Initialize and set the properties of combo box
        combo = new JComboBox<>(ports);
        combo.setPreferredSize(new Dimension(120,25));
        combo.setEditable(true);
        combo.setBackground(Color.WHITE);
        //Create a JPanel to contain second row of components
        JPanel portRow = new JPanel();
        portRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        port.setLabelFor(combo);
        portRow.add(port);
        portRow.add(combo);
        portRow.add(connectButton);
        
        //Initialize and set properties of message field
        message = new JTextField("Type a message");
        message.setPreferredSize(new Dimension(450,25));
        //Initialize and set properties of send button
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.setMnemonic('S');
        sendButton.setPreferredSize(new Dimension(90,24));
        sendButton.addActionListener(control);
        sendButton.setActionCommand("send");
        //Create a JPanel to contain third row of components
        JPanel messageRow = new JPanel();
        messageRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageRow.add(message);
        messageRow.add(sendButton);
        
        //topHalf JPanel contains both the top and msg panels
        JPanel topHalf = new JPanel();
        topHalf.setLayout(new BorderLayout());
        //top contains the first and second row JPanels
        JPanel top = new JPanel(new BorderLayout());
        top.add(hostRow,BorderLayout.NORTH);
        top.add(portRow,BorderLayout.SOUTH);
        top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,10),"CONNECTION"));
        //msg panel contains only the third message row
        JPanel msg = new JPanel(new BorderLayout());
        msg.add(messageRow,BorderLayout.CENTER);
        msg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,10),"MESSAGE"));
        //Add the top panel and msge panel
        topHalf.add(top,BorderLayout.NORTH);
        topHalf.add(msg,BorderLayout.SOUTH);
        
        //Initialize and set properties of display
        display = new JTextArea(30,45);
        display.setEditable(false);
        //bottom panel contains only the display textarea
        JPanel bottom = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(display);
        bottom.add(scroll);
        bottom.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,10),"CHAT DISPLAY",TitledBorder.CENTER,TitledBorder.CENTER));
        //Finally add the tophalf and bottom half to main JPanel pan
        pan.add(topHalf,BorderLayout.NORTH);
        pan.add(bottom,BorderLayout.CENTER);
        return pan;
        
    }
    
    /**
     * Adds WindowController and adds Panel to JFrame
     */
    private void runClient(){
        //Create new windowController
        WindowController controller = new WindowController();
        JPanel pan = createClientUI();
        addWindowListener(controller);
        add(pan,BorderLayout.CENTER);
        
    }
    
    /**
     * Overridden method from Accessible interface, returns display
     * @return JTextArea display
     */
    @Override
    public JTextArea getDisplay(){
        return display;
    }
    
    /**
     * If the socket is not closed it tries to close it
     */
    @Override
    public void closeChat(){
        if(!socket.isClosed()){
            try{
                connection.closeConnection();
                socket.close();
            }catch(IOException e){
                System.out.println("closeChat ERROR");
            }
            //Enable the connect button
            enableConnectButton();
        }
    }
    
    /**
     * Enables connect button 
     */
    private void enableConnectButton(){
        connectButton.setEnabled(true);
        connectButton.setBackground(Color.RED);
        sendButton.setEnabled(false);
        hostField.requestFocus();
    }
    
   
    /**
     * Inner class handles window events
     */
    private class WindowController extends WindowAdapter{
        
        @Override
        public void windowClosing(WindowEvent e){
            
            try{
                if((!socket.isClosed())){
                    outputStream.writeObject(ChatProtocolConstants.CHAT_TERMINATOR);
                }
                //If exception occurs call System.exit(0) 
            }catch(Exception error){
               System.exit(0);
            }
            System.exit(0); 
        }
    }
    
    /**
     * Inner controller class handles the button requests
     */
    private class Controller implements ActionListener{
        
        /**
         * Handles connect button action
         * @param host current text in host field
         * @param port current port number in combo box
         * @return true or false, whether we have initialized our socket
         *          connection and outputStream
         */
        public boolean connect(String host, int port){
            
            try{
                //40 seconds until the server friend closes
                int timeout = 40000;
                socket = new Socket();
                //Create a time-out socket
                socket.connect(new InetSocketAddress(host,port),timeout);
                socket.setSoTimeout(timeout);
                if(socket.getSoLinger() != -1){
                    socket.setSoLinger(true,5);
                }
                if(!socket.getTcpNoDelay()){
                    socket.setTcpNoDelay(true);
                }
                //Append connection data
                display.append(String.format("Connected to Socket [addr=%s, port=%d, localPort=%d ] \n", socket.getInetAddress(),socket.getPort(),socket.getLocalPort()));
                connection = new ConnectionWrapper(socket);
                connection.createStreams();
                outputStream = connection.getOutputStream();
                return true;
            }
            catch(UnknownHostException e){
                display.append("Unknown Host ERROR\n");
                return false;
            }
            catch(IOException | IllegalArgumentException e){
                display.append("ERROR: Connection refused: server is not available. Check port or restart server.\n");
                return false;
            }
            
          
        }
        
        /**
         * Appends message fields text than uses outputStream to write a string
         * object
         */
        private void send(){
            //Gets text from message textfield
           // System.out.println("We are in send method client");
            String sendMessage = message.getText();
            display.append(sendMessage+ChatProtocolConstants.LINE_TERMINATOR);
            try{
                outputStream.writeObject(ChatProtocolConstants.DISPLACMENT+sendMessage+ChatProtocolConstants.LINE_TERMINATOR);
            }catch(IOException e){
                enableConnectButton();
                display.append("Unable to send");
               
            }
        }   
        
        /**
         * Responsible for handing connect button and send button events
         * @param event Action event argument
         */
        @Override
        public void actionPerformed(ActionEvent event)
        {
            boolean connected = false;
            String action = event.getActionCommand();
            int port;
            if(action.equals("connect")){
                String host = hostField.getText();
                
                //If current selected item is empty, prompt user and return
                try{
                    port = Integer.parseInt((String)combo.getSelectedItem());
                }catch(Exception e){
                    display.append("Invalid Port Error\n");
                    return;
                }
                connected = connect(host,port);
                
                if(connected){
                    connectButton.setEnabled(false);
                    connectButton.setBackground(Color.BLUE);
                    sendButton.setEnabled(true);
                    message.requestFocus();
                    try{
                        //Create a thread passing the runnable reference to thread constructor
                        //Access instance of outer class using class name and this
                        Runnable run = new ChatRunnable<>(ClientChatUI.this,connection);
                        Thread thread = new Thread(run);
                        thread.start();
                    }catch(Exception e){
                        display.append("Thread creation error");
                    }
                    
                }
                
            }
            else if(action.equals("send")){
                send();
            }
        }
    }
    
   
    
}
