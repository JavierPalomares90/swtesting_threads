package server;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class UnitTestSuite {

    @Test
    public void InventoryItemUnitTests(){
        List<InventoryItem> inventoryList = new ArrayList<>();

        //Test Qty Values
        InventoryItem inv1 = new InventoryItem("aaa9b9", -1);
        inventoryList.add(inv1);
        assert(inv1.repOk() && inv1.qty == -1);

        InventoryItem inv2 = new InventoryItem("bb1aa", 0);
        inventoryList.add(inv2);
        assert(inv2.repOk() && inv2.qty == 0);

        InventoryItem inv3 = new InventoryItem("3cbab2a", 1);
        inventoryList.add(inv3);
        assert(inv3.repOk() && inv3.qty == 1);

        InventoryItem inv4 = new InventoryItem("bbvva42x", 101);
        inventoryList.add(inv4);
        assert(inv4.repOk() && inv4.qty == 101);

        //Test Name Values
        InventoryItem inv5 = new InventoryItem("a", 25);
        inventoryList.add(inv5);
        assert(inv5.repOk() && inv5.qty == 25);

        InventoryItem inv6 = new InventoryItem("xbox", 25);
        inventoryList.add(inv6);
        assert(inv6.repOk() && inv5.qty == 25);

        InventoryItem inv7 = new InventoryItem("camera", 25);
        inventoryList.add(inv7);
        assert(inv7.repOk() && inv5.qty == 25);

        InventoryItem inv8 = new InventoryItem("1234 5678", 25);
        inventoryList.add(inv8);
        assert(inv8.repOk() && inv5.qty == 25);

        //Test updateQty
        inv1.updateQty(-1);
        assert(inv1.repOk() && inv1.qty == 0);
        inv1.updateQty(1);
        assert(inv1.repOk() && inv1.qty == -1);
        inv1.updateQty(10);
        assert(inv1.repOk() && inv1.qty == -11);
        inv1.updateQty(0);
        assert(inv1.repOk() && inv1.qty == -11);

        //Test comparator
        Collections.sort(inventoryList, InventoryItem.ItemComparator);
        Iterator<InventoryItem> iterator = inventoryList.iterator();
        InventoryItem curr = iterator.next();
        while(iterator.hasNext())
        {
            InventoryItem next = iterator.next();
            // curr must be lexographically less than or equal ot next.
            assertTrue(InventoryItem.ItemComparator.compare(curr,next) <= 0);
            curr = next;
        }
    }

    @Test(expected = InvalidParameterException.class)
    public void OrderNullUsernameTest(){
        int id = 0;
        String username = null;
        String productName = "bar";
        int qty = 1;
        Order order = new Order(id,username,productName,qty);
    }

    @Test(expected = InvalidParameterException.class)
    public void OrderNullProductnameTest(){
        int id = 0;
        String username = "foo";
        String productName = null;
        int qty = 1;
        Order order = new Order(id,username,productName,qty);
    }

    // Asser the Order parameters are initialized properly
    @Test
    public void OrderConstructorTest(){
        int id = 0;
        String username = "foo";
        String productName = "bar";
        int qty = 1;
        Order order = new Order(id,username,productName,qty);
        assertTrue(order != null);
        assertTrue(order.id == id);
        assertTrue(order.productName == productName);
        assertTrue(order.qty == qty);

    }

    @Test
    public void ListenerConstructorTest()
    {
        int tcpPort = 0;
        String protocol = "T";
        Listener listener = new Listener(protocol,tcpPort);
        assertTrue(listener != null);
        assertTrue(tcpPort == listener.tcpPort);
        assertTrue(protocol.equals(listener.protocol));
    }


    /**
     * Test the listener processes the request and returns the expected response
     */
    @Test
    public void ListenerRunUnitTest(){
        if(ServerThread.invList.isEmpty() == true)
        {
            // Populate the inventory if it's empty
            Server.processInventory("./src/test/resources/inventory.txt"); //Setup the inventory list.
        }

        int tcpPort = 1238; // This cannot be lower than 1024 on mac
        String protocol = "T";
        // Start off the listener
        Runnable listener = new Listener(protocol, tcpPort);
        new Thread(listener).start();

        // Wait 1 seconds for Listener to start, then send command:
        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
        String command = "list";
        String hostAddress = "127.0.0.1";
        String response = TestSuite.sendTcpMessage(command,hostAddress,tcpPort);
        assertTrue(response != null);
        String expectedResponse = "phone 20; laptop 15; camera 10; ps4 17; xbox 8; ";
        assertTrue(expectedResponse.equals(response));
    }

    // Test for ServerThread Class. Tests satisfy node coverage
    @Test
    public void ServerThreadUnitTests(){
        if(ServerThread.invList.isEmpty() == true)
        {
            // Populate the inventory if it's empty
            Server.processInventory("./src/test/resources/inventory.txt"); //Setup the inventory list.
        }

        String hostAddress = "127.0.0.1";
        int tcpPort = 1236; // This cannot be lower than 1024 on mac
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
        // Send a message to the ServerThread
        String listCommand = "list";
        String response = TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);
        // Assert that the server processes a list command correctly
        assertTrue(response != null);
        String expectedResponse = "phone 20; laptop 15; camera 10; ps4 17; xbox 8; ";
        assertTrue(expectedResponse.equals(response));
        // TODO: Finish testing purchase/cancel/search commands
        // Assert that a purchase command is processed successfully
        String purchaseCommand = "purchase bob camera 1";
        response = TestSuite.sendTcpMessage(purchaseCommand,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Your order has been placed, 101, camera, 1";
        assertTrue(expectedResponse.equals(response));

        // Assert that a search command is processed successfully
        String searchCommand = "search bob";
        response = TestSuite.sendTcpMessage(searchCommand,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "101, camera, 1";
        assertTrue(expectedResponse.equals(response));

        // Assert that a search command is processed successfully
        String cancelCommand = "cancel 101";
        response = TestSuite.sendTcpMessage(cancelCommand,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Order 101 is cancelled";
        assertTrue(expectedResponse.equals(response));

        // Assert that an invalid command is processed succesfully
        String invalidCommand = "invalid";
        response = TestSuite.sendTcpMessage(invalidCommand,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Invalid request";
        assertTrue(expectedResponse.equals(response));

        // Assert that a request for more than the available item quality is handled gracefully
        String invalidPurchase = "purchase bob xbox 100";
        response = TestSuite.sendTcpMessage(invalidPurchase,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Not Available - Not enough items";
        assertTrue(expectedResponse.equals(response));

        // Assert that a request for an invalid item is handled gracefully
        String invalidItemPurchase = "purchase bob macbook 100";
        response = TestSuite.sendTcpMessage(invalidItemPurchase,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Not Available - We do not sell this product";
        assertTrue(expectedResponse.equals(response));

        // Assert that a misformed purchase request is handled gracefully
        String misformed= "purchase bob 100";
        response = TestSuite.sendTcpMessage(misformed,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Incorrect number of parameters for PURCHASE";
        assertTrue(expectedResponse.equals(response));

        // Assert that a misformed cancel request is handled gracefully
        String invalidCancelCommand = "cancel bob";
        response = TestSuite.sendTcpMessage(invalidCancelCommand,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "Order ID is not a number";
        assertTrue(expectedResponse.equals(response));

        // Assert that a cancel request for an unplaced order is handled gracefully
        String noPriorPurchaseCancelOrder = "cancel 102";
        response = TestSuite.sendTcpMessage(noPriorPurchaseCancelOrder,hostAddress,tcpPort);
        assertTrue(response != null);
        expectedResponse = "102 not found, no such order";
        assertTrue(expectedResponse.equals(response));
    }

}
