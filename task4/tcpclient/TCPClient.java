package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    boolean shutdown;
    Integer timeout;
    Integer limit;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {

        //create bytearray for serverresponse
        ByteArrayOutputStream fromServerBytes = new ByteArrayOutputStream();

        //create socket
        Socket clientSocket = new Socket(hostname, port);

        //Send bytes to server
        clientSocket.getOutputStream().write(toServerBytes);

        //check for a shutdown
        if(shutdown)
            clientSocket.shutdownOutput();

        //set timout timer
        if(timeout != null)
            clientSocket.setSoTimeout(timeout);
        else
            clientSocket.setSoTimeout(0);

        try{

        //variables for 
        boolean valid = true;
        int byteCounter = 0;
        int s;

        //Recieve bytes from server
        while(valid){

            //check how much time has passed
            if((s = clientSocket.getInputStream().read()) == -1)
                valid = false;
            
            else {
                fromServerBytes.write(s);
                byteCounter++;
                if(limit != null && byteCounter >= limit)
                    valid = false;
            }
        }

        //close socket
        clientSocket.close();
        return fromServerBytes.toByteArray();

     } //end of try

    catch(SocketTimeoutException e){
        System.out.println("Timeout: " + e);
        clientSocket.close();
        return fromServerBytes.toByteArray();
    } 

    }
}
