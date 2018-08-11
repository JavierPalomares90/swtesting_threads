package server;

import org.junit.*;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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
       int numberOfLoops = 2;  //Must be less than 9 (number of inventory items)
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

                LinkedList<String>inventoryList = new LinkedList<>();
                LinkedList<String>orderIds = new LinkedList<>();

                // Get Inventory List and populate Inventory Items List
                String listCommand = "list";
                String origListReply = TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);
                String[] listreplyArray = origListReply.split(";");
                for(String elem : listreplyArray){
                    if(elem.trim().split(" ")[0].length() > 0)
                        inventoryList.add(elem.trim().split(" ")[0]);
                }

                //Place a series of purchases
                for(int i = 0; i < numberOfLoops; i++){
                    int randomQty = ThreadLocalRandom.current().nextInt(1, 11);
                    String command = "purchase " +  name + " " + inventoryList.get(i) + " " + randomQty;
                    TestSuite.sendTcpMessage(command, hostAddress, tcpPort);
                }

                try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

                //Find the purchases and validate the item
                String reply = TestSuite.sendTcpMessage("search " + name, hostAddress, tcpPort);
                String[] orderArray = reply.split(";");
                for(int i = 0; i < orderArray.length; i++){
                    String[] elemArray = orderArray[i].split(",");
                    orderIds.add(elemArray[0].trim());
                    Assert.assertTrue(inventoryList.get(i) + " == " + elemArray[1].trim(), inventoryList.get(i).equals(elemArray[1].trim()));
                }

                try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

                //We placed ((number of loops)) orders.
                Assert.assertTrue(orderIds.size() + " == " + numberOfLoops, orderIds.size() == numberOfLoops);

                //Cancel the orders
                for(int i = 0; i < orderIds.size(); i++){
                    String cancel = "cancel " + orderIds.get(i);
                    TestSuite.sendTcpMessage(cancel,hostAddress,tcpPort);
                }

                try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

                //Assert no orders found
                reply = TestSuite.sendTcpMessage("search " + name, hostAddress, tcpPort);
                Assert.assertTrue(reply.trim() + " == " + "No order found for " + name, reply.trim().equals("No order found for " + name));

            }
        }

        //Start up a list of Client threads
        LinkedList<Thread> threadList = new LinkedList<>();
        for(int i = 0; i < customerArray.length; i++) {
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
