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
public class PcMonitor {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PcMonitor.class);
    public static String hostname;
    private static String prefix = "";
    private static String testbedServer = "";

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
        hostname = getHostname();

        while (true) {
            runAll();

            try {
                Thread.sleep(30 * 60 * 1000);
            } catch (InterruptedException e) {
                LOGGER.fatal(e);
                return;
            }
        }
    }

    private static void runAll() {
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

    private static void errorMessage() {
        LOGGER.fatal("No Property file detected please create a .pcmonitor file in user.home");
        LOGGER.fatal("The property file should contain the testbed_server and prefix");

    }


    private static String getHostname() {
        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("hostname");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
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

    public static String getPrefix() {
        return prefix;
    }

    public static String getTestbedServer() {
        return testbedServer;
    }
}
