package server;

import org.junit.Test;
import java.io.IOException;

public class ServerTest
{
    @Test
    public void testMain() throws IOException
    {
        String[] inputs = new String[]{"1234", "src/test/resources/inventory.txt"};
        Server.main(inputs);

        System.out.println("Size of inventory list: " + ServerThread.invList.size());

        UnitTestSuite unitTests = new UnitTestSuite();
        unitTests.runUnitTestSuite();
        IntegrationTestSuite integrationTests = new IntegrationTestSuite();
        integrationTests.runIntegrationTestSuite();
        MultiThreadTestSuite multiThreadTests = new MultiThreadTestSuite();
        multiThreadTests.runMultiThreadTestSuite();
        //test

    }

}
