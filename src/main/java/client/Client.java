package client;//Distributed Systems Fall 2017
//Homework 1
//Patrick Sigourney & Howie Benefiel

import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Client {
    public static void main (String[] args) {
        String hostAddress;
        int tcpPort;
        int udpPort;
    
        if (args.length != 3) {
        System.out.println("ERROR: Provide 3 arguments");
        System.out.println("\t(1) <hostAddress>: the address of the server");
        System.out.println("\t(2) <tcpPort>: the port number for TCP connection");
        System.out.println("\t(3) <udpPort>: the port number for UDP connection");
        System.exit(-1);
        }
    
        hostAddress = args[0];
        tcpPort = Integer.parseInt(args[1]);
        udpPort = Integer.parseInt(args[2]);
    
        char protocol = 'T';  //default protocol is TCP
        int bufferLen = 1024;
            
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            String cmd = sc.nextLine();
            String[] tokens = cmd.split(" ");

            if (tokens[0].equals("quit")) System.exit(-1);
            
            if (tokens[0].equals("setmode")) {
                switch (tokens[1]) {
                    case "T": 
                        protocol =  'T';
                        System.out.println("Using TCP");
                        break;
                    case "U": 
                        protocol = 'U';
                        System.out.println("Using UDP");
                        break;
                    default: System.out.println("Invalid value");
                        continue;            
                }
            }
            else{
                switch(protocol){
                    case 'T':
                        try{
                            //Send message
                            Socket tcpSocket = new Socket(hostAddress, tcpPort);
                            PrintWriter outputWriter = new PrintWriter(tcpSocket.getOutputStream(), true);
                            BufferedReader inputReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                            outputWriter.write(cmd + "\n");
                            outputWriter.flush();
                            
                            //Receive reply
                            String recvLine = "";
                            
                            while((recvLine = inputReader.readLine()) != null)
                                    System.out.println(recvLine);

                            tcpSocket.close();
                        }
                        catch(IOException ioe){System.err.println(ioe);}

                        break;
                        
                    case 'U':
                        try{
                            //Send message
                            byte[] payload = new byte[cmd.length()];
                            payload = cmd.getBytes();
                            InetAddress inetAddy = InetAddress.getByName(hostAddress);
                            DatagramSocket udpOutboundSocket = new DatagramSocket();  //No IP/port for outbound
                            DatagramSocket udpInboundSocket = new DatagramSocket(udpPort);  //Listen on UDP port for inbound
                            DatagramPacket sPacket = new DatagramPacket(payload, payload.length, inetAddy, udpPort);
                            udpOutboundSocket.send(sPacket);
                            udpOutboundSocket.close();
                            
                            //Receive Reply:
                            byte[] udpBuff = new byte[bufferLen];
                            DatagramPacket rPacket = new DatagramPacket(udpBuff,udpBuff.length);
                            udpInboundSocket.receive(rPacket);
                            String msgData = new String(rPacket.getData());                            
                            msgData = msgData.trim();
                            System.out.println(msgData);  
                            udpInboundSocket.close();
                        }
                        catch(SocketException se){System.err.println(se);}
                        catch(IOException ioe){System.err.println(ioe);}
                        break;
}   }   }   }   }
