package eu.uberdust.pcmonitor.test;


import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.pcmonitor.jobs.AbstractJob;
import eu.uberdust.pcmonitor.jobs.unix.CpuUsage;
import eu.uberdust.pcmonitor.jobs.unix.KernelVersion;
import eu.uberdust.pcmonitor.jobs.unix.ProcessesInfo;
import eu.uberdust.pcmonitor.jobs.unix.UpTime;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UnixJobTest extends TestCase {

    public UnixJobTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(UnixJobTest.class);
    }

    public void testCpuUsage() {
        assertTrue(true);
        try {
            AbstractJob test = new CpuUsage();
            for (Message.NodeReadings.Reading nodeReading : test.getReadings().getReadingList()) {
                if (nodeReading.hasStringReading()) {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getStringReading());
                } else {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getDoubleReading());
                }
            }
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    public void testKernelVersion() {
        assertTrue(true);
        try {
            AbstractJob test = new KernelVersion();
            for (Message.NodeReadings.Reading nodeReading : test.getReadings().getReadingList()) {
                if (nodeReading.hasStringReading()) {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getStringReading());
                } else {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getDoubleReading());
                }
            }
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    public void testProcesses() {
        assertTrue(true);
        try {
            AbstractJob test = new ProcessesInfo();
            for (Message.NodeReadings.Reading nodeReading : test.getReadings().getReadingList()) {
                if (nodeReading.hasStringReading()) {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getStringReading());
                } else {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getDoubleReading());
                }
            }
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    public void testUptime() {
        assertTrue(true);
        try {
            AbstractJob test = new UpTime();
            for (Message.NodeReadings.Reading nodeReading : test.getReadings().getReadingList()) {
                if (nodeReading.hasStringReading()) {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getStringReading());
                } else {
                    System.out.println(nodeReading.getCapability() + ":" + nodeReading.getDoubleReading());
                }
            }
        } catch (Exception e) {
            assertFalse(true);
        }
    }
}
