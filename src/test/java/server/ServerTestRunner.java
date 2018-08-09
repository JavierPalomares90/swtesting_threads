package server;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class ServerTestRunner
{
    public static void main(String[] args)
    {
        //TODO: Is there a way to close ports at the end of the tests?

        Result result = JUnitCore.runClasses(TestSuite.class);
        int testCount = result.getRunCount();
        System.out.println("Ran " + testCount + " tests");


        for (Failure failure : result.getFailures()) {
            Description description = failure.getDescription();
            String className = description.getClassName();
            String methodName = description.getMethodName();
            System.out.println("Failure at method " + methodName + " in " + className);
        }

    }

}
