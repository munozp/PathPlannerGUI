package Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;


/**
 * Class to handle the TCP/IP connection with the robot
 * @author pablo.munoz
 */
public class ConnectionThread extends Thread {

    /** Message buffer. */
    private CharBuffer msg;
    /** Socket connection. */
    private Socket socket;
    /** Input channel. */
    private BufferedReader in;
    /** Output channel. */
    private OutputStreamWriter out;
    /** Last message received */
    private String lastMessage;
    /** Close the connection */
    private boolean endConnection = false;
    /** Message size in bytes. */
    private static final int MESSAGE_SIZE = 256;
    /** Object that handle the received data */
    private Object onReadObject;
    /** Method invoked when received data */
    private Method onReadMethod;
    /** Name of the invoked method */
    private static final String ON_READ_METHOD_NAME = "handleReceivedData";
    /** Delay (milliseconds) between socket reads for the thread */
    private static final int DELAY = 100;

    
    /**
     * Constructor initializes output buffer of MESSAGE_SIZE in bytes and the method invoked when data is received
     */
    public ConnectionThread(Object ri) {
        onReadObject = ri;
        try {
            onReadMethod = ri.getClass().getMethod(ON_READ_METHOD_NAME, String.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            System.out.println(ex.toString());
        }
        msg = CharBuffer.allocate(MESSAGE_SIZE);
    }
  
    /**
     * Establishes the connection with the OGATE server at the specified IP and PORT number.
     * @param ip IP or host in which is located the OGATE server
     * @param port port number
     * @return return true if the connection is established and the client id is obtained. False otherwise.
     */
    public boolean connect(String ip, int port) {
        try{
            socket = new Socket(ip, port);
            out = new java.io.OutputStreamWriter(socket.getOutputStream(), Charset.forName("ISO-8859-1"));
            in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            endConnection = false;
        }catch(IOException ioe){
            System.err.println("Problem connecting to "+ip+":"+String.valueOf(port)+"\n"+ioe.getMessage());
            endConnection = true;
            out = null;
            in = null;
        }
        lastMessage = null;        
        return !endConnection;
    }
    
    public boolean connect(String ipport) {
        int colon = ipport.indexOf(':');
        String addr = ipport.substring(0, colon);
        String ports = ipport.substring(colon+1, ipport.length());
        int port;
        try{
            port = Integer.valueOf(ports);
        }catch(NumberFormatException e){
            return false;
        }
        return connect(addr, port);
    }

    /**
     * Get the last message or null string
     * @return the last message readed or null if there is no a new message since the last call to this function
     */
    public String getLastMessage() {
        String temp = lastMessage;
        lastMessage = null;
        return temp;    
    }

    /**
     * Read the input channel and stores received data
     * Data is accessible using getLastMessage()
     */
    @Override
    public void run()
    {
        if(!socket.isConnected())
            return;
        do{
            try{
                lastMessage = receive();
                if(lastMessage.isEmpty())
                    sleep(DELAY);
                else
                    // Invoke method that handle the received message
                    onReadMethod.invoke(onReadObject, lastMessage);
            }catch(InterruptedException ex){
                System.out.println(ex.toString());
            }catch(IllegalAccessException | InvocationTargetException ex){
                System.out.println(ex.toString());
                endConnection = true;
            }
        }while(!endConnection);
    }
    
    /**
     * Read the socket in a non-blocking way
     * @return a clean string with the message read, empty string if nothing to read
     */
    private String receive()
    {
        if(!socket.isConnected())
            return "";
        try{
            msg.rewind();
            if(in.read(msg) > 0)
            {
                msg.rewind();
                String message = msg.toString();
                return message.substring(0, message.indexOf('\0')); // Clean string
            }
        }catch(IOException ioe){ 
            System.out.println(ioe.toString());
        }
        return "";
    }
    
    /**
     * Send a message to the socket
     * @param data the data of the message
     * @param sizeprefix if true, size of the data will be included as prefix (00000|data)
     * @return The number of bytes written
     */
    public int send(String data, boolean sizeprefix) {
        if(!socket.isConnected() || data == null || data.isEmpty())
            return -1;
        
        String message = data;
        java.text.DecimalFormat df = new java.text.DecimalFormat("00000");
        int size = message.length()+5;
        message = df.format(size).concat(message);
        
        msg.clear();
        msg.put(message);
        for(int p=msg.position(); p<msg.capacity(); p++)
            msg.put('\0');
        msg.rewind();
        
        try{
            out.write(msg.array());
            out.flush();
        }catch(IOException ioe) {
            System.err.println(ioe.toString());
            return -1;
        }
        return size;
    }

    /**
     * Finalizes the current connection
     */
    public void endConnection() {
        endConnection = true;
        try {
            if(socket != null && socket.isConnected())
                socket.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
}