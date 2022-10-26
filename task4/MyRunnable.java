import java.net.*;
import java.io.*;
import tcpclient.*;

public class MyRunnable implements Runnable{

    Socket socket;

    public MyRunnable(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        
        //HTTP responses
        String HTTPHeader = "HTTP/1.1 200 OK\r\n\r\n";
        String BadRequest = "HTTP/1.1 400 Bad Request\r\n\r\n";
        String NotFound = "HTTP/1.1 404 Not Found\r\n\r\n";
        
        //variables for get request
        Boolean httpVersion;
        String request;
        String hostname;
        Integer port;
        String data;
        String[] parameters;
        byte[] fromClientData;

        //extras variables
        boolean shutdown;
        Integer limit;
        int timeout;

        try{
            //Create connection socet and input/outputstream
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            InputStream in = socket.getInputStream();

            //get request, save it into a string
            StringBuilder buildRequest = new StringBuilder();
            int s;
            while((s = in.read()) != '\n'){
                 buildRequest.append((char)s);
            }
            request = buildRequest.toString();

            System.out.println(request);

            //split request into smaller pieces
            parameters = request.split("[?\\&\\=\\ ]");
            
            /*System.out.println("\nParameters start:");
            for(String t : parameters)
                System.out.println(t);
            System.out.println("Parameters end:\n");*/

            httpVersion = false;
            hostname = null;
            port = null;
            data = null;
            shutdown = false;
            limit = null;
            timeout = 0;

            if(parameters[0].equals("GET"))
                for(int i = 0; i < parameters.length; i++){
                    if(parameters[i].equals("hostname"))
                        hostname = parameters[++i];

                    else if(parameters[i].equals("port"))
                        port = Integer.parseInt(parameters[++i]);
                    
                    else if(parameters[i].equals("limit"))
                        limit = Integer.parseInt(parameters[++i]);
                        
                    else if(parameters[i].equals("string"))
                        data = parameters[++i];

                    else if(parameters[i].equals("timeout"))
                        timeout = Integer.parseInt(parameters[++i]);
                        
                    else if(parameters[i].equals("shutdown"))
                        shutdown = Boolean.parseBoolean(parameters[++i]);

                    else if(parameters[i].contains("HTTP/1.1"))
                        httpVersion = true;
                        
                }

            //check if any data was sent
            if(data != null)
                fromClientData = data.getBytes();
            else
                fromClientData = new byte[0];

           /* System.out.println("\nHostname: " + hostname +
                                "\nPort: " + port + 
                                "\nData: " + data +
                                "\nHttpVersion: " + httpVersion + "\n");*/


            
            //send response
            if(parameters[1].equals("/ask") && hostname != null && port != null && httpVersion){
                try{
                    TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
                    byte[] toClientBytes = tcpClient.askServer(hostname, port, fromClientData);
                    String serverOutput = new String(toClientBytes);
                    out.writeBytes(HTTPHeader + serverOutput);
                }
                catch(Exception e){
                    out.writeBytes(NotFound);
                }
            } 
            else{
                out.writeBytes(BadRequest);
            }
            System.out.println("*****CLIENT DONE*****");
            socket.close();
        }
        catch(Exception e){
            System.out.println("Exception thrown: " + e);
        }
    }

    /*public static void main(String args[]) {
        (new Thread(new MyRunnable())).start();
    }*/
}
