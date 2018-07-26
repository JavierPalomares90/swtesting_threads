package server;

import org.junit.Test;
import gov.nasa.jpf.vm.Verify;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class ServerTest
{
    @Test
    public void testMain() throws IOException
    {
        String[] inputs = new String[]{"1234", "src/test/resources/inventory.txt"};
        Server.main(inputs);

        System.out.println("Size of inventory list: " + ServerThread.invList.size());

        unitTestSuite();
        integrationTestSuite();
        multiThreadTestSuite();

    }

    @Test
    public void unitTestSuite(){
        InventoryItemUnitTests();
        OrderUnitTests();
        ListenerUnitTests();
        ServerThreadUnitTests();
        ServerUnitTests();

    }

    @Test
    public void integrationTestSuite(){
        //TODO: Send single commands (list, purchase, search, cancel) to Server and validate response

    }

    @Test
    public void multiThreadTestSuite(){
        //TODO: Start 1 server process, then spawn off n threads which will throw randomized (but valid)
        //TODO:     commands to the server simultaneously.
        //TODO:     Create a grammar and use Verify to generate the commands.


    }

    @Test
    public void InventoryItemUnitTests(){
        ArrayList<InventoryItem> inventoryList = new ArrayList<>();

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
        //TODO: Test to see if the inventoryList is sorted after Collections.sort is called.

    }

    @Test
    public void OrderUnitTests(){
        //TODO: Test Order constructor:
        //TODO:     Order(int idParam, String usernameParam, String productNameParam, int qtyParam)
    }

    @Test
    public void ListenerUnitTests(){
        //TODO: Test Listener constructor and thread spawning
        //TODO:    Listener(String protocolParam, int portParam)
        //TODO:    void run()

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
