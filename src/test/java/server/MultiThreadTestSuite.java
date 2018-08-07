package server;

import org.junit.Assert;
import org.junit.Test;
import gov.nasa.jpf.vm.Verify;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;


import static org.junit.Assert.assertTrue;

public class MultiThreadTestSuite {

   @Test
    public void multiThreadTestSuite(){


        Server.processInventory("./src/test/resources/inventory2.txt"); //Setup the inventory list.

        String hostAddress = "127.0.0.1";
        int tcpPort = 4567; // This cannot be lower than 1024 on mac

        // Spin off the serverThread
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ServerSocket tcpServerSocket = new ServerSocket(tcpPort);
                    while (true)
                    {
                        Socket tcpClientSocket = tcpServerSocket.accept();
                        Runnable tcpServerThread = new ServerThread(tcpClientSocket, tcpPort);
                        new Thread(tcpServerThread).start();

                    }
                }catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    Assert.fail();
                }
            }
        }).start();

        // Wait 1 seconds for Listener to start, then send command:
        try
        {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // Create Inventory Item List
       LinkedList<String>inventoryItems = new LinkedList<>();
       // Create Customer Name List
       String[] customerList = new String[]{"Adam", "Bob", "Charlie", "David", "Erik", "Frank", "George"};
       // Create Orders List
       LinkedList<Integer> orderIdList = new LinkedList<Integer>();

        // Get Inventory List
        String listCommand = "list";
        String listreply = TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);
        String[] listreplyArray = listreply.split(";");
        for(String elem : listreplyArray){
            System.out.println(elem.trim());
            if(elem.trim().split(" ")[0].length() > 0)
                inventoryItems.add(elem.trim().split(" ")[0]);

        }

        //TODO: Generate random commands to the Server:
       //When command is "purchase" add order ID to orderIdList
       //When command is "cancel" remove order ID from orderIdList
       

    }


}
