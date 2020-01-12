/*
 * File name: Server.java
 * Author:    Alex Carrozzi
 * Date: Date: Dec 6th, 2019 
 * Purpose: Contains the main method used to launch serverchat UI
 */


import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Contains main method and launcher of server
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see java.awt.EventQueue,java.io.IOException,java.net.ServerSocket,java.net.Socket
 */
public class Server {
    
    /**
     * Creates ServerChatUI and sets it visible
     * @param sck Socket object used for argument when creating ServerChat UI
     * @param title String used to set the title of JFrame
     */
    public static void launchClient(Socket sck, String title){
        EventQueue.invokeLater(new Runnable(){
            
            @Override
            public void run(){
                //Create an instance of ServerChatUI
                ServerChatUI chat = new ServerChatUI(sck);
                //Set title and visibilty 
                chat.setTitle(title);
                chat.setLocationRelativeTo(null);
                chat.setVisible(true);
       
            }
        });
        
    }
    
    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[]args){
        //Default port
        int port = 65535;
        Socket socket;
        if(args.length != 0){
            port = Integer.parseInt(args[0]);
            System.out.println("Using port: "+ port);
        }else{
            System.out.println("Using default port: "+port);
        }
        
        ServerSocket serverSocket;
        
        try{
            //Bound to the port
            serverSocket = new ServerSocket(port);
            int friend = 0;
            
            while(true){
                socket = serverSocket.accept();
                
                if(socket.getSoLinger()!= -1){
                    socket.setSoLinger(true,5);
                }
                if(!socket.getTcpNoDelay()){
                    socket.setTcpNoDelay(true);
                }
                System.out.println("Connecting to client Socket[addr="+socket.getInetAddress()+" port="+socket.getPort()+" localport="+socket.getLocalPort()+" ]");
                friend++;
                final String title = "Andy's Friend "+friend;
                launchClient(socket,title);
            }
        }catch(IOException e){
            System.out.println("Main server error");
        }
                     
    }
    
       
}
