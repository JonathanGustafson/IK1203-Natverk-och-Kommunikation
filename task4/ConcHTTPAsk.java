import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
    public static void main( String[] args) throws IOException{

        //create server with given port
        int argindex = 0;
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[argindex++]));
        
        while(true) { 
            Socket socket = serverSocket.accept();
            System.out.println("INCOMMING CLIENT: \n");
            Runnable r = new MyRunnable(socket);
            new Thread(r).start();
            System.out.println("New thread created for client\n");
        } 
    }
}

