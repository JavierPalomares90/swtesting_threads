package server;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        String protocol = "tcp";
        Listener listener = new Listener(protocol,tcpPort);
        assertTrue(listener != null);
        assertTrue(tcpPort == listener.tcpPort);
        assertTrue(protocol.equals(listener.protocol));
    }

    private static void sendTcpMessage(String cmd, String hostAddress, int tcpPort)
    {
        // Send
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

    }

    @Test
    public void ListenerRunUnitTest(){
        int tcpPort = 1024; // This cannot be lower than 1024 on mac
        String protocol = "tcp";
        Listener listener = new Listener(protocol,tcpPort);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(listener);
        String command = "list";


        String hostAddress = "192.168.1.1";
        sendTcpMessage(command,hostAddress,tcpPort);

        service.shutdown();
        try
        {
            service.awaitTermination(60, TimeUnit.SECONDS);
        }catch (InterruptedException e)
        {
            Assert.fail(e.getMessage());
        }

        //TODO: What should be asserted here?
    }

    @Test
    public void ServerThreadUnitTests(){
        //TODO: Test ServerThread, individual functions, and thread spawning:
        //TODO:     ServerThread(Socket tcpClientSocketParam, int tcpPort)
        //TODO:     String processMessage(String[] msgArray)
        //TODO:     String purchase(String[] msgArray)
        //TODO:     String cancel(String[] msgArray)
        //TODO:     String search(String[] msgArray)
        //TODO:     String list()
        //TODO:     void run()

    }

    @Test
    public void ServerUnitTests(){
        //TODO: Test main:
        //TODO:     void main(String[] args)

    }

}
