package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class IntegrationTestSuite {

    @Test
    public void integrationTestSuite(){
        int tcpPort = 1234;
        String hostAddress = "127.0.0.1";
        // Clear the inventory
        ServerThread.invList = new ArrayList<>();
        ServerThread.maxOrderID = 100;
        String inventory = "src/test/resources/inventory.txt";
        String[] inputs = new String[]{"1234", inventory};

        // Start up the server
        //Server.main(inputs);
        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        Listener server = TestSuite.startServer(tcpPort, inventory);

        // Send a message to the ServerThread
        String listCommand = "list";
        String response = TestSuite.sendTcpMessage(listCommand,hostAddress,tcpPort);

        // Assert that the server processes a list command correctly
        assertTrue(response != null);
        // response should be sorted
        String expectedResponse = "camera 10; laptop 15; phone 20; ps4 17; xbox 8; ";
        assertTrue(expectedResponse.equals(response));


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

        server.stop();

    }
}
