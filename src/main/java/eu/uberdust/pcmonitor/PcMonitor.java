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
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 9:06 PM
 */
public class PcMonitor {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PcMonitor.class);
    public static String hostname;
    public static String prefix = "urn:qopbot:";
    public static String testbed_server = "http://qopbot.dyndns.org:8081/rest/testbed/1/";

    public static void main(final String[] args) {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));
        LOGGER.info("starting");
        hostname = getHostname();


        DiskFree diskFree = new DiskFree();
        new RestCommiter(diskFree.getReadings());
        CpuUsage cpuUsage = new CpuUsage();
        new RestCommiter(cpuUsage.getReadings());
        BatteryCharge batteryCharge = new BatteryCharge();
        new RestCommiter(batteryCharge.getReadings());
        HostAlive hostAlive = new HostAlive();
        new RestCommiter(hostAlive.getReadings());
        KernelVersion kernelVersion = new KernelVersion();
        new RestCommiter(kernelVersion.getReadings());
        MemFree memFree = new MemFree();
        new RestCommiter(memFree.getReadings());
        UpTime uptime = new UpTime();
        new RestCommiter(uptime.getReadings());
        DiskTemp diskTemp = new DiskTemp();
        new RestCommiter(diskTemp.getReadings());
    }


    private static String getHostname() {
        try {
            Process runtime = Runtime.getRuntime().exec("hostname");
            runtime.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
            String line = reader.readLine();
            while (line != null) {

                return line;
            }

        } catch (IOException e) {
            LOGGER.fatal(e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }
}
