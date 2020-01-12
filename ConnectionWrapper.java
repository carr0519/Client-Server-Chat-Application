/*
 * File name: ConnectionWrapper.java
 * Author:    Alex Carrozzi
 * Date: Dec 6th, 2019  
 * Purpose: Contains embedded objects of ObjectInputStream and ObjectOutputStream,
 *          provides methods to create and get them.
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Creates an output stream and input stream for client and server
 * 
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see java.io.IOException,java.io.ObjectInputStream,java.io.ObjectOutputStream,
 *      java.net.Socket
 */
public class ConnectionWrapper {
    
    /**
     * embedded ObjectOutputStream object 
     */
    private ObjectOutputStream outputStream;
    /**
     * embedded ObjectInputStream object
     */
    private ObjectInputStream inputStream;
    /**
     * embedded Socket object
     */
    Socket socket;
    
    /**
     * Class constructor initialize socket field
     * @param socket Used to initialize socket field
     */
    public ConnectionWrapper(Socket socket){
        this.socket = socket;
    }
    
    /**
     * Getter method for socket
     * @return socket field
     */
    public Socket getSocket(){
        return this.socket;
    }
    
    /**
     * Getter method for output stream
     * @return outputStream field
     */
    public ObjectOutputStream getOutputStream(){
        return this.outputStream;
    }
    
    /**
     * Getter method for input stream
     * @return inputStream field
     */
    public ObjectInputStream getInputStream(){
        return this.inputStream;
    }
    
    /**
     * Creates an instance of ObjectInputStream and initializes inputStream field
     * @return an instance of ObjectInputStream
     * @throws IOException 
     */
    ObjectInputStream createObjectIStreams() throws IOException{
        
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        this.inputStream = input;
        return input;
        
    }
    
    /**
     * Creates an instance of ObjectOutputStream and initializes outputStream field
     * @return an instance of ObjectOutputStream
     * @throws IOException 
     */
    ObjectOutputStream createObjectOStreams() throws IOException{
        
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream = output;
        return output;
    }
    
    /**
     * Firstly creates an instance of ObjectOutputStream and initializes outputStream field
     * before doing the same for inputStream field
     * @throws IOException 
     */
    void createStreams() throws IOException{
 
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
       
    }
    
    /**
     * Closes the output stream, input stream and socket
     * @throws IOException 
     */
    public void closeConnection() throws IOException{
        //Check for null references and already closed socket
        if(inputStream != null && !(socket.isClosed()) && socket != null){
            inputStream.close();
        }
        if(outputStream != null && !(socket.isClosed()) && socket != null){
            outputStream.close();
        }
        if(socket != null && !(socket.isClosed()) && socket != null){
            socket.close();
        }
        
      
    }
    
    
}
