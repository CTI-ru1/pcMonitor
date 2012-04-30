package eu.uberdust.pcmonitor.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FindOsTest extends TestCase {

    public FindOsTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(FindOsTest.class);
    }

    public void testCase() {
        assertTrue(true);
        try {
            final String osName = System.getProperty("os.name");
            System.out.println(osName);
            final String osArch = System.getProperty("os.arch");
            System.out.println(osArch);
            final String osVersion = System.getProperty("os.version");
            System.out.println(osVersion);
        } catch (Exception e) {
            assertFalse(true);
        }
    }
}
