package server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        IntegrationTestSuite.class,
        MultiThreadTestSuite.class,
        UnitTestSuite.class
})

public class TestSuite {
}
