package server;

import gov.nasa.jpf.vm.ThreadInfo;
import org.junit.*;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import java.lang.Math.*;

public class MultiThreadTestSuite{

    private static Listener server;

    @BeforeClass
    public static void initServer()
    {
        int tcpPort = 3456;
        String inventoryFile = "./src/test/resources/inventory2.txt";
        server = TestSuite.startServer(tcpPort, inventoryFile);
    }

    @AfterClass
    public static void stopServer()
    {
        server.stop();
    }



    @Test
    public void multiThreadTestSuite(){
       int numberOfLoops = 5;


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
                int tcpPort = 3456;
                LinkedList<String>inventoryItems = new LinkedList<>();
                // Get Inventory List and populate Inventory Items List
                String listCommand = "list";
                String listreply = TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);
                String[] listreplyArray = listreply.split(";");
                for(String elem : listreplyArray){
                    if(elem.trim().split(" ")[0].length() > 0)
                        inventoryItems.add(elem.trim().split(" ")[0]);
                }
                System.out.println(name + " : " + inventoryItems);

                //TODO: Send a series of purchases to server, search for orders, parse the orders for orderIDs
                //TODO:     then cancel the orders, exit.

                //TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);

                // Random Number: //ThreadLocalRandom.current().nextInt(min, max + 1)
            }
        }

        //Start up a list of Client threads
        LinkedList<Thread> threadList = new LinkedList<>();
        for(int i = 0; i < numberOfLoops; i++) {
            clientRunner cr = new clientRunner(customerArray[i]);
            Thread tc = new Thread(cr);
            tc.setName(customerArray[i]);
            threadList.add(tc);
            tc.start();
        }

        //Check all the threads and wait till they all complete, THEN proceed with ending the method.
        boolean allDone = false;
        while(!allDone){
            allDone = true;
            for(Thread thread : threadList){
                if(thread.getState() != Thread.State.TERMINATED){
                    allDone = false;
                }
            }
        }
    }
}
