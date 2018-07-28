package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class UnitTestSuite {

    @Test
    public static void runUnitTestSuite(){
        InventoryItemUnitTests();
        OrderUnitTests();
        ListenerUnitTests();
        ServerThreadUnitTests();
        ServerUnitTests();
    }

    @Test
    public static void InventoryItemUnitTests(){
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
    public static void OrderUnitTests(){
        //TODO: Test Order constructor:
        //TODO:     Order(int idParam, String usernameParam, String productNameParam, int qtyParam)
    }

    @Test
    public static void ListenerUnitTests(){
        //TODO: Test Listener constructor and thread spawning
        //TODO:    Listener(String protocolParam, int portParam)
        //TODO:    void run()

    }

    @Test
    public static void ServerThreadUnitTests(){
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
    public static void ServerUnitTests(){
        //TODO: Test main:
        //TODO:     void main(String[] args)

    }

}
