package server;

import org.junit.Test;

import gov.nasa.jpf.vm.Verify;

import java.io.IOException;

public class ServerTest
{
    @Test
    public void testMain() throws IOException
    {
        String[] inputs = new String[]{"1234", "src/src/test/resources/inventory.txt"};
        server.Server.main(inputs);

        /** TODO: Call unit test **/

        /** TODO: Complete testing **/
    }

    @Test
    public void unitTests(){

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


    }

}
