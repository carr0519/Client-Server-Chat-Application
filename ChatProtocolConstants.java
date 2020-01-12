/*
 * File name: ChatProtocolConstants.java
 * Author:    Alex Carrozzi
 * Date: Dec 6th, 2019 
 * Purpose: provides constants for chat 
 */


/**
 * Contains only final strings
 * @author Alex Carrozzi
 * @version 1.0
 * @since 1.8.0_181
 * @see package server
 */
public class ChatProtocolConstants {
    /**
     * constant string : bye
     */
    final static String CHAT_TERMINATOR="bye";
    /**
     * constant string : two tabs
     */
    final static String DISPLACMENT="\t\t";
    /**
     * constant string : return carriage and newline
     */
    final static String LINE_TERMINATOR="\r\n";
    /**
     * constant sting : hello
     */
    final static String HANDSHAKE="hello";
}
