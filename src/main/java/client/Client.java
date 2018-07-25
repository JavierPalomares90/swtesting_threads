package client;

import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Client {
    public static void main (String[] args) {
        String hostAddress;
        int tcpPort;

        if (args.length != 2) {
        System.out.println("ERROR: Provide 2 arguments");
        System.out.println("\t(1) <hostAddress>: the address of the server");
        System.out.println("\t(2) <tcpPort>: the port number for TCP connection");
        System.exit(-1);
        }
    
        hostAddress = args[0];
        tcpPort = Integer.parseInt(args[1]);

        int bufferLen = 1024;
            
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            String cmd = sc.nextLine();
            String[] tokens = cmd.split(" ");

            if (tokens[0].equals("quit")) System.exit(-1);

            try{
                //Send message
                Socket tcpSocket = new Socket(hostAddress, tcpPort);
                PrintWriter outputWriter = new PrintWriter(tcpSocket.getOutputStream(), true);
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                outputWriter.write(cmd + "\n");
                outputWriter.flush();
                //Receive reply
                String recvLine = "";
                while((recvLine = inputReader.readLine()) != null) System.out.println(recvLine);
                //tcpSocket.close();
            } catch(IOException ioe){System.err.println(ioe);}
            break;
   }   }   }
