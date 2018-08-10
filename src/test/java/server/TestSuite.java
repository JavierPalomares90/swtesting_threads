package server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import javax.management.ListenerNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        UnitTestSuite.class,
        IntegrationTestSuite.class,
        MultiThreadTestSuite.class
})

public class TestSuite {
    public static String sendTcpMessage(String cmd, String hostAddress, int tcpPort)
    {
        String replyMessage = "";
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
                replyMessage += recvLine;
            tcpSocket.close();
        } catch(IOException ioe){System.err.println(ioe);}
        return replyMessage;
    }

    public static Listener startServer(int tcpPort, String inventoryFile){
        System.out.println("Starting server at port" + tcpPort);
        ServerThread.invList.clear();
        Server.processInventory(inventoryFile);
        String protocol = "T";
        Runnable listener = new Listener(protocol, tcpPort);
        Listener myListener = (Listener) listener;
        System.out.println("ServerStarted");
        new Thread(listener).start();

        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        return myListener;
    }

}
