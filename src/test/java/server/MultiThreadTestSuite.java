package server;

import org.junit.Test;
import gov.nasa.jpf.vm.Verify;

public class MultiThreadTestSuite {

    @Test
    public static void runMultiThreadTestSuite(){
        multiThreadTestSuite();
    }

    @Test
    public static void multiThreadTestSuite(){
        //TODO: Start 1 server process, then spawn off n threads which will throw randomized (but valid)
        //TODO:     commands to the server simultaneously.
        //TODO:     Create a grammar and use Verify to generate the commands.
        //TODO:     Have all the threads keep throwing commands to the Server


    }

}
