package eu.uberdust.pcmonitor;

import eu.uberdust.pcmonitor.jobs.unix.BatteryCharge;
import eu.uberdust.pcmonitor.jobs.unix.CpuUsage;
import eu.uberdust.pcmonitor.jobs.unix.DiskFree;
import eu.uberdust.pcmonitor.jobs.unix.DiskTemp;
import eu.uberdust.pcmonitor.jobs.unix.HostAlive;
import eu.uberdust.pcmonitor.jobs.unix.KernelVersion;
import eu.uberdust.pcmonitor.jobs.unix.MemFree;
import eu.uberdust.pcmonitor.jobs.unix.UpTime;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 9:06 PM
 */
public final class PcMonitor {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PcMonitor.class);
    /**
     * Sampling interval.
     */
    private static final long INTERVALL = 30 * 60 * 1000;
    /**
     * local Hostname.
     */
    private static String hostname;
    /**
     * Testbed prefix.
     */
    private static String prefix = "";
    /**
     * Testbed url.
     */
    private static String testbedServer = "";

    /**
     * Constructor.
     */
    private PcMonitor() {
        LOGGER.debug("PcMonitor");
    }

    /**
     * main class.
     *
     * @param args cmd line arguments
     */
    public static void main(final String[] args) {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));
        final Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(System.getProperty("user.home") + "/.pcmonitor"));
            if (!properties.contains("testbed_server")) {
                errorMessage();
            }
            if (!properties.contains("prefix")) {
                errorMessage();
            }

            testbedServer = (String) properties.get("testbed_server");

            prefix = (String) properties.get("prefix");

        } catch (IOException e) {
            errorMessage();
            return;
        }

        LOGGER.info("starting");
        hostname = findHostname();

        while (true) {
            runUnix();

            try {
                Thread.sleep(INTERVALL);
            } catch (InterruptedException e) {
                LOGGER.fatal(e);
                return;
            }
        }
    }

    /**
     * Runs all registered Unix jobs.
     */
    private static void runUnix() {
        final DiskFree diskFree = new DiskFree();
        new RestCommiter(diskFree.getReadings());
        final CpuUsage cpuUsage = new CpuUsage();
        new RestCommiter(cpuUsage.getReadings());
        final BatteryCharge batteryCharge = new BatteryCharge();
        new RestCommiter(batteryCharge.getReadings());
        final HostAlive hostAlive = new HostAlive();
        new RestCommiter(hostAlive.getReadings());
        final KernelVersion kernelVersion = new KernelVersion();
        new RestCommiter(kernelVersion.getReadings());
        final MemFree memFree = new MemFree();
        new RestCommiter(memFree.getReadings());
        final UpTime uptime = new UpTime();
        new RestCommiter(uptime.getReadings());
        final DiskTemp diskTemp = new DiskTemp();
        new RestCommiter(diskTemp.getReadings());
    }

    /**
     * Displays an error message to user.
     */
    private static void errorMessage() {
        LOGGER.fatal("No Property file detected please create a .pcmonitor file in user.home");
        LOGGER.fatal("The property file should contain the testbed_server and prefix");

    }

    /**
     * Gets the hostname from the machine.
     *
     * @return the hostname.
     */
    private static String findHostname() {
        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("hostname");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            final String line = reader.readLine();
            while (line != null) {
                return line;
            }
        } catch (IOException e) {
            LOGGER.fatal(e);
        } catch (InterruptedException e) {
            LOGGER.fatal(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        return "";
    }

    /**
     * Returns the generated hostname.
     *
     * @return the hostname of the machine.
     */
    public static String getHostname() {
        return hostname;
    }

    /**
     * Returns the prefix read from the property file.
     *
     * @return the testbed prefix.
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * Returns the server url read from the property file.
     *
     * @return the server url.
     */
    public static String getTestbedServer() {
        return testbedServer;
    }
}
