package server;

import org.junit.Assert;
import org.junit.Test;
import gov.nasa.jpf.vm.Verify;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class MultiThreadTestSuite{


    @Test
    public void multiThreadTestSuite(){
       int numberOfLoops = 5;

       Server.processInventory("./src/test/resources/inventory2.txt"); //Setup the inventory list.

       String hostAddress = "127.0.0.1";
       int tcpPort = 4567;

       // Spin off the serverThread
       class serverRunner implements Runnable{
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
       }

       serverRunner sr = new serverRunner();
       Thread ts = new Thread(sr);
       ts.start();

       // Wait 1 seconds for Listener to start, then send command:

       try
       {
           TimeUnit.SECONDS.sleep(1);
       } catch (InterruptedException e)
       {
           e.printStackTrace();
       }

       String[] customerArray = new String[]{"Adam", "Bob", "Charlie", "David", "Erik", "Frank", "George"};

       class clientRunner implements Runnable{
            private String name;
            public clientRunner(String threadName){
                name = threadName;
            }

            @Override
            public void run()
            {
                String hostAddress = "127.0.0.1";
                int tcpPort = 4567;
                LinkedList<String>inventoryItems = new LinkedList<>();
                // Get Inventory List and populate Inventory Items List
                String listCommand = "list";
                String listreply = TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);
                String[] listreplyArray = listreply.split(";");
                for(String elem : listreplyArray){
                    if(elem.trim().split(" ")[0].length() > 0)
                        inventoryItems.add(elem.trim().split(" ")[0]);
                }

                System.out.println("Name: " + name);

                //TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);

                // Random Number: //ThreadLocalRandom.current().nextInt(min, max + 1)
            }
        }
        clientRunner cr = new clientRunner(customerArray[Verify.getInt(0,customerArray.length)]);
        Thread tc = new Thread(cr);
        tc.start();

    }
}
