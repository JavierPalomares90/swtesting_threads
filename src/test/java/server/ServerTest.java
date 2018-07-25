package server;

import org.junit.Test;
import gov.nasa.jpf.vm.Verify;
import java.io.IOException;

/**
 *
 * Functions to Test:
 *
 Class InventoryItem
    InventoryItem(String nameParam, int qtyParam)
    void updateQty(int qtyChange)
    Comparator<InventoryItem>

 Class Order
    Order(int idParam, String usernameParam, String productNameParam, int qtyParam)

 Class Listener implements Runnable
    Listener(String protocolParam, int portParam)
    void run()

 Class ServerThread implements Runnable
    ServerThread(Socket tcpClientSocketParam, int tcpPort)
    String processMessage(String[] msgArray)
    String purchase(String[] msgArray)
    String cancel(String[] msgArray)
    String search(String[] msgArray)
    String list()
    void run()

 Class Server
    void main(String[] args)
 **/

public class ServerTest
{
    @Test
    public void testMain() throws IOException
    {
        String[] inputs = new String[]{"1234", "src/test/resources/inventory.txt"};
        Server.main(inputs);

        unitTestSuite();
        integrationTestSuite();
        multiThreadTestSuite();

        /** TODO: Call unit test **/

        /** TODO: Complete testing **/
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

    }

    @Test
    public void multiThreadTestSuite(){

    }

    @Test
    public void InventoryItemUnitTests(){
        InventoryItem inv1 = new InventoryItem("phone", -1);
        assert(inv1.repOk() && inv1.qty == -1);
        InventoryItem inv2 = new InventoryItem("phone", 0);
        assert(inv2.repOk() && inv2.qty == 0);
        InventoryItem inv3 = new InventoryItem("phone", 1);
        assert(inv3.repOk() && inv3.qty == 1);
        InventoryItem inv4 = new InventoryItem("phone", 101);
        assert(inv4.repOk() && inv4.qty == 101);

        InventoryItem inv5 = new InventoryItem("", 25);
        assert(inv5.repOk() && inv5.qty == 25);
        InventoryItem inv6 = new InventoryItem("xbox", 25);
        assert(inv6.repOk() && inv5.qty == 25);
        InventoryItem inv7 = new InventoryItem("camera", 25);
        assert(inv7.repOk() && inv5.qty == 25);
        InventoryItem inv8 = new InventoryItem("1234 5678", 25);
        assert(inv8.repOk() && inv5.qty == 25);

        //TODO: Check updateQty function

        //TODO: Check comparator function
    }

    @Test
    public void OrderUnitTests(){

    }

    @Test
    public void ListenerUnitTests(){

    }

    @Test
    public void ServerThreadUnitTests(){

    }

    @Test
    public void ServerUnitTests(){

    }
}
