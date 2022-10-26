package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {

        //create bytearray for serverresponse
        ByteArrayOutputStream fromServerBytes = new ByteArrayOutputStream();

        //create socket
        Socket clientSocket = new Socket(hostname, port);

        //Send bytes to server
        clientSocket.getOutputStream().write(toServerBytes);

        //Recieve bytes from serve
        int s;
        while((s = clientSocket.getInputStream().read()) != -1){
            fromServerBytes.write(s);
        }

        //close socket
        clientSocket.close();

        return fromServerBytes.toByteArray();
    }
}
